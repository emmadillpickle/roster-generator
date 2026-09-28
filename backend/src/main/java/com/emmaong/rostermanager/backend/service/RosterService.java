package com.emmaong.rostermanager.backend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.dto.response.PairedAssignmentResponse;
import com.emmaong.rostermanager.backend.dto.response.PairingResponse;
import com.emmaong.rostermanager.backend.dto.response.PersonResponse;
import com.emmaong.rostermanager.backend.dto.response.RosterEntryResponse;
import com.emmaong.rostermanager.backend.dto.response.RosterResponse;
import com.emmaong.rostermanager.backend.dto.response.SoloAssignmentResponse;
import com.emmaong.rostermanager.engine.RosterEngine;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedAssignment;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.Roster;
import com.emmaong.rostermanager.models.RosterEntry;
import com.emmaong.rostermanager.models.SoloAssignment;

@Service
public class RosterService {
	private final EventService eventService;
	private final RoleService roleService;
	private final PersonService personService;
	private final PairingService pairingService;
	
	public RosterService(EventService eventService, RoleService roleService,
			PersonService personService, PairingService pairingService) {
		super();
		this.eventService = eventService;
		this.roleService = roleService;
		this.personService = personService;
		this.pairingService = pairingService;
	}
	
	public RosterResponse generateRoster() {
		List<Event> events = eventService.findAll();
		List<Role> roles = roleService.findAll();
		List<Person> people = personService.findAll();
		List<Pairing> pairings = pairingService.findAll();
		
		Roster roster = RosterEngine.builder()
	            .people(people)
	            .roles(roles)
	            .events(events)
	            .pairings(pairings)
	            .build()
	            .generateRoster();
		
		return mapToResponse(roster);
	}
	
	private RosterResponse mapToResponse(Roster roster) {
		List<RosterEntryResponse> rosterEntryResponses = new ArrayList<>();
		
		for (RosterEntry entry : roster.getRosterEntries()) {
			List<SoloAssignmentResponse> soloAssignments = new ArrayList<>();
			List<PairedAssignmentResponse> pairedAssignments = new ArrayList<>();
			
			for (SoloAssignment soloAssignment : entry.getSoloAssignments()) {
				Person person = soloAssignment.getPerson();
				
				PersonResponse personResponse = PersonResponse.builder()
													.name(person.getName())
													.id(person.getId())
													.build();
				
				SoloAssignmentResponse soloAssignmentResponse = SoloAssignmentResponse.builder()
																	.role(soloAssignment.getRole())
																	.person(personResponse)
																	.build();
				
				soloAssignments.add(soloAssignmentResponse);
			}
			
			for (PairedAssignment pairedAssignment : entry.getPairedAssignments()) {
				Pairing pairing = pairedAssignment.getPairing();
				
				Set<PersonResponse> people = new HashSet<>();
				
				for (Person person : pairing.getPeople()) {
					PersonResponse personResponse = PersonResponse.builder()
							.name(person.getName())
							.id(person.getId())
							.build();
					
					people.add(personResponse);
				}
				
				PairingResponse pairingResponse = PairingResponse.builder()
													.id(pairing.getId())
													.people(people)
													.build();
				
				PairedAssignmentResponse pairedAssignmentResponse = PairedAssignmentResponse.builder()
																		.role(pairedAssignment.getRole())
																		.pairing(pairingResponse)
																		.build();
				
				pairedAssignments.add(pairedAssignmentResponse);
			}
			
			RosterEntryResponse rosterEntryResponse = RosterEntryResponse.builder()
														.eventId(entry.getEvent().getId())
														.date(entry.getEvent().getDate())
														.soloAssignments(soloAssignments)
														.pairedAssignments(pairedAssignments)
														.build();
			
			rosterEntryResponses.add(rosterEntryResponse);
			
		}
		
		RosterResponse rosterResponse = RosterResponse.builder()
											.entries(rosterEntryResponses)
											.build();
		
		return rosterResponse;
	}
	
	
}
