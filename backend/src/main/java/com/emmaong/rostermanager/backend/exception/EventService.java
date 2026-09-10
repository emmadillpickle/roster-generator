package com.emmaong.rostermanager.backend.exception;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.repository.EventRepository;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.RoleCount;

@Service
public class EventService {
	private final EventRepository eventRepository;
	
	public EventService(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	public List<Event> findAll() {
		return eventRepository.findAll();
	}
	
	public Event findById(long id) {
		Optional<Event> optional = eventRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Event", id);
		return optional.get();
	}
	
	public Event save(Event event) {
		validateEvent(event);
		return eventRepository.save(event);
	}
	
	public void deleteById(long id) {
		eventRepository.deleteById(id);
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
