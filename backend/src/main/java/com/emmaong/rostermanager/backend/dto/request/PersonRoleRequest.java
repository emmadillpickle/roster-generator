package com.emmaong.rostermanager.backend.dto.request;

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
@EqualsAndHashCode(of = "roleId")
@ToString
public class PersonRoleRequest {
	private long roleId;
	private Integer shiftsWorked;
	private Integer maxShifts;
}