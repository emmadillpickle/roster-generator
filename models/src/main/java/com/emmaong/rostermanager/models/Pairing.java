package com.emmaong.rostermanager.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Pairing {
	private long id;
	private PairedRole role;
	private int shiftsWorked;
	
	@Builder.Default
	private int maxShifts = -1;

	@Builder.Default
	private Set<Person> people = new HashSet<>();
	
	public boolean isAvailableOn(LocalDate date) {
		boolean canServe = true;
		
		for (Person person : people) {
			canServe = canServe && person.isAvailableOn(date);
		}
		
		return canServe;
	}
	
	public boolean isNotOnCooldown(LocalDate date) {
		boolean canServe = true;
		
		for (Person person : people) {
			canServe = canServe && person.isNotOnCooldown(date);
		}
		
		return canServe;
	}
	
	public boolean hasRemainingShiftsFor(PairedRole role) {
		return shiftsWorked < maxShifts;
		
	}
	
	public void updateCounters(LocalDate date) {
		shiftsWorked++;
		
		for (Person person : people) {
			person.setLastServed(date);
		}
	}
	
}
