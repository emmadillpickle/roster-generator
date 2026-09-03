package com.emmaong.rostermanager.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.emmaong.rostermanager.models.Assignment;
import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.PairedAssignment;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.Role;
import com.emmaong.rostermanager.models.RoleCount;
import com.emmaong.rostermanager.models.Roster;
import com.emmaong.rostermanager.models.RosterEntry;
import com.emmaong.rostermanager.models.SoloAssignment;
import com.emmaong.rostermanager.models.SoloRole;

class RosterEngineTest {
	
	@Test
	void shouldAssignPersonToSoloRole() {
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
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .people(List.of(emma))
	            .roles(List.of(worship))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	    SoloAssignment assignment = entry.getSoloAssignments().get(0);
	
	    assertEquals(event, entry.getEvent());
	    assertEquals(1, entry.getSoloAssignments().size());
	    assertEquals(emma, assignment.getPerson());
	    assertEquals(worship, assignment.getRole());
	}
	
	@Test
	void shouldAssignMultiplePeopleToSameRole() {
	    SoloRole worship = SoloRole.builder()
	            .id(1L)
	            .name("Worship")
	            .build();
	
	    PersonRole emmaRole = PersonRole.builder()
	            .role(worship)
	            .maxShifts(5)
	            .build();
	
	    PersonRole johnRole = PersonRole.builder()
	            .role(worship)
	            .maxShifts(5)
	            .build();
	
	    Person emma = Person.builder()
	            .id(1L)
	            .name("Emma")
	            .roles(Set.of(emmaRole))
	            .build();
	
	    Person john = Person.builder()
	            .id(2L)
	            .name("John")
	            .roles(Set.of(johnRole))
	            .build();
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worship)
	            .requiredCount(2)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .people(List.of(emma, john))
	            .roles(List.of(worship))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	
	    assertEquals(2, entry.getSoloAssignments().size());
	
	    List<Person> assignedPeople = entry.getSoloAssignments()
	            .stream()
	            .map(SoloAssignment::getPerson)
	            .toList();
	
