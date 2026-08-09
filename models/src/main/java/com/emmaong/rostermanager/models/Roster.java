package com.emmaong.rostermanager.models;

import java.util.List;

public class Roster {
	private List<RosterEntry> rosterEntries;

	public Roster(List<RosterEntry> rosterEntries) {
		super();
		this.rosterEntries = rosterEntries;
	}

	public List<RosterEntry> getRosterEntries() {
		return rosterEntries;
	}

	public void setRosterEntries(List<RosterEntry> rosterEntries) {
		this.rosterEntries = rosterEntries;
	}
	
}
