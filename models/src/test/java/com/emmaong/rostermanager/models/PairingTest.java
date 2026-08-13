package com.emmaong.rostermanager.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

class PairingTest {

    // -------------------------------------------------------------------------
    // isAvailableOn()
    // -------------------------------------------------------------------------

    @Test
    void isAvailableOn_returnsTrueWhenAllPeopleAreAvailable() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person emma = Person.builder()
                .name("Emma")
                .unavailability(Set.of())
                .build();

        Person john = Person.builder()
                .name("John")
                .unavailability(Set.of())
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertTrue(pairing.isAvailableOn(date));
    }

    @Test
    void isAvailableOn_returnsFalseWhenOnePersonIsUnavailable() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person emma = Person.builder()
                .name("Emma")
                .unavailability(Set.of(date))
                .build();

        Person john = Person.builder()
                .name("John")
                .unavailability(Set.of())
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertFalse(pairing.isAvailableOn(date));
    }

    @Test
    void isAvailableOn_returnsFalseWhenAllPeopleAreUnavailable() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person emma = Person.builder()
                .name("Emma")
                .unavailability(Set.of(date))
                .build();

        Person john = Person.builder()
                .name("John")
                .unavailability(Set.of(date))
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertFalse(pairing.isAvailableOn(date));
    }

    @Test
    void isAvailableOn_returnsTrueWhenPeopleAreUnavailableOnDifferentDates() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person emma = Person.builder()
                .name("Emma")
                .unavailability(Set.of(LocalDate.of(2026, 8, 16)))
                .build();

        Person john = Person.builder()
                .name("John")
                .unavailability(Set.of(LocalDate.of(2026, 8, 17)))
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertTrue(pairing.isAvailableOn(date));
    }


    // -------------------------------------------------------------------------
    // isNotOnCooldown()
    // -------------------------------------------------------------------------

    @Test
    void isNotOnCooldown_returnsTrueWhenAllPeopleAreNotOnCooldown() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person emma = Person.builder()
                .name("Emma")
                .lastServed(null)
                .cooldown(1)
                .build();

        Person john = Person.builder()
                .name("John")
                .lastServed(null)
                .cooldown(1)
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertTrue(pairing.isNotOnCooldown(date));
    }

    @Test
    void isNotOnCooldown_returnsFalseWhenOnePersonIsOnCooldown() {
        LocalDate date = LocalDate.of(2026, 8, 10);

        Person emma = Person.builder()
                .name("Emma")
                .lastServed(LocalDate.of(2026, 8, 1))
                .cooldown(1)
                .build();

        Person john = Person.builder()
                .name("John")
                .lastServed(null)
                .cooldown(1)
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertFalse(pairing.isNotOnCooldown(date));
    }

    @Test
    void isNotOnCooldown_returnsFalseWhenAllPeopleAreOnCooldown() {
        LocalDate date = LocalDate.of(2026, 8, 10);

        Person emma = Person.builder()
                .name("Emma")
                .lastServed(LocalDate.of(2026, 8, 1))
                .cooldown(1)
                .build();

        Person john = Person.builder()
                .name("John")
                .lastServed(LocalDate.of(2026, 8, 1))
                .cooldown(1)
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertFalse(pairing.isNotOnCooldown(date));
    }

    @Test
    void isNotOnCooldown_returnsTrueWhenAllPeopleArePastCooldown() {
        LocalDate date = LocalDate.of(2026, 8, 16);

        Person emma = Person.builder()
                .name("Emma")
                .lastServed(LocalDate.of(2026, 8, 1))
                .cooldown(1)
                .build();

        Person john = Person.builder()
                .name("John")
                .lastServed(LocalDate.of(2026, 8, 1))
                .cooldown(1)
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .build();

        assertTrue(pairing.isNotOnCooldown(date));
    }


    // -------------------------------------------------------------------------
    // hasRemainingShiftsFor()
    // -------------------------------------------------------------------------

    @Test
    void hasRemainingShiftsFor_returnsTrueWhenShiftsRemain() {
        PairedRole role = PairedRole.builder()
                .name("Worship")
                .build();

        Pairing pairing = Pairing.builder()
                .shiftsWorked(1)
                .maxShifts(3)
                .build();

        assertTrue(pairing.hasRemainingShiftsFor(role));
    }

    @Test
    void hasRemainingShiftsFor_returnsFalseWhenMaxShiftsReached() {
        PairedRole role = PairedRole.builder()
                .name("Worship")
                .build();

        Pairing pairing = Pairing.builder()
                .shiftsWorked(3)
                .maxShifts(3)
                .build();

        assertFalse(pairing.hasRemainingShiftsFor(role));
    }

    @Test
    void hasRemainingShiftsFor_returnsFalseWhenMaxShiftsExceeded() {
        PairedRole role = PairedRole.builder()
                .name("Worship")
                .build();

        Pairing pairing = Pairing.builder()
                .shiftsWorked(4)
                .maxShifts(3)
                .build();

        assertFalse(pairing.hasRemainingShiftsFor(role));
    }


    // -------------------------------------------------------------------------
    // updateCounters()
    // -------------------------------------------------------------------------

    @Test
    void updateCounters_incrementsShiftsWorked() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person emma = Person.builder()
                .name("Emma")
                .build();

        Person john = Person.builder()
                .name("John")
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .shiftsWorked(1)
                .maxShifts(3)
                .build();

        pairing.updateCounters(date);

        assertEquals(2, pairing.getShiftsWorked());
    }

    @Test
    void updateCounters_updatesLastServedForAllPeople() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person emma = Person.builder()
                .name("Emma")
                .build();

        Person john = Person.builder()
                .name("John")
                .build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(emma, john))
                .shiftsWorked(1)
                .maxShifts(3)
                .build();

        pairing.updateCounters(date);

        assertEquals(date, emma.getLastServed());
        assertEquals(date, john.getLastServed());
    }

    @Test
    void updateCounters_incrementsFromZero() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Pairing pairing = Pairing.builder()
                .people(Set.of())
                .shiftsWorked(0)
                .maxShifts(3)
                .build();

        pairing.updateCounters(date);

        assertEquals(1, pairing.getShiftsWorked());
    }

    @Test
    void updateCounters_doesNotChangeMaxShifts() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Pairing pairing = Pairing.builder()
                .people(Set.of())
                .shiftsWorked(1)
                .maxShifts(3)
                .build();

        pairing.updateCounters(date);

        assertEquals(3, pairing.getMaxShifts());
    }
}