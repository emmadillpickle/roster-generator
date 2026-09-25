package com.emmaong.rostermanager.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.dto.request.PersonRequest;
import com.emmaong.rostermanager.backend.exception.ResourceNotFoundException;
import com.emmaong.rostermanager.backend.repository.PersonRepository;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.SoloRole;

@Service
public class PersonService {
	private final PersonRepository personRepository;
	private final RoleService roleService;
	
	public PersonService(PersonRepository personRepository, RoleService roleService) {
		this.personRepository = personRepository;
		this.roleService = roleService;
	}
	
	public List<Person> findAll() {
		return personRepository.findAll();
	}
	
	public Person findById(long id) {
		Optional<Person> optional = personRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Person", id);
		return optional.get();
	}
	
	public Person save(PersonRequest request) {
		Person person = buildPerson(request);
		
		validatePerson(person);
		return personRepository.save(person);
	}
	
	public Person update(long id, PersonRequest request) {
		Person person = buildPerson(request);
		person.setId(id);
		
		validatePerson(person);
		return personRepository.save(person);
	}
	
	public void deleteById(long id) {
		personRepository.deleteById(id);
	}
	
	private Person buildPerson(PersonRequest request) {
		Set<PersonRole> roles = request.getRoles() == null 
									? new HashSet<>() 
									: request.getRoles()
								        .stream()
								        .map(pr -> {
								            Role role = roleService.findById(pr.getRoleId());
						
								            return PersonRole.builder()
								                    .role((SoloRole) role)
								                    .maxShifts(pr.getMaxShifts() == null ? -1 : pr.getMaxShifts())
								                    .shiftsWorked(pr.getShiftsWorked() == null ? 0 : pr.getShiftsWorked())
								                    .build();
								        })
								        .collect(Collectors.toSet());
		
		return Person.builder()
						.name(request.getName())
						.cooldown(request.getCooldown() == null ? 0 : request.getCooldown())
						.lastServed(request.getLastServed())
						.roles(roles)
						.unavailability(request.getUnavailability() == null ? new HashSet<>() : request.getUnavailability())
						.build();
	}
	
	private void validatePerson(Person person) {
		if (person.getName() == null || person.getName().isBlank()) {
			throw new IllegalArgumentException("Person name cannot be empty.");
		}
	}
}
