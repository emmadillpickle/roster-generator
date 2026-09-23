package com.emmaong.rostermanager.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emmaong.rostermanager.backend.dto.EventRequest;
import com.emmaong.rostermanager.backend.service.EventService;
import com.emmaong.rostermanager.models.Event;

@RestController
@RequestMapping("/api/events")
public class EventController {
	private final EventService eventService;
	
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}
	
	@GetMapping
	public List<Event> getAllEvents() {
		return eventService.findAll();
	}
	
	@GetMapping("/{id}")
	public Event getEvent(@PathVariable long id) {
		return eventService.findById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Event createEvent(@RequestBody EventRequest request) {
		return eventService.save(request);
	}
	
	@PutMapping("/{id}")
	public Event updateEvent(@PathVariable long id, @RequestBody EventRequest request) {
		return eventService.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEvent(@PathVariable long id) {
		eventService.deleteById(id);
	}	
	
}
