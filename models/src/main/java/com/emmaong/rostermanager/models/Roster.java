package com.emmaong.rostermanager.models;

import java.util.ArrayList;
import java.util.List;

public class Roster {
	private List<RosterEntry> rosterEntries = new ArrayList<>();

	private Roster() { }
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final Roster roster = new Roster();
		
		public Roster build() {
			return roster;
		}
	}

	public List<RosterEntry> getRosterEntries() {
		return rosterEntries;
	}

	public void addRosterEntry(RosterEntry entry) {
		rosterEntries.add(entry);
	}
}
