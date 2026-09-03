package com.emmaong.rostermanager.models;

public class PairedAssignment implements Assignment {
	private Pairing pairing;
	private PairedRole role;
	
	private PairedAssignment() { } 
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final PairedAssignment assignment = new PairedAssignment();
		
		public Builder pairing(Pairing pairing) {
			assignment.pairing = pairing;
			return this;
		}
		
		public Builder role(PairedRole role) {
			assignment.role = role;
			return this;
		}
		
		public PairedAssignment build() {
			return assignment;
		}
	}


	public Pairing getPairing() {
		return pairing;
	}


	public void setPairing(Pairing pairing) {
		this.pairing = pairing;
	}


	public PairedRole getRole() {
		return role;
	}


	public void setRole(PairedRole role) {
		this.role = role;
	}
	
}
