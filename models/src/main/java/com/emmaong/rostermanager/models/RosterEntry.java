package com.emmaong.rostermanager.models;

import java.util.List;

public class RosterEntry {
	private Event event;
	private List<Assignment> assignments;
	
	public RosterEntry(Event event, List<Assignment> assignments) {
		super();
		this.event = event;
		this.assignments = assignments;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public List<Assignment> getAssignments() {
		return assignments;
	}
	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}
	
}
