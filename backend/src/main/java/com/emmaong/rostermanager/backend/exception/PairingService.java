package com.emmaong.rostermanager.backend.exception;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.repository.PairingRepository;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;

@Service
public class PairingService {
	private final PairingRepository pairingRepository;
	
	public PairingService(PairingRepository pairingRepository) {
		this.pairingRepository = pairingRepository;
	}
	
	public List<Pairing> findAll() {
		return pairingRepository.findAll();
	}
	
	public Pairing findById(long id) {
		Optional<Pairing> optional = pairingRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Pairing", id);
		return optional.get();
	}
	
	public Pairing save(Pairing pairing) {
		validatePairing(pairing);
		return pairingRepository.save(pairing);
	}
	
	public void deleteById(long id) {
		pairingRepository.deleteById(id);
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
