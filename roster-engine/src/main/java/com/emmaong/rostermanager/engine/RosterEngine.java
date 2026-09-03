package com.emmaong.rostermanager.engine;

import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Roster;
import com.emmaong.rostermanager.models.SoloRole;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.RoleCount;
import com.emmaong.rostermanager.models.PersonRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RosterEngine {
	private List<Person> people;
	private List<Pairing> pairings;
	private List<Role> roles;
	private List<Event> events;
	
	private CandidateManager candidateManager;
	private SlackCalculator slackCalculator = new SlackCalculator();
	private RosterBuilder rosterBuilder = new RosterBuilder();
	
	private RosterEngine() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final RosterEngine engine = new RosterEngine();
        
        private Builder() {
            engine.people = new ArrayList<>();
            engine.pairings = new ArrayList<>();
            engine.roles = new ArrayList<>();
            engine.events = new ArrayList<>();
        }

        public Builder people(List<Person> people) {
            engine.people = people;
            return this;
        }

        public Builder pairings(List<Pairing> pairings) {
            engine.pairings = pairings;
            return this;
        }

        public Builder roles(List<Role> roles) {
            engine.roles = roles;
            return this;
        }

        public Builder events(List<Event> events) {
            engine.events = events;
            return this;
        }

        public RosterEngine build() {
        	engine.setup();
            return engine;
        }
    }
    
    private void setup() {
    	candidateManager = CandidateManager.builder()
								.people(people)
								.pairings(pairings)
								.roles(roles)
								.build();
    }
    
    public Roster generateRoster() {
    	for (Event event : events) {
    		List<RoleCount> roleCounts = event.getRoles();
    		List<Role> roles = new ArrayList<>();
    		Set<Person> used = new HashSet<Person>();
    		
    		// turn rolecounts into a list of roles to be filled
    		for (RoleCount roleCount : roleCounts) {
    			for (int count = 0; count < roleCount.getRequiredCount(); count++) {
    				roles.add(roleCount.getRole());
    			}
    		}
    		
    		// while we still have roles to fill
    		while (!roles.isEmpty()) {
    			
    			Map<SoloRole, List<Person>> soloCandidates = new HashMap<>();
				Map<PairedRole, List<Pairing>> pairedCandidates = new HashMap<>();
    			
	    		// get a list of eligible candidates for each role to be filled
	    		for (Role role : roles) {
					if (role instanceof PairedRole && !pairedCandidates.containsKey(role)) {
						PairedRole pairedRole = (PairedRole) role;
						pairedCandidates.put(pairedRole, candidateManager.getEligibleCandidates(pairedRole, event.getDate(), used));
					} else if (role instanceof SoloRole && !soloCandidates.containsKey(role)) {
						SoloRole soloRole = (SoloRole) role;
						soloCandidates.put(soloRole, candidateManager.getEligibleCandidates(soloRole, event.getDate(), used));
					}
	    		}
	    		
	    		// fill the role with the least amount of candidates
	    		Role roleToFill = getRoleWithLeastCandidates(soloCandidates, pairedCandidates);
	    		
	    		// if there are no candidates for this role, flag it and jump to next role
	    		if (roleToFill instanceof PairedRole) {
	    			if (pairedCandidates.get(roleToFill).isEmpty()) {
	    				rosterBuilder.flagUnfillable((PairedRole) roleToFill, event);
	    				roles.remove(roleToFill);
	    				continue;
	    			}
	    		} else {
	    			if (soloCandidates.get(roleToFill).isEmpty()) {
	    				rosterBuilder.flagUnfillable((SoloRole) roleToFill, event);
	    				roles.remove(roleToFill);
	    				continue;
	    			}
	    		}
	    		
	    		// get remaining events to calculate slack
	    		List<Event> remainingEvents = getRemainingEvents(events, event.getDate());
	    		
	    		// pick a candidate, assign them to the roster, and update our tracking variables
	    		if (roleToFill instanceof PairedRole) {
	    			PairedRole pairedRole = (PairedRole) roleToFill;
	    			Pairing pick = getCandidateWithLeastSlack(pairedCandidates.get(roleToFill), pairedRole, remainingEvents);
	    			rosterBuilder.assign(pick, pairedRole, event);
	    			
	    			pick.updateCounters(event.getDate());
	    			for (Person person : pick.getPeople()) {
	    				used.add(person);
	    			}
	    		} else {
	    			SoloRole soloRole = (SoloRole) roleToFill;
	    			Person pick = getCandidateWithLeastSlack(soloCandidates.get(roleToFill), soloRole, remainingEvents);
	    			rosterBuilder.assign(pick, soloRole, event);
	    			
	    			pick.updateCounters(soloRole, event.getDate());
	    			used.add(pick);
	    		}
	    		
	    		// mark this role as filled
	    		roles.remove(roleToFill);
    		}
    	}
    	
    	return rosterBuilder.getRoster();
    }
    
    private Role getRoleWithLeastCandidates(Map<SoloRole, List<Person>> soloCandidates, Map<PairedRole, List<Pairing>> pairedCandidates) {
    	Role soloRole = soloCandidates.entrySet().stream()
    	        .min(Comparator.comparingInt(entry -> entry.getValue().size()))
    	        .map(Map.Entry::getKey)
    	        .orElse(null);
    	
    	Role pairedRole = pairedCandidates.entrySet().stream()
    	        .min(Comparator.comparingInt(entry -> entry.getValue().size()))
    	        .map(Map.Entry::getKey)
    	        .orElse(null);
    	
    	if (soloRole == null) return pairedRole;
    	if (pairedRole == null) return soloRole;
    	
    	return soloCandidates.get(soloRole).size() < pairedCandidates.get(pairedRole).size()
    				? soloRole
    				: pairedRole;
    }
    
    private Person getCandidateWithLeastSlack(List<Person> candidates, SoloRole role, List<Event> remainingEvents) {
    	Person pick = candidates.get(0);
    	int slack = slackCalculator.calculateSlack(pick, role, remainingEvents);
    	
    	for (int count = 1; count < candidates.size(); count++) {
    		Person currentPerson = candidates.get(count);
    		int currentSlack = slackCalculator.calculateSlack(currentPerson, role, remainingEvents);
    		
    		if (currentSlack < slack) {
    			pick = currentPerson;
    			slack = currentSlack;
    		}
    	}
    	
    	return pick;
    }
    
    private Pairing getCandidateWithLeastSlack(List<Pairing> candidates, PairedRole role, List<Event> remainingEvents) {
    	Pairing pick = candidates.get(0);
    	int slack = slackCalculator.calculateSlack(pick, remainingEvents);
    	
    	for (int count = 1; count < candidates.size(); count++) {
    		Pairing currentPairing = candidates.get(count);
    		int currentSlack = slackCalculator.calculateSlack(currentPairing, remainingEvents);
    		
    		if (currentSlack < slack) {
    			pick = currentPairing;
    			slack = currentSlack;
    		}
    	}
    	
    	return pick;
    }
    
    private List<Event> getRemainingEvents(List<Event> events, LocalDate date) {
    	return events.stream()
    					.filter(event -> event.getDate().isAfter(date))
    					.toList();
    }
}
