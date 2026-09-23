package com.emmaong.rostermanager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoloAssignment implements Assignment {
	private Person person;
	private SoloRole role;
}
