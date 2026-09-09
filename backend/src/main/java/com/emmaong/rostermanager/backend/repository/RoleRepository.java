package com.emmaong.rostermanager.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.SoloRole;
import com.emmaong.rostermanager.models.PairedRole;

@Repository
public class RoleRepository {
	private final JdbcTemplate jdbcTemplate;
	
	private final String SOLO_ROLE_TYPE = "SOLO";
	private final String PAIRED_ROLE_TYPE = "PAIRED";
	
	public RoleRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final RowMapper<Role> mapper = (rs, rowNum) -> {

	    String roleType = rs.getString("role_type");

	    if (SOLO_ROLE_TYPE.equals(roleType)) {
	        return SoloRole.builder()
	                .id(rs.getLong("id"))
	                .name(rs.getString("name"))
	                .build();
	    }

	    if (PAIRED_ROLE_TYPE.equals(roleType)) {
	        return PairedRole.builder()
	                .id(rs.getLong("id"))
	                .name(rs.getString("name"))
	                .build();
	    }

	    throw new IllegalStateException(
	            "Unknown role type: " + roleType
	    );
	};
	
	public Role save(Role role) {
		
		String roleType = role instanceof SoloRole ? SOLO_ROLE_TYPE : PAIRED_ROLE_TYPE;
		
		if (role.getId() == 0L) {
			
			jdbcTemplate.update(
					"INSERT INTO role (name, role_type) VALUES (?, ?)",
					role.getName(),
					roleType
			);
			
			long id = jdbcTemplate.queryForObject(
					"SELECT last_insert_rowid()", 
					Long.class
			);
			
			if (SOLO_ROLE_TYPE.equals(roleType)) {
				return SoloRole.builder()
						.id(id)
						.name(role.getName())
						.build();
			}
			
			return PairedRole.builder()
					.id(id)
					.name(role.getName())
					.build();
			
		}
		
		jdbcTemplate.update(					
			"UPDATE role SET name = ? WHERE id = ?",
			role.getName(),
			role.getId()
		);
		
		return role;
		
	}
	
	public Optional<Role> findById(long id) {
		List<Role> roles = jdbcTemplate.query(
				"SELECT * FROM role WHERE id = ?",
				mapper,
				id
		);
		
		return roles.stream().findFirst();
	}
	
	public List<Role> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM roles", 
				mapper
		);
	}
	
	public void deleteById(long id) {
		jdbcTemplate.update(
				"DELETE FROM role WHERE id = ?", 
				id
		);
	}
}
