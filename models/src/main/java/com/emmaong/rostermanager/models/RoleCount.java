package com.emmaong.rostermanager.models;

public class RoleCount {
	private Role role;
	private int requiredCount;
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final RoleCount roleCount = new RoleCount();
		
		public Builder role(Role role) {
			roleCount.role = role;
			return this;
		}
		
		public Builder requiredCount(int requiredCount) {
			roleCount.requiredCount = requiredCount;
			return this;
		}
		
		public RoleCount build() {
			return roleCount;
		}
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getRequiredCount() {
		return requiredCount;
	}

	public void setRequiredCount(int requiredCount) {
		this.requiredCount = requiredCount;
	}
	
	
}
