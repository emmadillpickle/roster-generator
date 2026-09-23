package com.emmaong.rostermanager.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RosterEntry {
	private Event event;
	
	@Builder.Default
	private List<SoloAssignment> soloAssignments = new ArrayList<>();
	
	@Builder.Default
	private List<PairedAssignment> pairedAssignments = new ArrayList<>();

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
}
