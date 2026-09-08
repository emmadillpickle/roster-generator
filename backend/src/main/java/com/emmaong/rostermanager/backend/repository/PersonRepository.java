package com.emmaong.rostermanager.backend.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.SoloRole;

@Repository
public class PersonRepository {
	private final JdbcTemplate jdbcTemplate;
	
	public PersonRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final RowMapper<Person> personMapper = (rs, rowNum) ->
		Person.builder()
			.id(rs.getLong("id"))
			.name(rs.getString("name"))
			.cooldown(rs.getInt("cooldown"))
			.build();
		
	private final RowMapper<LocalDate> unavailableDateMapper = (rs, rowNum) ->
		LocalDate.parse(rs.getString("date"));
		
	private final RowMapper<PersonRole> personRoleMapper = (rs, rowNum) -> {

	    SoloRole role = new SoloRole.Builder()
	            .id(rs.getLong("id"))
	            .name(rs.getString("name"))
	            .build();

	    return PersonRole.builder()
	            .role(role)
	            .maxShifts(rs.getInt("max_shifts"))
	            .build();
	};
		
		
	public Person save(Person person) {
		Person savedPerson = saveToPersonTable(person);
		savedPerson = saveToUnavailableDateTable(savedPerson);
		savedPerson = saveToPersonRoleTable(savedPerson);
	
		return savedPerson;
	}
	
	public Optional<Person> findById(long id) {
	    return findPersonById(id)
	            .map(person -> {
	                person.setUnavailability(findUnavailableDateById(id));
	                person.setRoles(findPersonRoleById(id));
	                return person;
	            });
	}
	
	public List<Person> findAll() {
	    List<Person> people = jdbcTemplate.query(
	            "SELECT * FROM person",
	            personMapper
	    );

	    Map<Long, Set<LocalDate>> unavailableDates = findAllUnavailableDates();
	    Map<Long, Set<PersonRole>> personRoles = findAllPersonRoles();

	    for (Person person : people) {
	        person.setUnavailability(
	                unavailableDates.getOrDefault(person.getId(), Set.of())
	        );

	        person.setRoles(
	                personRoles.getOrDefault(person.getId(), Set.of())
	        );
	    }

	    return people;
	}
	
	public void deleteById(long id) {
		jdbcTemplate.update(
				"DELETE FROM person WHERE id = ?", 
				id
		);
	}
	
	private Person saveToPersonTable(Person person) {
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
					.roles(person.getRoles())
					.unavailability(person.getUnavailability())
					.build();
			
		}

		jdbcTemplate.update(					
				"UPDATE person SET name = ?, cooldown = ? WHERE id = ?",
				person.getName(),
				person.getCooldown(),
				person.getId()
		);
		
		return person;
	}
	
	private Person saveToUnavailableDateTable(Person person) {
		System.out.println("here1");
		Set<LocalDate> dates = person.getUnavailability();
		
		if (!dates.isEmpty()) {
			System.out.println("here2");
			jdbcTemplate.batchUpdate(
			    "INSERT OR IGNORE INTO unavailable_date (person_id, date) VALUES (?, ?)",
			    dates,
			    dates.size(),
			    (ps, date) -> {
			        ps.setLong(1, person.getId());
			        ps.setString(2, date.toString());
			    }
			);
		}
		
		System.out.println("here3");
		return person;
	}
	
	private Person saveToPersonRoleTable(Person person) {
		Set<PersonRole> personRoles = person.getRoles();
		
		for (PersonRole personRole : personRoles) {
			if (personRole.getRole() instanceof SoloRole) {
				jdbcTemplate.update(
						"INSERT OR IGNORE INTO person_role (person_id, role_id, max_shifts) VALUES (?, ?, ?)",
						person.getId(),
						personRole.getRole().getId(),
						personRole.getMaxShifts()
				);
			}
		}
		
		return person;
	}
	
	private Optional<Person> findPersonById(long id) {
		List<Person> people = jdbcTemplate.query(
				"SELECT * FROM person WHERE id = ?",
				personMapper,
				id
		);
		
		return people.stream().findFirst();
	}
	
	private Set<LocalDate> findUnavailableDateById(long id) {
		return new HashSet<>( 
				jdbcTemplate.query(
					"SELECT * FROM unavailable_date WHERE person_id = ?",
					(rs, rowNum) -> LocalDate.parse(rs.getString("date")),
					id
		));
	}
	
	private Set<PersonRole> findPersonRoleById(long id) {
		return new HashSet<>(
				jdbcTemplate.query(
					    """
					    SELECT
					        pr.max_shifts,
					        r.id,
					        r.name
					    FROM person_role pr
					    JOIN role r
					        ON pr.role_id = r.id
					    WHERE pr.person_id = ?
					    """,
					    personRoleMapper,
					    id
		));
	}
	
	private Map<Long, Set<LocalDate>> findAllUnavailableDates() {
	    return jdbcTemplate.query(
	            "SELECT person_id, date FROM unavailable_date",
	            rs -> {
	                Map<Long, Set<LocalDate>> result = new HashMap<>();

	                while (rs.next()) {
	                    long personId = rs.getLong("person_id");
	                    LocalDate date = unavailableDateMapper.mapRow(rs, rs.getRow());

	                    result
	                        .computeIfAbsent(personId, key -> new HashSet<>())
	                        .add(date);
	                }

	                return result;
	            }
	    );
	}
	
	private Map<Long, Set<PersonRole>> findAllPersonRoles() {
	    return jdbcTemplate.query(
	            """
	            SELECT
	                pr.person_id,
	                pr.max_shifts,
	                r.id,
	                r.name
	            FROM person_role pr
	            JOIN role r
	                ON pr.role_id = r.id
	            """,
	            rs -> {
	                Map<Long, Set<PersonRole>> result = new HashMap<>();

	                while (rs.next()) {
	                    long personId = rs.getLong("person_id");
	                    PersonRole personRole = personRoleMapper.mapRow(rs, rs.getRow());

	                    result
	                        .computeIfAbsent(personId, key -> new HashSet<>())
	                        .add(personRole);
	                }

	                return result;
	            }
	    );
	}
}
