package com.emmaong.rostermanager.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.emmaong.rostermanager.models.Event;

@Repository
public class EventRepository {
	private final JdbcTemplate jdbcTemplate;
	
	public EventRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final RowMapper<Event> mapper = (rs, rowNum) ->
		Event.builder()
			.id(rs.getLong("id"))
			.date(LocalDate.parse(rs.getString("date")))
			.build();
	
	public Event save(Event event) {
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
					.build();
			
		}

		jdbcTemplate.update(					
				"UPDATE event SET date = ?, WHERE id = ?",
				event.getDate().toString(),
				event.getId()
		);
		
		return event;
	
	}
	
	public Optional<Event> findById(long id) {
		List<Event> events = jdbcTemplate.query(
				"SELECT * FROM event WHERE id = ?",
				mapper,
				id
		);
		
		return events.stream().findFirst();
	}
	
	public List<Event> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM events", 
				mapper
		);
	}
	
	public void deleteById(long id) {
		jdbcTemplate.update(
				"DELETE FROM event WHERE id = ?", 
				id
		);
	}
}
