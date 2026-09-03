package com.emmaong.rostermanager.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

class PersonTest {

    // -------------------------------------------------------------------------
    // isAvailableOn()
    // -------------------------------------------------------------------------

    @Test
    void isAvailableOn_returnsTrueWhenPersonHasNoUnavailability() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person person = Person.builder()
                .unavailability(Set.of())
                .build();

        assertTrue(person.isAvailableOn(date));
    }

    @Test
    void isAvailableOn_returnsFalseWhenPersonIsUnavailableOnDate() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person person = Person.builder()
                .unavailability(Set.of(date))
                .build();

        assertFalse(person.isAvailableOn(date));
    }

    @Test
    void isAvailableOn_returnsTrueWhenPersonIsUnavailableOnDifferentDate() {
        LocalDate unavailableDate = LocalDate.of(2026, 8, 15);
        LocalDate requestedDate = LocalDate.of(2026, 8, 16);

        Person person = Person.builder()
                .unavailability(Set.of(unavailableDate))
                .build();

        assertTrue(person.isAvailableOn(requestedDate));
    }


    // -------------------------------------------------------------------------
    // isNotOnCooldown()
    // -------------------------------------------------------------------------

    @Test
    void isNotOnCooldown_returnsTrueWhenPersonHasNeverServed() {
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person person = Person.builder()
                .lastServed(null)
                .cooldown(1)
                .build();

        assertTrue(person.isNotOnCooldown(date));
    }

    @Test
    void isNotOnCooldown_returnsFalseWhenDateIsBeforeCooldownEnds() {
        LocalDate lastServed = LocalDate.of(2026, 8, 1);
        LocalDate date = LocalDate.of(2026, 8, 14);

        Person person = Person.builder()
                .lastServed(lastServed)
                .cooldown(1)
                .build();

        assertFalse(person.isNotOnCooldown(date));
    }

    @Test
    void isNotOnCooldown_returnsTrueWhenDateIsExactlyWhenCooldownEnds() {
        LocalDate lastServed = LocalDate.of(2026, 8, 1);
        LocalDate date = LocalDate.of(2026, 8, 15);

        Person person = Person.builder()
                .lastServed(lastServed)
                .cooldown(1)
                .build();

        assertTrue(person.isNotOnCooldown(date));
    }

    @Test
    void isNotOnCooldown_returnsTrueWhenDateIsAfterCooldownEnds() {
        LocalDate lastServed = LocalDate.of(2026, 8, 1);
        LocalDate date = LocalDate.of(2026, 8, 16);

        Person person = Person.builder()
                .lastServed(lastServed)
                .cooldown(1)
                .build();

        assertTrue(person.isNotOnCooldown(date));
    }


    // -------------------------------------------------------------------------
    // hasRemainingShiftsFor()
    // -------------------------------------------------------------------------

    @Test
    void hasRemainingShiftsFor_returnsTrueWhenPersonHasRemainingShifts() {
        SoloRole role = SoloRole.builder()
                .name("Worship")
                .build();

        PersonRole personRole = PersonRole.builder()
                .role(role)
                .shiftsWorked(1)
                .maxShifts(3)
                .build();

        Person person = Person.builder()
                .roles(Set.of(personRole))
                .build();

        assertTrue(person.hasRemainingShiftsFor(role));
    }

    @Test
    void hasRemainingShiftsFor_returnsFalseWhenPersonHasReachedMaxShifts() {
        SoloRole role = SoloRole.builder()
                .name("Worship")
                .build();

        PersonRole personRole = PersonRole.builder()
                .role(role)
                .shiftsWorked(3)
                .maxShifts(3)
                .build();

        Person person = Person.builder()
                .roles(Set.of(personRole))
                .build();

        assertFalse(person.hasRemainingShiftsFor(role));
    }

    @Test
    void hasRemainingShiftsFor_returnsFalseWhenPersonHasExceededMaxShifts() {
        SoloRole role = SoloRole.builder()
                .name("Worship")
                .build();

        PersonRole personRole = PersonRole.builder()
                .role(role)
                .shiftsWorked(4)
                .maxShifts(3)
                .build();

        Person person = Person.builder()
                .roles(Set.of(personRole))
                .build();

        assertFalse(person.hasRemainingShiftsFor(role));
    }

    @Test
    void hasRemainingShiftsFor_returnsFalseWhenPersonDoesNotHaveRole() {
        SoloRole worshipRole = SoloRole.builder()
                .name("Worship")
                .build();

        SoloRole welcomeRole = SoloRole.builder()
                .name("Welcome")
                .build();

        PersonRole personRole = PersonRole.builder()
                .role(worshipRole)
                .shiftsWorked(1)
                .maxShifts(3)
                .build();

        Person person = Person.builder()
                .roles(Set.of(personRole))
                .build();

        assertFalse(person.hasRemainingShiftsFor(welcomeRole));
    }
    
	 // -------------------------------------------------------------------------
	 // updateCounters()
	 // -------------------------------------------------------------------------
	
	 @Test
	 void updateCounters_updatesLastServedDate() {
	     LocalDate date = LocalDate.of(2026, 8, 15);
	
	     SoloRole role = SoloRole.builder()
	             .name("Worship")
	             .build();
	
	     PersonRole personRole = PersonRole.builder()
	             .role(role)
	             .shiftsWorked(1)
	             .maxShifts(3)
	             .build();
	
	     Person person = Person.builder()
	             .name("Emma")
	             .roles(Set.of(personRole))
	             .build();
	
	     person.updateCounters(role, date);
	
	     assertEquals(date, person.getLastServed());
	 }
	
	 @Test
	 void updateCounters_incrementsShiftsWorked() {
	     LocalDate date = LocalDate.of(2026, 8, 15);
	
	     SoloRole role = SoloRole.builder()
	             .name("Worship")
	             .build();
	
	     PersonRole personRole = PersonRole.builder()
	             .role(role)
	             .shiftsWorked(1)
	             .maxShifts(3)
	             .build();
	
	     Person person = Person.builder()
	             .name("Emma")
	             .roles(Set.of(personRole))
	             .build();
	
	     person.updateCounters(role, date);
	
	     assertEquals(2, personRole.getShiftsWorked());
	 }
	
	 @Test
	 void updateCounters_incrementsShiftsWorkedFromZero() {
	     LocalDate date = LocalDate.of(2026, 8, 15);
	
	     SoloRole role = SoloRole.builder()
	             .name("Worship")
	             .build();
	
	     PersonRole personRole = PersonRole.builder()
	             .role(role)
	             .shiftsWorked(0)
	             .maxShifts(3)
	             .build();
	
	     Person person = Person.builder()
	             .name("Emma")
	             .roles(Set.of(personRole))
	             .build();
	
	     person.updateCounters(role, date);
	
	     assertEquals(1, personRole.getShiftsWorked());
	 }
	
	 @Test
	 void updateCounters_throwsExceptionWhenPersonDoesNotHaveRole() {
	     LocalDate date = LocalDate.of(2026, 8, 15);
	
	     SoloRole worshipRole = SoloRole.builder()
	             .name("Worship")
	             .build();
	
	     SoloRole welcomeRole = SoloRole.builder()
	             .name("Welcome")
	             .build();
	
	     PersonRole personRole = PersonRole.builder()
	             .role(worshipRole)
	             .shiftsWorked(1)
	             .maxShifts(3)
	             .build();
	
	     Person person = Person.builder()
	             .name("Emma")
	             .roles(Set.of(personRole))
	             .build();
	
	     IllegalStateException exception = assertThrows(
	             IllegalStateException.class,
	             () -> person.updateCounters(welcomeRole, date)
	     );
	
	     assertEquals(
	             "Trying to update Emma's counter for Welcome, but they don't do this role!",
	             exception.getMessage()
	     );
	 }
}