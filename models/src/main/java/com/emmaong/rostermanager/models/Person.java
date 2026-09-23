package com.emmaong.rostermanager.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
public class Person {
	private long id;
	private String name;
	private int cooldown;
	private LocalDate lastServed;
	
	@Builder.Default
	private Set<PersonRole> roles = new HashSet<>();
	
	@Builder.Default
	private Set<LocalDate> unavailability = new HashSet<>();

	public boolean isAvailableOn(LocalDate date) {
		return !unavailability.contains(date);
	}
	
	public boolean isNotOnCooldown(LocalDate date) {
		if (lastServed == null) return true;
		
		LocalDate canServeNext = lastServed.plusWeeks(cooldown + 1);
		return date.isAfter(canServeNext) || date.isEqual(canServeNext);
	}
	
	public boolean hasRemainingShiftsFor(SoloRole role) {
		return roles.stream()
		        .filter(pr -> pr.getRole().getName().equals(role.getName()))
		        .findFirst()
		        .map(pr -> pr.getShiftsWorked() < pr.getMaxShifts())
		        .orElse(false);
	}
	
	public void updateCounters(SoloRole role, LocalDate date) {
		PersonRole personRole = roles.stream()
				.filter(pr -> pr.getRole().getName().equals(role.getName()))
				.findFirst()
				.orElse(null);
		
		if (personRole == null) {
			throw new IllegalStateException("Trying to update " + name + "'s counter for " + role.getName() + ", but they don't do this role!");
		}
		
		lastServed = date;
		personRole.setShiftsWorked(personRole.getShiftsWorked() + 1);
	}
}
