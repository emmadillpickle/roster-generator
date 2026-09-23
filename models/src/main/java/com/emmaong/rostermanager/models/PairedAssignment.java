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
public class PairedAssignment implements Assignment {
	private Pairing pairing;
	private PairedRole role;
}
