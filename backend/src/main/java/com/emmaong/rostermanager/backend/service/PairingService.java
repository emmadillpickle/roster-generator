package com.emmaong.rostermanager.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.dto.request.PairingRequest;
import com.emmaong.rostermanager.backend.exception.ResourceNotFoundException;
import com.emmaong.rostermanager.backend.repository.PairingRepository;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.Role;

@Service
public class PairingService {
	private final PairingRepository pairingRepository;
	private final RoleService roleService;
	private final PersonService personService;
	
	public PairingService(PairingRepository pairingRepository, RoleService roleService, PersonService personService) {
		this.pairingRepository = pairingRepository;
		this.roleService = roleService;
		this.personService = personService;
	}
	
	public List<Pairing> findAll() {
		return pairingRepository.findAll();
	}
	
	public Pairing findById(long id) {
		Optional<Pairing> optional = pairingRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Pairing", id);
		return optional.get();
	}
	
	public Pairing save(PairingRequest request) {
		Pairing pairing = buildPairing(request);
		
		validatePairing(pairing);
		return pairingRepository.save(pairing);
	}
	
	public Pairing update(long id, PairingRequest request) {
		Pairing pairing = buildPairing(request);
		pairing.setId(id);
		
		validatePairing(pairing);
		return pairingRepository.save(pairing);
	}
	
	public void deleteById(long id) {
		pairingRepository.deleteById(id);
	}
	
	public void removePersonById(long pairingId, long personId) {
		pairingRepository.removePersonById(pairingId, personId);
	}
	
	private Pairing buildPairing(PairingRequest request) {
		if (request.getPeople() == null) {
			throw new IllegalArgumentException("Pairing must have at least one person");
		}
		
		Role role = roleService.findById(request.getRoleId());
		
		Set<Person> people = request.getPeople()
						        .stream()
						        .map(personId -> personService.findById(personId))
						        .collect(Collectors.toSet());
		
		return Pairing.builder()
						.role((PairedRole) role)
						.shiftsWorked(request.getShiftsWorked() == null ? 0 : request.getShiftsWorked())
						.maxShifts(request.getMaxShifts() == null ? -1 : request.getMaxShifts())
						.people(people)
						.build();
		
	}
	
	private void validatePairing(Pairing pairing) {
		if (pairing.getRole() == null) {
			throw new IllegalArgumentException("Pairing role cannot be null");
		}
		
		if (pairing.getMaxShifts() < -1) {
			throw new IllegalArgumentException("Pairing max shifts cannot be less than -1");
		}
		
		for (Person person : pairing.getPeople()) {
			if (person == null) {
				throw new IllegalArgumentException("Person in pairing cannot be null");
			}
			
			if (person.getId() <= 0L) {
				throw new IllegalArgumentException("Person in pairing has invalid ID: " + person.getId());
			}
		}
	}
}
