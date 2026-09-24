package com.emmaong.rostermanager.backend.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PairingRequest {
	private long roleId;
	private Integer shiftsWorked;
	private Integer maxShifts;
	private List<Integer> people;
}
