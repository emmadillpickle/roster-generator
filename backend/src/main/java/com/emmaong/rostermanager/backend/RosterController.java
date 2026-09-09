package com.emmaong.rostermanager.backend;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emmaong.rostermanager.backend.repository.EventRepository;
import com.emmaong.rostermanager.backend.repository.PairingRepository;
import com.emmaong.rostermanager.backend.repository.PersonRepository;
import com.emmaong.rostermanager.backend.repository.RoleRepository;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.RoleCount;
import com.emmaong.rostermanager.models.SoloRole;

@RestController
public class RosterController {
	
	private final PersonRepository personRepository;
	private final RoleRepository roleRepository;
	private final EventRepository eventRepository;
	private final PairingRepository pairingRepository;
	
	public RosterController(PersonRepository personRepository, RoleRepository roleRepository, EventRepository eventRepository, PairingRepository pairingRepository) {
		this.personRepository = personRepository;
		this.roleRepository = roleRepository;
		this.eventRepository = eventRepository;
		this.pairingRepository = pairingRepository;
	}

    @GetMapping("/healthcheck")
    public String hello() {
        return "healthy :)";
    }
    
    @GetMapping("/test") 
    public String test() {
    	List<Pairing> pairings = pairingRepository.findAll();
    	String ret = "";
    	
    	for (Pairing received : pairings) {
    		ret = ret + " // " + received.getId() + ", " + received.getPeople().toString() + ", " + received.getRole().getName() + ", " + received.getMaxShifts();
    	}
    	
    	return ret;
    }
}