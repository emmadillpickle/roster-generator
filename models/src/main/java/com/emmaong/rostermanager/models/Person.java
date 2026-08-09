package com.emmaong.rostermanager.models;

import java.time.LocalDate;
import java.util.Set;

public class Person {
	long id;
	String name;
	Set<Role> roles;
	Set<LocalDate> unavailability;
	
	
	public Person(long id, String name, Set<Role> roles, Set<LocalDate> unavailability) {
		super();
		this.id = id;
		this.name = name;
		this.roles = roles;
		this.unavailability = unavailability;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	public Set<LocalDate> getUnavailability() {
		return unavailability;
	}


	public void setUnavailability(Set<LocalDate> unavailability) {
		this.unavailability = unavailability;
	}
	
}
