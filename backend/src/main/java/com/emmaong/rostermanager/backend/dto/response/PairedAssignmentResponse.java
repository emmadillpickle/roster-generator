package com.emmaong.rostermanager.backend.dto.response;

import com.emmaong.rostermanager.models.PairedRole;

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
public class PairedAssignmentResponse {
	private PairedRole role;
	private PairingResponse pairing;
}
