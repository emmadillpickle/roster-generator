package com.emmaong.rostermanager.backend.repository;

import java.time.LocalDate;
import java.util.Collections;
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

import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;

@Repository
public class PairingRepository {
	private final JdbcTemplate jdbcTemplate;
	private final PersonRepository personRepository;
	
	public PairingRepository(JdbcTemplate jdbcTemplate, PersonRepository personRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.personRepository = personRepository;
	}
	
	private final RowMapper<Pairing> pairingMapper = (rs, rowNum) -> {
	    PairedRole role = new PairedRole.Builder()
	            .id(rs.getLong("role_id"))
	            .name(rs.getString("role_name"))
	            .build();

	    return Pairing.builder()
	            .id(rs.getLong("id"))
	            .role(role)
	            .maxShifts(rs.getInt("max_shifts"))
	            .build();
	};
	
	public Pairing save(Pairing pairing) {
		Pairing savedPairing = savePairing(pairing);
		savedPairing = savePairingMember(savedPairing);
		
		return savedPairing;
	}
	
	public Pairing findById(long pairingId) {

	    String pairingSql = """
	        SELECT
	            p.id,
	            p.max_shifts,
	            r.id AS role_id,
	            r.name AS role_name
	        FROM pairing p
	        JOIN role r
	            ON p.role_id = r.id
	        WHERE p.id = ?
	        """;

	    List<Pairing> pairings = jdbcTemplate.query(
	            pairingSql,
	            pairingMapper,
	            pairingId
	    );
	    
	    if (pairings.isEmpty()) {	
	        return null;
	    }
	    
	    Pairing pairing = pairings.stream().findFirst().get();

	    String memberSql = """
	        SELECT person_id
	        FROM pairing_member
	        WHERE pairing_id = ?
	        """;

	    List<Long> personIds = jdbcTemplate.query(
	            memberSql,
	            (rs, rowNum) -> rs.getLong("person_id"),
	            pairingId
	    );

	    Map<Long, Person> peopleById =
	            personRepository.findByIds(personIds);

	    pairing.setPeople(
	            personIds.stream()
	                    .map(peopleById::get)
	                    .collect(Collectors.toSet())
	    );

	    return pairing;
	}
	
	public List<Pairing> findAll() {
	
	    String pairingSql = """
	        SELECT
	            p.id,
	            p.max_shifts,
	            r.id AS role_id,
	            r.name AS role_name
	        FROM pairing p
	        JOIN role r
	            ON p.role_id = r.id
	        """;
	
	    List<Pairing> pairings = jdbcTemplate.query(
	            pairingSql,
	            pairingMapper
	    );
	
	    if (pairings.isEmpty()) {
	        return pairings;
	    }
	
	    String memberSql = """
	        SELECT pairing_id, person_id
	        FROM pairing_member
	        """;
	
	    Map<Long, Set<Long>> personIdsByPairing = jdbcTemplate.query(
	            memberSql,
	            rs -> {
	                Map<Long, Set<Long>> result = new HashMap<>();
	
	                while (rs.next()) {
	                    long pairingId = rs.getLong("pairing_id");
	                    long personId = rs.getLong("person_id");
	
	                    result.computeIfAbsent(
	                            pairingId,
	                            k -> new HashSet<>()
	                    ).add(personId);
	                }
	
	                return result;
	            }
	    );
	
	    Set<Long> personIds = personIdsByPairing.values().stream()
	            .flatMap(Set::stream)
	            .collect(Collectors.toSet());
	
	    Map<Long, Person> peopleById =
	            personRepository.findByIds(personIds);
	
	    for (Pairing pairing : pairings) {
	
	        Set<Long> memberIds =
	                personIdsByPairing.getOrDefault(
	                        pairing.getId(),
	                        Collections.emptySet()
	                );
	
	        Set<Person> people = new HashSet<>();

	        for (Long personId : memberIds) {
	            Person person = peopleById.get(personId);

	            if (person == null) {
	                throw new IllegalStateException(
	                        "Person " + personId
	                        + " referenced by pairing " + pairing.getId()
	                        + " does not exist"
	                );
	            }

	            people.add(person);
	        }

	        pairing.setPeople(people);
	
	        pairing.setPeople(people);
	    }
	
	    return pairings;
	}
	
	public void deleteById(long id) {
		jdbcTemplate.update(
				"DELETE FROM pairing WHERE id = ?", 
				id
		);
	}
	
	private Pairing savePairing(Pairing pairing) {
		if (pairing.getId() == 0L) {
			
			jdbcTemplate.update(
					"INSERT INTO pairing (role_id, max_shifts) VALUES (?, ?)",
					pairing.getRole().getId(),
					pairing.getMaxShifts()
			);
			
			long id = jdbcTemplate.queryForObject(
					"SELECT last_insert_rowid()", 
					Long.class
			);
			
			return Pairing.builder()
					.id(id)
					.people(pairing.getPeople())
					.role(pairing.getRole())
					.maxShifts(pairing.getMaxShifts())
					.shiftsWorked(pairing.getShiftsWorked())
					.build();
			
		}
		
		jdbcTemplate.update(
			"UPDATE pairing SET role_id = ?, max_shifts = ? WHERE id = ?",
			pairing.getRole().getId(),
			pairing.getMaxShifts(),
			pairing.getId()
		);
		
		return pairing;
	}
	
	private Pairing savePairingMember(Pairing pairing) {
		Set<Person> people = pairing.getPeople();
		
		if (!people.isEmpty()) {
			jdbcTemplate.batchUpdate(
			    "INSERT OR IGNORE INTO pairing_member (pairing_id, person_id) VALUES (?, ?)",
			    people,
			    people.size(),
			    (ps, person) -> {
			        ps.setLong(1, pairing.getId());
			        ps.setLong(2, person.getId());
			    }
			);
		}
		
		return pairing;
		
	}
}
