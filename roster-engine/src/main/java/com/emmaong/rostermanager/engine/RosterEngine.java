package com.emmaong.rostermanager.engine;

import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Roster;
import com.emmaong.rostermanager.models.SoloRole;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.PersonRole;

import java.time.LocalDate;
import java.util.ArrayList;
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
	
	// private PersonManager personManager = new PersonManager();
	// personManager.getEligiblePeople(role, date)
	// personManager.updateCounters(person, role, date)
	
	private CandidateManager candidateManager;
	
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
	
}
