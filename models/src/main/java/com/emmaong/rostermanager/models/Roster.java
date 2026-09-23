package com.emmaong.rostermanager.models;

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
public class Roster {
	@Builder.Default
	private List<RosterEntry> rosterEntries = new ArrayList<>();

	public void addRosterEntry(RosterEntry entry) {
		rosterEntries.add(entry);
	}
}
