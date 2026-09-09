package com.emmaong.rostermanager.backend.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.RoleCount;
import com.emmaong.rostermanager.models.SoloRole;

@Repository
public class EventRepository {
	private final JdbcTemplate jdbcTemplate;
	
	private final String SOLO_ROLE_TYPE = "SOLO";
	private final String PAIRED_ROLE_TYPE = "PAIRED";
	
	public EventRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final RowMapper<Event> eventMapper = (rs, rowNum) ->
		Event.builder()
			.id(rs.getLong("id"))
			.date(LocalDate.parse(rs.getString("date")))
			.build();
		
	private final RowMapper<RoleCount> roleCountMapper = (rs, rowNum) -> {
		String roleType = rs.getString("role_type");
		Role role;

	    if (SOLO_ROLE_TYPE.equals(roleType)) {
	         role = SoloRole.builder()
	                .id(rs.getLong("id"))
	                .name(rs.getString("name"))
	                .build();
	    }

	    else if (PAIRED_ROLE_TYPE.equals(roleType)) {
	        role = PairedRole.builder()
	                .id(rs.getLong("id"))
	                .name(rs.getString("name"))
	                .build();
	    }

	    else {
	    	throw new IllegalStateException("Unknown role type: " + roleType);
	    }
	    
	    return new RoleCount.Builder().role(role).requiredCount(rs.getInt("count")).build();
	};
	
	public Event save(Event event) {
		Event savedEvent = saveEvent(event);
		savedEvent = saveRoleCount(savedEvent);
		
		return savedEvent;
	}
	
	public Optional<Event> findById(long id) {
		return findEventById(id)
	            .map(event -> {
	                event.setRoles(findRolesById(id));
	                return event;
	            });
	}
	
	public List<Event> findAll() {
		List<Event> events = findAllEvents();
		Map<Long, List<RoleCount>> roleCounts = findAllRoleCounts();
		
		for (Event event : events) {
			event.setRoles(
					roleCounts.getOrDefault(event.getId(), List.of())
			);
		}
		
		return events;
	}
	
	public void deleteById(long id) {
		jdbcTemplate.update(
				"DELETE FROM event WHERE id = ?", 
				id
		);
	}
	
	private Event saveEvent(Event event) {
		if (event.getId() == 0L) {
			
			jdbcTemplate.update(
					"INSERT INTO event (date) VALUES (?)",
					event.getDate().toString()
			);
			
			long id = jdbcTemplate.queryForObject(
					"SELECT last_insert_rowid()", 
					Long.class
			);
			
			return Event.builder()
					.id(id)
					.date(event.getDate())
					.roles(event.getRoles())
					.build();
			
		}

		jdbcTemplate.update(					
				"UPDATE event SET date = ?, WHERE id = ?",
				event.getDate().toString(),
				event.getId()
		);
		
		return event;
	}
	
	private Event saveRoleCount(Event event) {
		List<RoleCount> roles = event.getRoles();
		
		if (!roles.isEmpty()) {
			for (RoleCount role : roles) {
				System.out.println(event.getId());
				
				jdbcTemplate.update(
						"INSERT OR IGNORE INTO role_count (event_id, role_id, count) VALUES (?, ?, ?)",
						event.getId(),
						role.getRole().getId(),
						role.getRequiredCount()
				);
			}
		}
		
		return event;
	}
	
	private Optional<Event> findEventById(long id) {
		List<Event> events = jdbcTemplate.query(
				"SELECT * FROM event WHERE id = ?",
				eventMapper,
				id
		);
		
		return events.stream().findFirst();
	}
	
	private List<RoleCount> findRolesById(long id) {
		return jdbcTemplate.query(
				"""
			    SELECT
			        rc.event_id,
			        rc.count,
			        r.id,
			        r.name,
			        r.role_type
			    FROM role_count rc
			    JOIN role r
			        ON rc.role_id = r.id
			    WHERE rc.event_id = ?
			    """,
				roleCountMapper,
				id
		);
	}
	
	private List<Event> findAllEvents() {
		return jdbcTemplate.query(
				"SELECT * FROM event", 
				eventMapper
		);
	}
	
	private Map<Long, List<RoleCount>> findAllRoleCounts() {
	    return jdbcTemplate.query(
	    		"""
			    SELECT
			        rc.event_id,
			        rc.count,
			        r.id,
			        r.name,
			        r.role_type
			    FROM role_count rc
			    JOIN role r
			        ON rc.role_id = r.id
			    """,
	            rs -> {
	                Map<Long, List<RoleCount>> result = new HashMap<>();

	                while (rs.next()) {
	                    long eventId = rs.getLong("event_id");
	                    RoleCount roleCount = roleCountMapper.mapRow(rs, rs.getRow());

	                    result
	                        .computeIfAbsent(eventId, key -> new ArrayList<>())
	                        .add(roleCount);
	                }

	                return result;
	            }
	    );
	}
}
