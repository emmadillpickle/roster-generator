package com.emmaong.rostermanager.models;

public class PairedRole implements Role {
	private long id;
	private String name;
	
	private PairedRole() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PairedRole role = new PairedRole();

        public Builder id(long id) {
            role.id = id;
            return this;
        }

        public Builder name(String name) {
            role.name = name;
            return this;
        }

        public PairedRole build() {
            return role;
        }
    }
	
	@Override
	public long getId() {
		return id;
	}
	@Override
	public String getName() {
		return name;
	}
	
	
}
