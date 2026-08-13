package com.emmaong.rostermanager.models;

public class SoloRole implements Role {
	private long id;
	private String name;
	
	private SoloRole() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final SoloRole role = new SoloRole();

        public Builder id(long id) {
            role.id = id;
            return this;
        }

        public Builder name(String name) {
            role.name = name;
            return this;
        }

        public SoloRole build() {
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