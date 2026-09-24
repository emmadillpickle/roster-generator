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

import com.emmaong.rostermanager.backend.dto.request.PersonRequest;
import com.emmaong.rostermanager.backend.service.PersonService;
import com.emmaong.rostermanager.models.Person;

@RestController
@RequestMapping("/api/people")
public class PersonController {
	private final PersonService personService;
	
	public PersonController (PersonService personService) {
		this.personService = personService;
	}
	
	@GetMapping
	public List<Person> getAllPeople() {
		return personService.findAll();
	}
	
	@GetMapping("/{id}")
	public Person getPerson(@PathVariable long id) {
		return personService.findById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Person createPerson(@RequestBody PersonRequest request) {
		return personService.save(request);
	}
	
	@PutMapping("/{id}")
	public Person updatePerson(@PathVariable long id, @RequestBody PersonRequest request) {
		return personService.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePerson(@PathVariable long id) {
		personService.deleteById(id);
	}	
}
