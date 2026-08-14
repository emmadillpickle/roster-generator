package com.emmaong.rostermanager.engine;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.PairedAssignment;
import com.emmaong.rostermanager.models.PairedRole;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.Roster;
import com.emmaong.rostermanager.models.RosterEntry;
import com.emmaong.rostermanager.models.SoloAssignment;
import com.emmaong.rostermanager.models.SoloRole;

class RosterBuilderTest {

    @Test
    void shouldCreateRosterEntryWhenAssigningSoloRoleToNewEvent() {
        Person person = Person.builder().name("Alice").build();
        SoloRole role = SoloRole.builder().name("welcome").build();
        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(person, role, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(event, entry.getEvent());
        assertEquals(1, entry.getSoloAssignments().size());

        SoloAssignment assignment = entry.getSoloAssignments().get(0);

        assertEquals(person, assignment.getPerson());
        assertEquals(role, assignment.getRole());
    }

    @Test
    void shouldAddMultipleSoloAssignmentsToSameEvent() {
        Person alice = Person.builder().name("Alice").build();
        Person bob = Person.builder().name("Bob").build();

        SoloRole role = SoloRole.builder().name("welcome").build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(alice, role, event);
        builder.assign(bob, role, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(2, entry.getSoloAssignments().size());
    }

    @Test
    void shouldCreateSeparateRosterEntriesForDifferentEvents() {
        Person person = Person.builder().name("Alice").build();

        SoloRole role = SoloRole.builder().name("welcome").build();

        Event event1 = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        Event event2 = Event.builder()
                .date(LocalDate.of(2026, 1, 8))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(person, role, event1);
        builder.assign(person, role, event2);

        Roster roster = builder.getRoster();

        assertEquals(2, roster.getRosterEntries().size());
    }

    @Test
    void shouldCreateRosterEntryWhenAssigningPairedRoleToNewEvent() {
        Person alice = Person.builder().name("Alice").build();
        Person bob = Person.builder().name("Bob").build();

        Pairing pairing = Pairing.builder()
			                .people(Set.of(alice, bob))
			                .build();

        PairedRole role = PairedRole.builder()
                .name("welcome Team")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(pairing, role, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(1, entry.getPairedAssignments().size());

        PairedAssignment assignment = entry.getPairedAssignments().get(0);

        assertEquals(pairing, assignment.getPairing());
        assertEquals(role, assignment.getRole());
    }

    @Test
    void shouldAddMultiplePairedAssignmentsToSameEvent() {
        Person alice = Person.builder().name("Alice").build();
        Person bob = Person.builder().name("Bob").build();
        Person charlie = Person.builder().name("Charlie").build();
        Person dave = Person.builder().name("Dave").build();

        Pairing pairing1 = Pairing.builder()
                .people(Set.of(alice, bob))
                .build();

        Pairing pairing2 = Pairing.builder()
                .people(Set.of(alice, bob))
                .build();

        PairedRole role = PairedRole.builder()
                .name("Welcome Team")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(pairing1, role, event);
        builder.assign(pairing2, role, event);

        RosterEntry entry = builder.getRoster()
                .getRosterEntries()
                .get(0);

        assertEquals(2, entry.getPairedAssignments().size());
    }

    @Test
    void shouldAllowSoloAndPairedAssignmentsOnSameEvent() {
        Person alice = Person.builder().name("Alice").build();
        Person bob = Person.builder().name("Bob").build();
        Person charlie = Person.builder().name("Charlie").build();

        SoloRole soloRole = SoloRole.builder()
                .name("Speaker")
                .build();

        PairedRole pairedRole = PairedRole.builder()
                .name("welcome Team")
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(bob, charlie))
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(alice, soloRole, event);
        builder.assign(pairing, pairedRole, event);

        RosterEntry entry = builder.getRoster()
                .getRosterEntries()
                .get(0);

        assertEquals(1, entry.getSoloAssignments().size());
        assertEquals(1, entry.getPairedAssignments().size());
    }

    @Test
    void getRosterShouldReturnBuiltRoster() {
        RosterBuilder builder = new RosterBuilder();

        Roster roster = builder.getRoster();

        assertNotNull(roster);
        assertNotNull(roster.getRosterEntries());
        assertTrue(roster.getRosterEntries().isEmpty());
    }
    
    @Test
    void shouldCreateUnfillableSoloAssignmentForNewEvent() {
        SoloRole role = SoloRole.builder()
                .name("Speaker")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.flagUnfillable(role, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(event, entry.getEvent());
        assertEquals(1, entry.getSoloAssignments().size());

        SoloAssignment assignment = entry.getSoloAssignments().get(0);

        assertNull(assignment.getPerson());
        assertEquals(role, assignment.getRole());
    }

    @Test
    void shouldCreateUnfillablePairedAssignmentForNewEvent() {
        PairedRole role = PairedRole.builder()
                .name("welcome Team")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.flagUnfillable(role, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(event, entry.getEvent());
        assertEquals(1, entry.getPairedAssignments().size());

        PairedAssignment assignment = entry.getPairedAssignments().get(0);

        assertNull(assignment.getPairing());
        assertEquals(role, assignment.getRole());
    }

    @Test
    void shouldReuseExistingRosterEntryWhenFlaggingUnfillableSoloRole() {
        Person person = Person.builder()
                .name("Alice")
                .build();

        SoloRole assignedRole = SoloRole.builder()
                .name("welcome")
                .build();

        SoloRole unfillableRole = SoloRole.builder()
                .name("Speaker")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(person, assignedRole, event);
        builder.flagUnfillable(unfillableRole, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(2, entry.getSoloAssignments().size());

        assertNotNull(entry.getSoloAssignments().get(0).getPerson());
        assertNull(entry.getSoloAssignments().get(1).getPerson());
    }

    @Test
    void shouldReuseExistingRosterEntryWhenFlaggingUnfillablePairedRole() {
        Person alice = Person.builder().name("Alice").build();
        Person bob = Person.builder().name("Bob").build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(alice, bob))
                .build();

        PairedRole assignedRole = PairedRole.builder()
                .name("welcome Team")
                .build();

        PairedRole unfillableRole = PairedRole.builder()
                .name("Backup Team")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.assign(pairing, assignedRole, event);
        builder.flagUnfillable(unfillableRole, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(2, entry.getPairedAssignments().size());

        assertNotNull(entry.getPairedAssignments().get(0).getPairing());
        assertNull(entry.getPairedAssignments().get(1).getPairing());
    }
    
    @Test
    void shouldAddMultipleUnfillableSoloAssignmentsToSameEvent() {
        SoloRole welcomeRole = SoloRole.builder()
                .name("welcome")
                .build();

        SoloRole speakerRole = SoloRole.builder()
                .name("Speaker")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.flagUnfillable(welcomeRole, event);
        builder.flagUnfillable(speakerRole, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(2, entry.getSoloAssignments().size());

        SoloAssignment first = entry.getSoloAssignments().get(0);
        SoloAssignment second = entry.getSoloAssignments().get(1);

        assertNull(first.getPerson());
        assertEquals(welcomeRole, first.getRole());

        assertNull(second.getPerson());
        assertEquals(speakerRole, second.getRole());
    }

    @Test
    void shouldAddMultipleUnfillablePairedAssignmentsToSameEvent() {
        PairedRole welcomeRole = PairedRole.builder()
                .name("welcome Team")
                .build();

        PairedRole prayerRole = PairedRole.builder()
                .name("Prayer Team")
                .build();

        Event event = Event.builder()
                .date(LocalDate.of(2026, 1, 1))
                .build();

        RosterBuilder builder = new RosterBuilder();

        builder.flagUnfillable(welcomeRole, event);
        builder.flagUnfillable(prayerRole, event);

        Roster roster = builder.getRoster();

        assertEquals(1, roster.getRosterEntries().size());

        RosterEntry entry = roster.getRosterEntries().get(0);

        assertEquals(2, entry.getPairedAssignments().size());

        PairedAssignment first = entry.getPairedAssignments().get(0);
        PairedAssignment second = entry.getPairedAssignments().get(1);

        assertNull(first.getPairing());
        assertEquals(welcomeRole, first.getRole());

        assertNull(second.getPairing());
        assertEquals(prayerRole, second.getRole());
    }
}