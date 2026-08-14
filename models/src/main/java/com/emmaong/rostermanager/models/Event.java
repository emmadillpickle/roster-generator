package com.emmaong.rostermanager.models;

import java.time.LocalDate;
import java.util.Set;

import com.emmaong.rostermanager.models.Pairing.Builder;

public class Event {
	private long id;
	private LocalDate date;
	private Set<RoleCount> roles;
	
	
	private Event() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Event event = new Event();

        public Builder id(long id) {
            event.id = id;
            return this;
        }
        
        public Builder date(LocalDate date) {
        	event.date = date;
        	return this;
        }
        
        public Builder roles(Set<RoleCount> roles) {
        	event.roles = roles;
        	return this;
        }

        public Event build() {
            return event;
        }
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Set<RoleCount> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleCount> roles) {
		this.roles = roles;
	}
	
	
}
