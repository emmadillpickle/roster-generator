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

import com.emmaong.rostermanager.backend.dto.request.RoleRequest;
import com.emmaong.rostermanager.backend.dto.response.RoleResponse;
import com.emmaong.rostermanager.backend.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
	private final RoleService roleService;
	
	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@GetMapping
	public List<RoleResponse> getAllRoles() {
		return roleService.getAll();
	}
	
	@GetMapping("/{id}")
	public RoleResponse getRole(@PathVariable long id) {
		return roleService.getById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RoleResponse createRole(@RequestBody RoleRequest request) {
		return roleService.save(request);
	}
	
	@PutMapping("/{id}")
	public RoleResponse updateRole(@PathVariable long id, @RequestBody RoleRequest request) {
		return roleService.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable long id) {
		roleService.deleteById(id);
	}	
}
