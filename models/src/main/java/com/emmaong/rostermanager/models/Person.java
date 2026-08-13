package com.emmaong.rostermanager.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person {
	private long id;
	private String name;
	private int cooldown;
	private LocalDate lastServed;
	private Set<PersonRole> roles = new HashSet<>();
	private Set<LocalDate> unavailability = new HashSet<>();
	
	
	private Person() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Person person = new Person();

        public Builder id(long id) {
            person.id = id;
            return this;
        }

        public Builder name(String name) {
            person.name = name;
            return this;
        }

        public Builder cooldown(int cooldown) {
            person.cooldown = cooldown;
            return this;
        }

        public Builder lastServed(LocalDate lastServed) {
            person.lastServed = lastServed;
            return this;
        }

        public Builder roles(Set<PersonRole> roles) {
            person.roles = roles;
            return this;
        }

        public Builder unavailability(Set<LocalDate> unavailability) {
            person.unavailability = unavailability;
            return this;
        }

        public Person build() {
            return person;
        }
    }
    
    public LocalDate getLastServed() {
    	return lastServed;
    }
    
    public void setLastServed(LocalDate lastServed) {
    	this.lastServed = lastServed;
    }

	public boolean isAvailableOn(LocalDate date) {
		return !unavailability.contains(date);
	}
	
	public boolean isNotOnCooldown(LocalDate date) {
		if (lastServed == null) return true;
		
		LocalDate canServeNext = lastServed.plusWeeks(cooldown + 1);
		return date.isAfter(canServeNext) || date.isEqual(canServeNext);
	}
	
	public boolean hasRemainingShiftsFor(SoloRole role) {
		return roles.stream()
		        .filter(pr -> pr.getRole().getName().equals(role.getName()))
		        .findFirst()
		        .map(pr -> pr.getShiftsWorked() < pr.getMaxShifts())
		        .orElse(false);
	}
	
	public Set<PersonRole> getRoles() {
		return roles;
	}
	
	public void updateCounters(SoloRole role, LocalDate date) {
		PersonRole personRole = roles.stream()
				.filter(pr -> pr.getRole().getName().equals(role.getName()))
				.findFirst()
				.orElse(null);
		
		if (personRole == null) {
			throw new IllegalStateException("Trying to update " + name + "'s counter for " + role.getName() + ", but they don't do this role!");
		}
		
		lastServed = date;
		personRole.setShiftsWorked(personRole.getShiftsWorked() + 1);
	}
	
}
