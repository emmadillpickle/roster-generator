package com.emmaong.rostermanager.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.engine.RosterEngine;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.Roster;

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
	
	public Roster generateRoster() {
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
		
		return roster;
	}
	
	
}
