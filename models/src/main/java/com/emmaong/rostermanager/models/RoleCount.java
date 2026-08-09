package com.emmaong.rostermanager.models;

public class RoleCount {
	private Role role;
	private int requiredCount;
	
	public RoleCount(Role role, int requiredCount) {
		super();
		this.role = role;
		this.requiredCount = requiredCount;
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
