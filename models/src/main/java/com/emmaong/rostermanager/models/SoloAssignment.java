package com.emmaong.rostermanager.models;

public class SoloAssignment implements Assignment {
	private Person person;
	private SoloRole role;
	
	private SoloAssignment() { } 
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final SoloAssignment assignment = new SoloAssignment();
		
		public Builder person(Person person) {
			assignment.person = person;
			return this;
		}
		
		public Builder role(SoloRole role) {
			assignment.role = role;
			return this;
		}
		
		public SoloAssignment build() {
			return assignment;
		}
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


	public void setRole(SoloRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "SoloAssignment [person=" + person + ", role=" + role + "]";
	}
	
	
	
}
