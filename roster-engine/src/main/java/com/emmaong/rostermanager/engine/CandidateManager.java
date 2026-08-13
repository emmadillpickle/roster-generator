package com.emmaong.rostermanager.engine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emmaong.rostermanager.engine.RosterEngine.Builder;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.SoloRole;

public class CandidateManager {
	private List<Person> people;
	private List<Pairing> pairings;
	private List<Role> roles;
	
	private Map<SoloRole, List<Person>> allSoloCandidates = new HashMap<>();
	private Map<PairedRole, List<Pairing>> allPairedCandidates = new HashMap<>();
	
	private CandidateManager() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final CandidateManager manager = new CandidateManager();
        
        private Builder() {
        	manager.people = new ArrayList<>();
        	manager.pairings = new ArrayList<>();
            manager.roles = new ArrayList<>();
        }

        public Builder people(List<Person> people) {
        	manager.people = people;
            return this;
        }
        
        public Builder pairings(List<Pairing> pairings) {
        	manager.pairings = pairings;
            return this;
        }

        public Builder roles(List<Role> roles) {
        	manager.roles = roles;
            return this;
        }

        public CandidateManager build() {
        	manager.setup();
            return manager;
        }
    }
    
    public Map<SoloRole, List<Person>> getAllSoloCandidates() {
	    return allSoloCandidates;
	}

	public Map<PairedRole, List<Pairing>> getAllPairedCandidates() {
	    return allPairedCandidates;
	}
	
	private void setup() {
		processRoles();
		processPeople();
		processPairings();
	}
	
	private void processRoles() {
		for (Role role : roles) {
			if (role instanceof SoloRole) {
				allSoloCandidates.put((SoloRole) role, new ArrayList<>());
			} else {
				allPairedCandidates.put((PairedRole) role, new ArrayList<>());
			}
		}
	}
	
	private void processPeople() {
		for (Person person: people) {
			for (PersonRole personRole : person.getRoles()) {
				allSoloCandidates.get(personRole.getRole()).add(person);
			}
		}
	}
	
	private void processPairings() {
		for (Pairing pairing : pairings) {
			allPairedCandidates.get(pairing.getRole()).add(pairing);
		}
	}
	
	public List<Person> getEligibleCandidates(SoloRole role, LocalDate date) {
		List<Person> eligibleCandidates = allSoloCandidates.get(role).stream()
			    .filter(person -> person.isAvailableOn(date))
			    .filter(person -> person.isNotOnCooldown(date))
			    .filter(person -> person.hasRemainingShiftsFor(role))
			    .toList();
		
		return eligibleCandidates;
	}
	
	public List<Pairing> getEligibleCandidates(PairedRole role, LocalDate date) {
		List<Pairing> eligibleCandidates = allPairedCandidates.get(role).stream()
				.filter(pairing -> pairing.isAvailableOn(date))
				.filter(pairing -> pairing.isNotOnCooldown(date))
				.filter(pairing -> pairing.hasRemainingShiftsFor(role))
				.toList();
		
		return eligibleCandidates;
	}
}
