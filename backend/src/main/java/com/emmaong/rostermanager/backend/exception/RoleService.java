package com.emmaong.rostermanager.backend.exception;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emmaong.rostermanager.backend.repository.RoleRepository;
import com.emmaong.rostermanager.models.Role;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public List<Role> findAll() {
		return roleRepository.findAll();
	}
	
	public Role findById(long id) {
		Optional<Role> optional = roleRepository.findById(id);
		
		if (optional.isEmpty()) throw new ResourceNotFoundException("Role", id);
		return optional.get();
	}
	
	public Role save(Role role) {
		validateRole(role);
		return roleRepository.save(role);
	}
	
	public void deleteById(long id) {
		roleRepository.deleteById(id);
	}
	
	private void validateRole(Role role) {
		if (role.getName() == null || role.getName().isEmpty()) {
			throw new IllegalArgumentException("Role name cannot be empty");
		}
	}
}
