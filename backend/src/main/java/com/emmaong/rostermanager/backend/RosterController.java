package com.emmaong.rostermanager.backend;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emmaong.rostermanager.backend.repository.EventRepository;
import com.emmaong.rostermanager.backend.repository.PersonRepository;
import com.emmaong.rostermanager.backend.repository.RoleRepository;
import com.emmaong.rostermanager.backend.repository.UnavailableDateRepository;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.SoloRole;

@RestController
public class RosterController {
	
	private final UnavailableDateRepository unavailableDateRepository;
	private final PersonRepository personRepository;
	private final RoleRepository roleRepository;
	private final EventRepository eventRepository;
	
	public RosterController(PersonRepository personRepository, UnavailableDateRepository unavailableDateRepository, RoleRepository roleRepository, EventRepository eventRepository) {
		this.personRepository = personRepository;
		this.unavailableDateRepository = unavailableDateRepository;
		this.roleRepository = roleRepository;
		this.eventRepository = eventRepository;
	}

    @GetMapping("/healthcheck")
    public String hello() {
        return "healthy :)";
    }
    
    @GetMapping("/test") 
    public String test() {
    	SoloRole role = SoloRole.builder().name("worship").build();
    	roleRepository.save(role);
    	
    	Event event = Event.builder().date(LocalDate.of(2025, 10, 12)).build();
    	eventRepository.save(event);
    	return "done!";
    }
}