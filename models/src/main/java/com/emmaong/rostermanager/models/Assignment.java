package com.emmaong.rostermanager.models;

public class Assignment {
	private Person person;
	private Role role;
	
	
	public Assignment(Person person, Role role) {
		super();
		this.person = person;
		this.role = role;
	}


	public Person getPerson() {
		return person;
	}


	public void setPerson(Person person) {
		this.person = person;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
}
