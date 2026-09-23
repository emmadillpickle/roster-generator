package com.emmaong.rostermanager.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.dto.request.RoleRequest;
import com.emmaong.rostermanager.backend.dto.response.RoleResponse;
import com.emmaong.rostermanager.backend.exception.ResourceNotFoundException;
import com.emmaong.rostermanager.backend.repository.RoleRepository;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.SoloRole;
import com.emmaong.rostermanager.models.PairedRole;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public List<RoleResponse> getAll() {
		List<Role> roles = roleRepository.findAll();
		List<RoleResponse> responses = new ArrayList<>();
		
		for (Role role : roles) {
			responses.add(buildRoleResponse(role));
		}
		
		return responses;
	}
	
	public List<Role> findAll() {
		return roleRepository.findAll();
	}
	
	public Role findById(long id) {
		Optional<Role> optional = roleRepository.findById(id);
		if (optional.isEmpty()) throw new ResourceNotFoundException("Role", id);
		return optional.get();
	}
	
	public RoleResponse getById(long id) {
		Optional<Role> optional = roleRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Role", id);
		Role role = optional.get();
		
		return buildRoleResponse(role);
	}
	
	public RoleResponse save(RoleRequest request) {
		Role role = buildRole(request);
		
		validateRole(role);
		return buildRoleResponse(roleRepository.save(role));
	}
	
	public RoleResponse update(long id, RoleRequest request) {
		Role role = buildRole(request);
		
		role.setId(id);
		validateRole(role);
		return buildRoleResponse(roleRepository.save(role));
	}
	
	public void deleteById(long id) {
		roleRepository.deleteById(id);
	}
	
	private Role buildRole(RoleRequest request) {
		
		switch (request.getRoleType()) {
		case "SOLO":
			return SoloRole.builder()
					.name(request.getName())
					.build();
			
		case "PAIRED":
			return PairedRole.builder()
					.name(request.getName())
					.build();
			
		default:
			throw new IllegalArgumentException("Role type should only be PAIRED or SOLO");
		}
	}
	
	private RoleResponse buildRoleResponse(Role role) {
		return RoleResponse.builder()
				.id(role.getId())
				.name(role.getName())
				.roleType(
						role instanceof SoloRole ? 
								"SOLO" : "PAIRED"
				)
				.build();
	}
	
	private void validateRole(Role role) {
		if (role.getName() == null || role.getName().isEmpty()) {
			throw new IllegalArgumentException("Role name cannot be empty");
		}
	}
}
