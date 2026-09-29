package com.emmaong.rostermanager.backend.dto.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
public class RosterEntryResponse {
	private long eventId;
	private LocalDate date;
	
	@Builder.Default
	private List<SoloAssignmentResponse> soloAssignments = new ArrayList<>();
	
	@Builder.Default
	private List<PairedAssignmentResponse> pairedAssignments = new ArrayList<>();
}
