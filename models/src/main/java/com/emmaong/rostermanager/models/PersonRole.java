package com.emmaong.rostermanager.models;

public class PersonRole {
	private SoloRole role;
	private int maxShifts;
	private int shiftsWorked;
	
	private PersonRole() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PersonRole personRole = new PersonRole();

        public Builder role(SoloRole role) {
            personRole.role = role;
            return this;
        }

        public Builder maxShifts(int maxShifts) {
            personRole.maxShifts = maxShifts;
            return this;
        }

        public Builder shiftsWorked(int shiftsWorked) {
            personRole.shiftsWorked = shiftsWorked;
            return this;
        }

        public PersonRole build() {
            return personRole;
        }
    }

	public Role getRole() {
		return role;
	}

	public void setRole(SoloRole role) {
		this.role = role;
	}

	public int getMaxShifts() {
		return maxShifts;
	}

	public void setMaxShifts(int maxShifts) {
		this.maxShifts = maxShifts;
	}
	
	public int getShiftsWorked() {
		return shiftsWorked;
	}
	
	public void setShiftsWorked(int shiftsWorked) {
		this.shiftsWorked = shiftsWorked;
	}

	@Override
	public String toString() {
		return "PersonRole [role=" + role + ", maxShifts=" + maxShifts + "]";
	}
	
}
