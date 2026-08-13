package com.emmaong.rostermanager.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.SoloRole;

public class CandidateManagerTest {

	@Test
	void shouldInitializeSoloRoleCandidateList() {
	    SoloRole wallop = SoloRole.builder()
	    						.id(1L)
	    						.name("Wallop")
	    						.build();

	    CandidateManager manager = CandidateManager.builder()
	            .roles(List.of(wallop))
	            .build();

	    assertTrue(manager.getAllSoloCandidates().containsKey(wallop));
	    assertEquals(
	            0,
	            manager.getAllSoloCandidates().get(wallop).size());
	}
	
	@Test
	void shouldInitializePairedRoleCandidateList() {
	    PairedRole worshipTeam = PairedRole.builder()
	    							.id(1L)
	    							.name("Worship")
	    							.build();

	    CandidateManager manager = CandidateManager.builder()
	            .roles(List.of(worshipTeam))
	            .build();

	    assertTrue(manager.getAllPairedCandidates().containsKey(worshipTeam));
	    assertEquals(
	            0,
	            manager.getAllPairedCandidates().get(worshipTeam).size());
	}
	
	@Test
	void shouldAddPersonToSoloRoleCandidateList() {
		SoloRole wallop = SoloRole.builder()
							.id(1L)
							.name("Wallop")
							.build();

	    PersonRole personRole = PersonRole.builder()
	    							.role(wallop)
	    							.maxShifts(5)
	    							.build();
	    		
	    Person emma = Person.builder()
	    					.id(1L)
	    					.name("Emma")
	    					.roles(Set.of(personRole))
	    					.build();

	    CandidateManager manager = CandidateManager.builder()
	    		.people(List.of(emma))
	            .roles(List.of(wallop))
	            .build();

	    List<Person> candidates =
	    		manager.getAllSoloCandidates().get(wallop);

	    assertEquals(1, candidates.size());
	    assertTrue(candidates.contains(emma));
	}
	
	@Test
    void shouldAddMultiplePeopleToSameRole() {
        SoloRole worship = SoloRole.builder()
                .id(1L)
                .name("Worship")
                .build();

        PersonRole emmaWorshipRole = PersonRole.builder()
                .role(worship)
                .maxShifts(5)
                .build();

        PersonRole johnWorshipRole = PersonRole.builder()
                .role(worship)
                .maxShifts(5)
                .build();

        Person emma = Person.builder()
                .id(1L)
                .name("Emma")
                .roles(Set.of(emmaWorshipRole))
                .build();

        Person john = Person.builder()
                .id(2L)
                .name("John")
                .roles(Set.of(johnWorshipRole))
                .build();

        CandidateManager manager = CandidateManager.builder()
                .people(List.of(emma, john))
                .roles(List.of(worship))
                .build();

        List<Person> candidates =
                manager.getAllSoloCandidates().get(worship);

        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(emma));
        assertTrue(candidates.contains(john));
    }

    @Test
    void shouldAddPersonOnlyToAssignedRole() {
        SoloRole worship = SoloRole.builder()
                .id(1L)
                .name("Worship")
                .build();

        SoloRole welcome = SoloRole.builder()
                .id(2L)
                .name("Welcome")
                .build();

        PersonRole worshipRole = PersonRole.builder()
                .role(worship)
                .maxShifts(5)
                .build();

        Person emma = Person.builder()
                .id(1L)
                .name("Emma")
                .roles(Set.of(worshipRole))
                .build();

        CandidateManager manager = CandidateManager.builder()
                .people(List.of(emma))
                .roles(List.of(worship, welcome))
                .build();

        assertEquals(
                1,
                manager.getAllSoloCandidates().get(worship).size());

        assertEquals(
                0,
                manager.getAllSoloCandidates().get(welcome).size());
    }

    @Test
    void shouldAddPairingToPairedRoleCandidateList() {
        PairedRole worshipTeam = PairedRole.builder()
                .id(1L)
                .name("Worship Team")
                .build();

        Person alice = Person.builder()
                .id(1L)
                .name("Alice")
                .build();

        Person bob = Person.builder()
                .id(2L)
                .name("Bob")
                .build();

        Pairing pairing = Pairing.builder()
                .id(1L)
                .role(worshipTeam)
                .people(Set.of(alice, bob))
                .maxShifts(5)
                .shiftsWorked(0)
                .build();

        CandidateManager manager = CandidateManager.builder()
                .pairings(List.of(pairing))
                .roles(List.of(worshipTeam))
                .build();

        List<Pairing> candidates =
        		manager.getAllPairedCandidates().get(worshipTeam);

        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(pairing));
    }

    @Test
    void shouldAddMultiplePairingsToSamePairedRole() {
        PairedRole worshipTeam = PairedRole.builder()
                .id(1L)
                .name("Worship Team")
                .build();

        Person alice = Person.builder()
                .id(1L)
                .name("Alice")
                .build();

        Person bob = Person.builder()
                .id(2L)
                .name("Bob")
                .build();

        Person charlie = Person.builder()
                .id(3L)
                .name("Charlie")
                .build();

        Person diana = Person.builder()
                .id(4L)
                .name("Diana")
                .build();

        Pairing pairing1 = Pairing.builder()
                .id(1L)
                .role(worshipTeam)
                .people(Set.of(alice, bob))
                .maxShifts(5)
                .build();

        Pairing pairing2 = Pairing.builder()
                .id(2L)
                .role(worshipTeam)
                .people(Set.of(charlie, diana))
                .maxShifts(5)
                .build();

        CandidateManager manager = CandidateManager.builder()
                .pairings(List.of(pairing1, pairing2))
                .roles(List.of(worshipTeam))
                .build();

        List<Pairing> candidates =
        		manager.getAllPairedCandidates().get(worshipTeam);

        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(pairing1));
        assertTrue(candidates.contains(pairing2));
    }

    @Test
    void shouldThrowExceptionWhenPersonRoleNotRegistered() {
        SoloRole worship = SoloRole.builder()
                .id(1L)
                .name("Worship")
                .build();

        PersonRole worshipRole = PersonRole.builder()
                .role(worship)
                .maxShifts(5)
                .build();

        Person emma = Person.builder()
                .id(1L)
                .name("Emma")
                .roles(Set.of(worshipRole))
                .build();

        assertThrows(
                NullPointerException.class,
                () -> RosterEngine.builder()
                        .people(List.of(emma))
                        .build());
    }
	
}
