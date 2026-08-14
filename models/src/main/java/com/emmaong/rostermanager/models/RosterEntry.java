package com.emmaong.rostermanager.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RosterEntry {
	private Event event;
	private List<SoloAssignment> soloAssignments = new ArrayList<>();
	private List<PairedAssignment> pairedAssignments = new ArrayList<>();
	
	private RosterEntry() { } 
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final RosterEntry rosterEntry = new RosterEntry();
		
		public Builder event(Event event) {
			rosterEntry.event = event;
			return this;
		}
		
		public RosterEntry build() {
			return rosterEntry;
		}
	}
	
	public void addSoloAssignment(SoloAssignment assignment) {
		boolean validAssignment = soloAssignments.stream()
				.noneMatch(a -> 
					    Objects.equals(a.getPerson(), assignment.getPerson())
						&& Objects.equals(a.getRole(), assignment.getRole())
				);
		
		if (!validAssignment) {
			throw new IllegalArgumentException("trying to double-assign a role or person with new assignment: " + assignment.toString());
		}
		
		soloAssignments.add(assignment);
	}
	
	public void addPairedAssignment(PairedAssignment assignment) {
		boolean validAssignment = pairedAssignments.stream()
				.noneMatch(a -> 
					    Objects.equals(a.getPairing(), assignment.getPairing())
						&& Objects.equals(a.getRole(), assignment.getRole())
				);
		
		if (!validAssignment) {
			throw new IllegalArgumentException("trying to double-assign a role or person with new assignment: " + assignment.toString());
		}
		
		pairedAssignments.add(assignment);
	}
	
	public Event getEvent() {
		return event;
	}
	
	public List<SoloAssignment> getSoloAssignments() {
		return soloAssignments;
	}
	
	public List<PairedAssignment> getPairedAssignments() {
		return pairedAssignments;
	}
	
}
