package com.emmaong.rostermanager.models;

import java.util.Set;

public class WorshipPairing {
	private long id;
	private Set<Person> people;
	
	
	public WorshipPairing(long id, Set<Person> people) {
		super();
		this.id = id;
		this.people = people;
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
	
	
}
