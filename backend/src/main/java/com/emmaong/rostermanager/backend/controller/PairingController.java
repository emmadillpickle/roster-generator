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

import com.emmaong.rostermanager.backend.dto.request.PairingRequest;
import com.emmaong.rostermanager.backend.service.PairingService;
import com.emmaong.rostermanager.models.Pairing;

@RestController
@RequestMapping("/api/pairings")
public class PairingController {
	private final PairingService pairingService;
	
	public PairingController (PairingService pairingService) {
		this.pairingService = pairingService;
	}
	
	@GetMapping
	public List<Pairing> getAllPairings() {
		return pairingService.findAll();
	}
	
	@GetMapping("/{id}")
	public Pairing getPairing(@PathVariable long id) {
		return pairingService.findById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Pairing createPairing(@RequestBody PairingRequest request) {
		return pairingService.save(request);
	}
	
	@PutMapping("/{id}")
	public Pairing updatePairing(@PathVariable long id, @RequestBody PairingRequest request) {
		return pairingService.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePairing(@PathVariable long id) {
		pairingService.deleteById(id);
	}
	
	@DeleteMapping("/{pairingId}/{personId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePairing(@PathVariable long pairingId, @PathVariable long personId) {
		pairingService.removePersonById(pairingId, personId);
	}
}
