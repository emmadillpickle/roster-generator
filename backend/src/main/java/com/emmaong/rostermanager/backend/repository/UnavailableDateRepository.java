package com.emmaong.rostermanager.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UnavailableDateRepository {
	private final JdbcTemplate jdbcTemplate;
	
	public UnavailableDateRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public LocalDate save(long personId, LocalDate date) {
		jdbcTemplate.update(
				"INSERT OR IGNORE INTO unavailable_date (person_id, date) VALUES (?, ?)",
				personId,
				date.toString()
		);
		
		return date;
	}
	
	public List<LocalDate> findByPersonId(long personId) {
		return jdbcTemplate.query(
				"SELECT * FROM unavailable_date WHERE person_id = ?",
				(rs, rowNum) -> LocalDate.parse(rs.getString("date")),
				personId
		);
	}
	
	public void deleteByPersonId(long personId) {
		jdbcTemplate.update(
				"DELETE FROM unavailable_date WHERE person_id = ?", 
				personId
		);
	}
	
	public void delete(long personId, LocalDate date) {
		jdbcTemplate.update(
				"DELETE FROM unavailable_date WHERE person_id = ? AND date = ?",
				personId,
				date.toString()
		);
	}
}
