package com.emmaong.rostermanager.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.emmaong.rostermanager.models.Person;

@Repository
public class PersonRepository {
	private final JdbcTemplate jdbcTemplate;
	
	public PersonRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final RowMapper<Person> mapper = (rs, rowNum) ->
		Person.builder()
			.id(rs.getLong("id"))
			.name(rs.getString("name"))
			.cooldown(rs.getInt("cooldown"))
			.build();
		
	public Person save(Person person) {
		if (person.getId() == 0L) {
			
			jdbcTemplate.update(
					"INSERT INTO person (name, cooldown) VALUES (?, ?)",
					person.getName(),
					person.getCooldown()
			);
			
			long id = jdbcTemplate.queryForObject(
					"SELECT last_insert_rowid()", 
					Long.class
			);
			
			return Person.builder()
					.id(id)
					.name(person.getName())
					.cooldown(person.getCooldown())
					.build();
			
		}

		jdbcTemplate.update(					
				"UPDATE person SET name = ?, cooldown = ?, WHERE id = ?",
				person.getName(),
				person.getCooldown(),
				person.getId()
		);
		
		return person;
	
	}
	
	public Optional<Person> findById(long id) {
		List<Person> people = jdbcTemplate.query(
				"SELECT * FROM person WHERE id = ?",
				mapper,
				id
		);
		
		return people.stream().findFirst();
	}
	
	public List<Person> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM person", 
				mapper
		);
	}
	
	public void deleteById(long id) {
		jdbcTemplate.update(
				"DELETE FROM person WHERE id = ?", 
				id
		);
	}
}
