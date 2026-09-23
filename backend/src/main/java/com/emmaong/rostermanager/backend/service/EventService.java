package com.emmaong.rostermanager.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.dto.EventRequest;
import com.emmaong.rostermanager.backend.dto.RoleCountRequest;
import com.emmaong.rostermanager.backend.exception.ResourceNotFoundException;
import com.emmaong.rostermanager.backend.repository.EventRepository;
import com.emmaong.rostermanager.backend.service.RoleService;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.RoleCount;
import com.emmaong.rostermanager.models.Role;

@Service
public class EventService {
	private final EventRepository eventRepository;
	private final RoleService roleService;
	
	public EventService(EventRepository eventRepository, RoleService roleService) {
		this.eventRepository = eventRepository;
		this.roleService = roleService;
	}
	
	public List<Event> findAll() {
		return eventRepository.findAll();
	}
	
	public Event findById(long id) {
		Optional<Event> optional = eventRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Event", id);
		return optional.get();
	}
	
	public Event save(EventRequest request) {
		Event event = buildEvent(request);
		
		validateEvent(event);
		return eventRepository.save(event);
	}
	
	public Event update(long id, EventRequest request) {
		Event event = buildEvent(request);
		event.setId(id);
		
		validateEvent(event);
		return eventRepository.save(event);
	}
	
	public void deleteById(long id) {
		eventRepository.deleteById(id);
	}
	
	private Event buildEvent(EventRequest request) {
		List<RoleCount> roles = request.getRoles()
				.stream()
				.map(rc -> {
					Role role = roleService.findById(rc.getRoleId());
					
					return RoleCount.builder()
							.role(role)
							.requiredCount(rc.getRequiredCount())
							.build();
				})
				.toList();

		return Event.builder()
			.date(request.getDate())
			.roles(roles)
			.build();
	}
	
	private void validateEvent(Event event) {
		if (event.getDate() == null) {
			throw new IllegalArgumentException("Event date cannot be null");
		}
		
		for (RoleCount roleCount : event.getRoles()) {
			if (roleCount.getRequiredCount() <= 0) {
				throw new IllegalArgumentException("Required role count cannot be less than 1");
			}
		}
	}
}
