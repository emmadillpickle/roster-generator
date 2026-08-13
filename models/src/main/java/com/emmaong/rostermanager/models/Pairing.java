package com.emmaong.rostermanager.models;

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


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Set<Person> getPeople() {
		return people;
	}


	public void setPeople(Set<Person> people) {
		this.people = people;
	}
	
	public PairedRole getRole() {
		return role;
	}
	
	
}
