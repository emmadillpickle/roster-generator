package com.emmaong.rostermanager.models;

import java.time.LocalDate;
import java.util.Set;

public class Pairing {
	private long id;
	private Set<Person> people;
	private PairedRole role;
	private int maxShifts;
	private int shiftsWorked;
	
	
	private Pairing() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Pairing pairing = new Pairing();

        public Builder id(long id) {
            pairing.id = id;
            return this;
        }

        public Builder people(Set<Person> people) {
            pairing.people = people;
            return this;
        }

        public Builder role(PairedRole role) {
            pairing.role = role;
            return this;
        }

        public Builder maxShifts(int maxShifts) {
            pairing.maxShifts = maxShifts;
            return this;
        }

        public Builder shiftsWorked(int shiftsWorked) {
            pairing.shiftsWorked = shiftsWorked;
            return this;
        }

        public Pairing build() {
            return pairing;
        }
    }

	public PairedRole getRole() {
		return role;
	}
	
	public int getShiftsWorked() {
		return shiftsWorked;
	}
	
	public int getMaxShifts() {
		return maxShifts;
	}
	
	public boolean isAvailableOn(LocalDate date) {
		boolean canServe = true;
		
		for (Person person : people) {
			canServe = canServe && person.isAvailableOn(date);
		}
		
		return canServe;
	}
	
	public boolean isNotOnCooldown(LocalDate date) {
		boolean canServe = true;
		
		for (Person person : people) {
			canServe = canServe && person.isNotOnCooldown(date);
		}
		
		return canServe;
	}
	
	public boolean hasRemainingShiftsFor(PairedRole role) {
		return shiftsWorked < maxShifts;
		
	}
	
	public void updateCounters(LocalDate date) {
		shiftsWorked++;
		
		for (Person person : people) {
			person.setLastServed(date);
		}
	}
	
}
