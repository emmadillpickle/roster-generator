package com.emmaong.rostermanager.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.SoloRole;

class SlackCalculatorTest {

    private final SlackCalculator calculator = new SlackCalculator();
    
    @Test
    void shouldReturnZeroWhenPersonDoesNotHaveRole() {
        SoloRole welcome = SoloRole.builder()
			                .id(1)
			                .build();

        Person person = Person.builder()
			                .roles(Set.of())
			                .build();

        Event event = Event.builder()
		                .date(LocalDate.of(2026, 1, 1))
		                .build();

        int slack = calculator.calculateSlack(
                person,
                welcome,
                List.of(event));

        assertEquals(0, slack);
    }

    @Test
    void shouldUseRemainingCapacityWhenCapacityIsSmaller() {
        SoloRole welcome = SoloRole.builder()
				                .id(1)
				                .build();

        PersonRole personRole = PersonRole.builder()
					                .role(welcome)
					                .maxShifts(3)
					                .shiftsWorked(2)
					                .build();

        Person person = Person.builder()
			                .roles(Set.of(personRole))
			                .build();

        List<Event> events = List.of(
                Event.builder().date(LocalDate.of(2026, 1, 1)).build(),
                Event.builder().date(LocalDate.of(2026, 1, 8)).build(),
                Event.builder().date(LocalDate.of(2026, 1, 15)).build()
        );

        int slack = calculator.calculateSlack(person, welcome, events);

        assertEquals(1, slack);
    }

    @Test
    void shouldUseEligibleFutureEventsWhenUnlimitedCapacity() {
        SoloRole welcome = SoloRole.builder()
			                .id(1)
			                .build();

        PersonRole personRole = PersonRole.builder()
				                .role(welcome)
				                .maxShifts(0)
				                .build();

        Person person = Person.builder()
			                .roles(Set.of(personRole))
			                .build();

        List<Event> events = List.of(
                Event.builder().date(LocalDate.of(2026, 1, 1)).build(),
                Event.builder().date(LocalDate.of(2026, 1, 8)).build()
        );

        int slack = calculator.calculateSlack(person, welcome, events);

        assertEquals(2, slack);
    }

    @Test
    void shouldReturnZeroWhenPersonHasReachedMaxShifts() {
        SoloRole worship = SoloRole.builder()
				                .id(1)
				                .build();

        PersonRole personRole = PersonRole.builder()
					                .role(worship)
					                .maxShifts(2)
					                .shiftsWorked(2)
					                .build();

        Person person = Person.builder()
			                .roles(Set.of(personRole))
			                .build();

        Event event = Event.builder()
		                .date(LocalDate.of(2026, 1, 1))
		                .build();

        int slack = calculator.calculateSlack(
                person,
                worship,
                List.of(event));

        assertEquals(0, slack);
    }
    
    @Test
    void shouldUsePairingCapacityWhenSmallerThanAvailability() {
        Person p1 = Person.builder().build();
        Person p2 = Person.builder().build();
    
        Pairing pairing = Pairing.builder()
			                .people(Set.of(p1, p2))
			                .maxShifts(3)
			                .shiftsWorked(2)
			                .build();

        List<Event> events = List.of(
                Event.builder().date(LocalDate.of(2026, 1, 1)).build(),
                Event.builder().date(LocalDate.of(2026, 1, 8)).build(),
                Event.builder().date(LocalDate.of(2026, 1, 15)).build()
        );

        int slack = calculator.calculateSlack(pairing, events);

        assertEquals(1, slack);
    }

    @Test
    void shouldReturnZeroWhenPairingHasReachedMaxShifts() {
        Person p1 = Person.builder().build();
        Person p2 = Person.builder().build();

        Pairing pairing = Pairing.builder()
                .people(Set.of(p1, p2))
                .maxShifts(2)
                .shiftsWorked(2)
                .build();

        Event event = Event.builder()
		                .date(LocalDate.of(2026, 1, 1))
		                .build();

        int slack = calculator.calculateSlack(
                pairing,
                List.of(event));

        assertEquals(0, slack);
    }

    @Test
    void shouldIgnoreUnavailableEventsWhenCalculatingSlack() {
        SoloRole welcome = SoloRole.builder()
			                .id(1)
			                .build();

        PersonRole personRole = PersonRole.builder()
				                .role(welcome)
				                .maxShifts(10)
				                .build();

        LocalDate unavailableDate = LocalDate.of(2026, 1, 8);

        Person person = Person.builder()
			                .roles(Set.of(personRole))
			                .unavailability(Set.of(unavailableDate))
			                .build();

        Event event1 = Event.builder()
		                .date(LocalDate.of(2026, 1, 1))
		                .build();

        Event event2 = Event.builder()
		                .date(unavailableDate)
		                .build();

        Event event3 = Event.builder()
		                .date(LocalDate.of(2026, 1, 15))
		                .build();

        int slack = calculator.calculateSlack(
                person,
                welcome,
                List.of(event1, event2, event3));

        assertEquals(2, slack);
    }

