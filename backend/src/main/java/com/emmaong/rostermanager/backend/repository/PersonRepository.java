package com.emmaong.rostermanager.backend.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
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
	
	public Map<Long, Person> findByIds(Collection<Long> ids) {

	    if (ids.isEmpty()) {
	        return Collections.emptyMap();
	    }

	    String placeholders = ids.stream()
	            .map(id -> "?")
	            .collect(Collectors.joining(","));

	    Map<Long, Person> people = findPeopleByIds(ids, placeholders);
	    Map<Long, Set<LocalDate>> unavailableDates = findLocalDateByIds(ids, placeholders);
	    Map<Long, Set<PersonRole>> personRoles = findPersonRolesByIds(ids, placeholders);

	    for (Person person : people.values()) {

	        person.setUnavailability(
	                unavailableDates.getOrDefault(
	                        person.getId(),
	                        Collections.emptySet()
	                )
	        );

	        person.setRoles(
	                personRoles.getOrDefault(
	                        person.getId(),
	                        Collections.emptySet()
	                )
	        );
	    }

	    return people;
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
		Set<LocalDate> dates = person.getUnavailability();
		
		if (!dates.isEmpty()) {
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
	
	private Map<Long, Person> findPeopleByIds(Collection<Long> ids, String placeholders) {
		String sql = """
		    SELECT *
		    FROM person
		    WHERE id IN (%s)
		    """.formatted(placeholders);

		return jdbcTemplate.query(
		    sql,
		    personMapper,
		    ids.toArray()
		).stream()
		 .collect(Collectors.toMap(Person::getId, Function.identity()));
	}
	
	private Set<LocalDate> findUnavailableDateById(long id) {
		return new HashSet<>( 
				jdbcTemplate.query(
					"SELECT * FROM unavailable_date WHERE person_id = ?",
					(rs, rowNum) -> LocalDate.parse(rs.getString("date")),
					id
		));
	}
	
	private Map<Long, Set<LocalDate>> findLocalDateByIds(Collection<Long> ids, String placeholders) {
		String sql = """
		    SELECT *
		    FROM unavailable_date
		    WHERE person_id IN (%s)
		    """.formatted(placeholders);

		return jdbcTemplate.query(sql, rs -> {
		        Map<Long, Set<LocalDate>> result = new HashMap<>();

		        while (rs.next()) {
		            long personId = rs.getLong("person_id");

		            result.computeIfAbsent(personId, k -> new HashSet<>())
		                  .add(unavailableDateMapper.mapRow(rs, 0));
		        }

		        return result;
		    }, ids.toArray());
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
	
	private Map<Long, Set<PersonRole>> findPersonRolesByIds(Collection<Long> ids, String placeholders) {
		String roleSql = """
		    SELECT
		        pr.person_id,
		        pr.max_shifts,
		        r.id AS role_id,
		        r.name AS role_name
		    FROM person_role pr
		    JOIN role r
		        ON pr.role_id = r.id
		    WHERE pr.person_id IN (%s)
		    """.formatted(placeholders);
		
		return jdbcTemplate.query(roleSql, rs -> {
		        Map<Long, Set<PersonRole>> result = new HashMap<>();

		        while (rs.next()) {
		            long personId = rs.getLong("person_id");

		            result.computeIfAbsent(personId, k -> new HashSet<>())
		                  .add(personRoleMapper.mapRow(rs, 0));
		        }

		        return result;
		    }, ids.toArray());
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
