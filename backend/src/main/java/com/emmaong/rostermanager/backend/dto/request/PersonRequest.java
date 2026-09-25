package com.emmaong.rostermanager.backend.dto.request;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class PersonRequest {
	private String name;
	private Integer cooldown;
	private LocalDate lastServed;
	private Set<PersonRoleRequest> roles;
	private Set<LocalDate> unavailability;
}