    @Test
    void shouldReturnZeroSlackWhenAllFutureEventsAreUnavailable() {
    	SoloRole welcome = SoloRole.builder()
                .id(1)
                .build();

    	PersonRole personRole = PersonRole.builder()
	                .role(welcome)
	                .maxShifts(10)
	                .build();

        LocalDate date1 = LocalDate.of(2026, 1, 1);
        LocalDate date2 = LocalDate.of(2026, 1, 8);

        Person person = Person.builder()
		                .roles(Set.of(personRole))
		                .unavailability(Set.of(date1, date2))
		                .build();

        List<Event> events = List.of(
				                Event.builder().date(date1).build(),
				                Event.builder().date(date2).build());

        int slack = calculator.calculateSlack(
                person,
                welcome,
                events);

        assertEquals(0, slack);
    }

    @Test
    void shouldIgnoreEventsBlockedByCooldown() {
    	SoloRole welcome = SoloRole.builder()
                .id(1)
                .build();

    	PersonRole personRole = PersonRole.builder()
	                .role(welcome)
	                .maxShifts(10)
	                .build();


        Person person = Person.builder()
			                .roles(Set.of(personRole))
			                .lastServed(LocalDate.of(2026, 1, 1))
			                .cooldown(2)
			                .build();

        Event event1 = Event.builder()
		                .date(LocalDate.of(2026, 1, 8))
		                .build();

        Event event2 = Event.builder()
		                .date(LocalDate.of(2026, 1, 15))
		                .build();

        Event event3 = Event.builder()
		                .date(LocalDate.of(2026, 1, 22))
		                .build();

        int slack = calculator.calculateSlack(
                person,
                welcome,
                List.of(event1, event2, event3));

        assertEquals(1, slack);
    }

    @Test
    void shouldReturnZeroSlackWhenAllFutureEventsAreBlockedByCooldown() {
    	SoloRole welcome = SoloRole.builder()
                .id(1)
                .build();

    	PersonRole personRole = PersonRole.builder()
	                .role(welcome)
	                .maxShifts(10)
	                .build();

        Person person = Person.builder()
			                .roles(Set.of(personRole))
			                .lastServed(LocalDate.of(2026, 1, 1))
			                .cooldown(4)
			                .build();

        List<Event> events = List.of(
				                Event.builder()
				                        .date(LocalDate.of(2026, 1, 8))
				                        .build(),
				                Event.builder()
				                        .date(LocalDate.of(2026, 1, 15))
				                        .build());

        int slack = calculator.calculateSlack(
                person,
                welcome,
                events);

        assertEquals(0, slack);
    }

    @Test
    void shouldUseLeastAvailableMemberForPairingSlack() {
        LocalDate unavailableDate = LocalDate.of(2026, 1, 8);

        Person person1 = Person.builder()
			                .unavailability(Set.of(unavailableDate))
			                .build();

        Person person2 = Person.builder()
            				.build();

        Pairing pairing = Pairing.builder()
			                .people(Set.of(person1, person2))
			                .maxShifts(0)
			                .build();

        List<Event> events = List.of(
				                Event.builder()
				                        .date(LocalDate.of(2026, 1, 1))
				                        .build(),
				                Event.builder()
				                        .date(unavailableDate)
				                        .build(),
				                Event.builder()
				                        .date(LocalDate.of(2026, 1, 15))
				                        .build());

        int slack = calculator.calculateSlack(
                pairing,
                events);

        assertEquals(2, slack);
    }

    @Test
    void shouldUsePairingCapacityEvenWhenAvailabilityIsHigher() {
        Person person1 = Person.builder().build();
        Person person2 = Person.builder().build();

        Pairing pairing = Pairing.builder()
			                .people(Set.of(person1, person2))
			                .maxShifts(3)
			                .shiftsWorked(2)
			                .build();

        List<Event> events = List.of(
				                Event.builder().date(LocalDate.of(2026, 1, 1)).build(),
				                Event.builder().date(LocalDate.of(2026, 1, 8)).build(),
				                Event.builder().date(LocalDate.of(2026, 1, 15)).build());

        int slack = calculator.calculateSlack(
                pairing,
                events);

        assertEquals(1, slack);
    }
}
