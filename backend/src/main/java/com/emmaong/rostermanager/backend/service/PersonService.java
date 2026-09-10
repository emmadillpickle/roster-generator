package com.emmaong.rostermanager.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.exception.ResourceNotFoundException;
import com.emmaong.rostermanager.backend.repository.PersonRepository;
import com.emmaong.rostermanager.models.Person;

@Service
public class PersonService {
	private final PersonRepository personRepository;
	
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	public List<Person> findAll() {
		return personRepository.findAll();
	}
	
	public Person findById(long id) {
		Optional<Person> optional = personRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Person", id);
		return optional.get();
	}
	
	public Person save(Person person) {
		validatePerson(person);
		return personRepository.save(person);
	}
	
	public void deleteById(long id) {
		personRepository.deleteById(id);
	}
	
	private void validatePerson(Person person) {
		if (person.getName() == null || person.getName().isBlank()) {
			throw new IllegalArgumentException("Person name cannot be empty.");
		}
	}
}
