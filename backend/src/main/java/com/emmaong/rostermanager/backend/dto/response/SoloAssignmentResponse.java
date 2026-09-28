package com.emmaong.rostermanager.backend.dto.response;

import com.emmaong.rostermanager.models.SoloRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SoloAssignmentResponse {
	private SoloRole role;
	private PersonResponse person;
}