	    assertTrue(assignedPeople.contains(emma));
	    assertTrue(assignedPeople.contains(john));
	}
	
	@Test
	void shouldFlagUnfillableSoloRole() {
	    SoloRole worship = SoloRole.builder()
	            .id(1L)
	            .name("Worship")
	            .build();
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .roles(List.of(worship))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	
	    assertEquals(1, entry.getSoloAssignments().size());
	    
	    SoloAssignment assignment = entry.getSoloAssignments().get(0);
	    assertEquals(null, assignment.getPerson());
	    assertEquals(worship, assignment.getRole());
	}
	
	@Test
	void shouldNotAssignSamePersonTwiceToSameEvent() {
	    SoloRole worship = SoloRole.builder()
	            .id(1L)
	            .name("Worship")
	            .build();
	
	    SoloRole welcome = SoloRole.builder()
	            .id(2L)
	            .name("Welcome")
	            .build();
	
	    PersonRole emmaWorshipRole = PersonRole.builder()
	            .role(worship)
	            .maxShifts(5)
	            .build();
	
	    PersonRole emmaWelcomeRole = PersonRole.builder()
	            .role(welcome)
	            .maxShifts(5)
	            .build();
	
	    PersonRole johnWelcomeRole = PersonRole.builder()
	            .role(welcome)
	            .maxShifts(5)
	            .build();
	
	    Person emma = Person.builder()
	            .id(1L)
	            .name("Emma")
	            .roles(Set.of(emmaWorshipRole, emmaWelcomeRole))
	            .build();
	
	    Person john = Person.builder()
	            .id(2L)
	            .name("John")
	            .roles(Set.of(johnWelcomeRole))
	            .build();
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    RoleCount welcomeCount = RoleCount.builder()
	            .role(welcome)
	            .requiredCount(1)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount, welcomeCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .people(List.of(emma, john))
	            .roles(List.of(worship, welcome))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	
	    assertEquals(2, entry.getSoloAssignments().size());
	
	    List<Person> assignedPeople = entry.getSoloAssignments()
	            .stream()
	            .map(SoloAssignment::getPerson)
	            .toList();
	
	    assertEquals(1, assignedPeople.stream()
	            .filter(person -> person.equals(emma))
	            .count());
	
	    assertEquals(1, assignedPeople.stream()
	            .filter(person -> person.equals(john))
	            .count());
	}
	
	@Test
	void shouldPrioritizeRoleWithLeastCandidates() {
	    SoloRole worship = SoloRole.builder()
	            .id(1L)
	            .name("Worship")
	            .build();
	
	    SoloRole welcome = SoloRole.builder()
	            .id(2L)
	            .name("Welcome")
	            .build();
	
	    PersonRole emmaWorshipRole = PersonRole.builder()
	            .role(worship)
	            .maxShifts(5)
	            .build();
	
	    PersonRole emmaWelcomeRole = PersonRole.builder()
	            .role(welcome)
	            .maxShifts(5)
	            .build();
	
	    PersonRole johnWelcomeRole = PersonRole.builder()
	            .role(welcome)
	            .maxShifts(5)
	            .build();
	
	    Person emma = Person.builder()
	            .id(1L)
	            .name("Emma")
	            .roles(Set.of(emmaWorshipRole, emmaWelcomeRole))
	            .build();
	
	    Person john = Person.builder()
	            .id(2L)
	            .name("John")
	            .roles(Set.of(johnWelcomeRole))
	            .build();
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    RoleCount welcomeCount = RoleCount.builder()
	            .role(welcome)
	            .requiredCount(1)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount, welcomeCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .people(List.of(emma, john))
	            .roles(List.of(worship, welcome))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	
	    SoloAssignment worshipAssignment = entry.getSoloAssignments()
	            .stream()
	            .filter(assignment ->
	                    assignment.getRole().equals(worship))
	            .findFirst()
	            .orElseThrow();
	
	    SoloAssignment welcomeAssignment = entry.getSoloAssignments()
	            .stream()
	            .filter(assignment ->
	                    assignment.getRole().equals(welcome))
	            .findFirst()
	            .orElseThrow();
	
	    assertEquals(emma, worshipAssignment.getPerson());
	    assertEquals(john, welcomeAssignment.getPerson());
	}
	
	@Test
	void shouldAssignPairingToPairedRole() {
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
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worshipTeam)
	            .requiredCount(1)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .pairings(List.of(pairing))
	            .roles(List.of(worshipTeam))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	    PairedAssignment assignment =
	            entry.getPairedAssignments().get(0);
	
	    assertEquals(1, entry.getPairedAssignments().size());
	    assertEquals(pairing, assignment.getPairing());
	    assertEquals(worshipTeam, assignment.getRole());
	}
	
	@Test
	void shouldAssignMultiplePairingsToSamePairedRole() {
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
	            .shiftsWorked(0)
	            .build();
	
	    Pairing pairing2 = Pairing.builder()
	            .id(2L)
	            .role(worshipTeam)
	            .people(Set.of(charlie, diana))
	            .maxShifts(5)
	            .shiftsWorked(0)
	            .build();
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worshipTeam)
	            .requiredCount(2)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .pairings(List.of(pairing1, pairing2))
	            .roles(List.of(worshipTeam))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	
	    assertEquals(2, entry.getPairedAssignments().size());
	
	    List<Pairing> assignedPairings = entry.getPairedAssignments()
	            .stream()
	            .map(PairedAssignment::getPairing)
	            .toList();
	
	    assertTrue(assignedPairings.contains(pairing1));
	    assertTrue(assignedPairings.contains(pairing2));
	}
	
	@Test
	void shouldFlagUnfillablePairedRole() {
	    PairedRole worshipTeam = PairedRole.builder()
	            .id(1L)
	            .name("Worship Team")
	            .build();
	
	    RoleCount worshipCount = RoleCount.builder()
	            .role(worshipTeam)
	            .requiredCount(1)
	            .build();
	
	    Event event = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .roles(List.of(worshipTeam))
	            .events(List.of(event))
	            .build()
	            .generateRoster();
	
	    RosterEntry entry = roster.getRosterEntries().get(0);
	
	    assertEquals(1, entry.getPairedAssignments().size());
	    
	    PairedAssignment assignment = entry.getPairedAssignments().get(0);
	    
	    assertEquals(null, assignment.getPairing());
	    assertEquals(worshipTeam, assignment.getRole());
	}
	
	@Test
	void shouldRespectMaximumShiftCount() {
	    SoloRole worship = SoloRole.builder()
	            .id(1L)
	            .name("Worship")
	            .build();
	
	    PersonRole emmaRole = PersonRole.builder()
	            .role(worship)
	            .maxShifts(1)
	            .build();
	
	    PersonRole johnRole = PersonRole.builder()
	            .role(worship)
	            .maxShifts(5)
	            .build();
	
	    Person emma = Person.builder()
	            .id(1L)
	            .name("Emma")
	            .roles(Set.of(emmaRole))
	            .build();
	
	    Person john = Person.builder()
	            .id(2L)
	            .name("John")
	            .roles(Set.of(johnRole))
	            .build();
	
	    RoleCount worshipCount1 = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    RoleCount worshipCount2 = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    Event firstEvent = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(worshipCount1))
	            .build();
	
	    Event secondEvent = Event.builder()
	            .id(2L)
	            .date(LocalDate.of(2026, 1, 8))
	            .roles(List.of(worshipCount2))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .people(List.of(emma, john))
	            .roles(List.of(worship))
	            .events(List.of(firstEvent, secondEvent))
	            .build()
	            .generateRoster();
	
	    List<SoloAssignment> assignments = roster.getRosterEntries()
	            .stream()
	            .flatMap(entry -> entry.getSoloAssignments().stream())
	            .toList();
	
	    long emmaAssignments = assignments.stream()
	            .filter(assignment ->
	                    assignment.getPerson().equals(emma))
	            .count();
	
	    assertEquals(1, emmaAssignments);
	    assertEquals(2, assignments.size());
	}
	
	@Test
	void shouldGenerateRosterEntryForEachEvent() {
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
	
	    RoleCount firstWorshipCount = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    RoleCount secondWorshipCount = RoleCount.builder()
	            .role(worship)
	            .requiredCount(1)
	            .build();
	
	    Event firstEvent = Event.builder()
	            .id(1L)
	            .date(LocalDate.of(2026, 1, 1))
	            .roles(List.of(firstWorshipCount))
	            .build();
	
	    Event secondEvent = Event.builder()
	            .id(2L)
	            .date(LocalDate.of(2026, 1, 8))
	            .roles(List.of(secondWorshipCount))
	            .build();
	
	    Roster roster = RosterEngine.builder()
	            .people(List.of(emma))
	            .roles(List.of(worship))
	            .events(List.of(firstEvent, secondEvent))
	            .build()
	            .generateRoster();
	
	    assertEquals(2, roster.getRosterEntries().size());
	
	    assertEquals(
	            firstEvent,
	            roster.getRosterEntries().get(0).getEvent()
	    );
	
	    assertEquals(
	            secondEvent,
	            roster.getRosterEntries().get(1).getEvent()
	    );
	}

}
