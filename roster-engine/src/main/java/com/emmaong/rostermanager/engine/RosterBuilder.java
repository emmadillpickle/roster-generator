package com.emmaong.rostermanager.engine;

import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedAssignment;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.Roster;
import com.emmaong.rostermanager.models.RosterEntry;
import com.emmaong.rostermanager.models.SoloAssignment;
import com.emmaong.rostermanager.models.SoloRole;

public class RosterBuilder {
	private Roster roster = Roster.builder().build();
	
	public void assign(Person person, SoloRole role, Event event) {
		RosterEntry entry = roster.getRosterEntries().stream()
								.filter(re -> re.getEvent().equals(event))
								.findFirst()
								.orElse(null);
		
		if (entry == null) {
			entry = RosterEntry.builder().event(event).build();
			roster.addRosterEntry(entry);
		}
		
		SoloAssignment assignment = SoloAssignment.builder()
										.person(person)
										.role(role)
										.build();
		
		entry.addSoloAssignment(assignment);
	}
	
	public void assign(Pairing pairing, PairedRole role, Event event) {
		RosterEntry entry = roster.getRosterEntries().stream()
								.filter(re -> re.getEvent().equals(event))
								.findFirst()
								.orElse(null);
		
		if (entry == null) {
			entry = RosterEntry.builder().event(event).build();
			roster.addRosterEntry(entry);
		}
		
		PairedAssignment assignment = PairedAssignment.builder()
										.pairing(pairing)
										.role(role)
										.build();
		
		entry.addPairedAssignment(assignment);
	}
	
	public void flagUnfillable(SoloRole role, Event event) {
		RosterEntry entry = roster.getRosterEntries().stream()
						.filter(re -> re.getEvent().equals(event))
						.findFirst()
						.orElse(null);
		
		if (entry == null) {
			entry = RosterEntry.builder().event(event).build();
			roster.addRosterEntry(entry);
		}
		
		SoloAssignment soloAssignment = SoloAssignment.builder()
											.person(null)
											.role(role)
											.build();
		
		entry.addSoloAssignment(soloAssignment);
	}
	
	public void flagUnfillable(PairedRole role, Event event) {
		RosterEntry entry = roster.getRosterEntries().stream()
								.filter(re -> re.getEvent().equals(event))
								.findFirst()
								.orElse(null);
		
		if (entry == null) {
			entry = RosterEntry.builder().event(event).build();
			roster.addRosterEntry(entry);
		}
		
		PairedAssignment assignment = PairedAssignment.builder()
										.pairing(null)
										.role(role)
										.build();
		
		entry.addPairedAssignment(assignment);
	}
	
	public Roster getRoster() {
		return roster;
	}
}
