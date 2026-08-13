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
	private Set<PersonRole> roles;
	private Set<LocalDate> unavailability;
	
	
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

	public boolean isAvailableOn(LocalDate date) {
		return !unavailability.contains(date);
	}
	
	public int getcooldown() {
		return cooldown;
	}
	
	public Set<PersonRole> getRoles() {
		return roles;
	}
	
	public void setLastServed(LocalDate lastServed) {
		this.lastServed = lastServed;
	}
	
	public LocalDate getLastServed() {
		return lastServed;
	}
	
	
}
