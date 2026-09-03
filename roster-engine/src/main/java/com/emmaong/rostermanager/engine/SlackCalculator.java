package com.emmaong.rostermanager.engine;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import com.emmaong.rostermanager.models.Event;
import com.emmaong.rostermanager.models.Pairing;
import com.emmaong.rostermanager.models.Person;
import com.emmaong.rostermanager.models.PersonRole;
import com.emmaong.rostermanager.models.SoloRole;

/**
 * Computes slack for a candidate (person or pairing) being considered for a role
 * at a given point in the roster period.
 *
 * Slack = min(eligibleFutureEvents, remainingCapacity)
 *
 * Lower slack = more urgent to assign this candidate now, since they have fewer
 * remaining chances before cooldown/unavailability or their max-shifts cap locks
 * them out for the rest of the period.
 */
public class SlackCalculator {
    private static final int NO_CAP = 0;


    public int calculateSlack(Person person, SoloRole role, List<Event> remainingEvents) {
        int eligibleFutureEvents = countEligibleFutureEvents(person, remainingEvents);
        int remainingCapacity = remainingCapacityForSoloRole(person, role);
        return Math.min(eligibleFutureEvents, remainingCapacity);
    }

    private int remainingCapacityForSoloRole(Person person, SoloRole role) {
        PersonRole personRole = person.getRoles().stream()
                .filter(pr -> pr.getRole().getId() == role.getId())
                .findFirst()
                .orElse(null);

        if (personRole == null) {
            return 0;
        }

        int maxShifts = personRole.getMaxShifts();
        int shiftsWorked = personRole.getShiftsWorked();

        if (maxShifts <= NO_CAP) {
            return Integer.MAX_VALUE; 
        }
        return Math.max(0, maxShifts - shiftsWorked);
    }


    public int calculateSlack(Pairing pairing, List<Event> remainingEvents) {
        int eligibleFutureEvents = pairing.getPeople().stream()
                .mapToInt(person -> countEligibleFutureEvents(person, remainingEvents))
                .min()
                .orElse(0);

        int remainingCapacity = remainingCapacityForPairing(pairing);

        return Math.min(eligibleFutureEvents, remainingCapacity);
    }

    private int remainingCapacityForPairing(Pairing pairing) {
        int maxShifts = pairing.getMaxShifts();
        int shiftsWorked = pairing.getShiftsWorked();

        if (maxShifts <= NO_CAP) {
            return Integer.MAX_VALUE;
        }
        return Math.max(0, maxShifts - shiftsWorked);
    }


    private int countEligibleFutureEvents(Person person, List<Event> remainingEvents) {
        int count = 0;
        for (Event event : remainingEvents) {
            if (person.isAvailableOn(event.getDate()) && person.isNotOnCooldown(event.getDate())) {
                count++;
            }
        }
        return count;
    }
}