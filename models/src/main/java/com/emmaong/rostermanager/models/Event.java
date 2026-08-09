package com.emmaong.rostermanager.models;

import java.time.LocalDate;
import java.util.Set;

public class Event {
	private long id;
	private LocalDate date;
	private Set<RoleCount> roles;
	
	public Event(long id, LocalDate date, Set<RoleCount> roles) {
		super();
		this.id = id;
		this.date = date;
		this.roles = roles;
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
