package com.arenal.telegrambot.model;

import java.util.List;
import java.util.Objects;

public class Teams{
	public List<Team> teamdata;

	public Teams(List<Team> teamData) {
		super();
		this.teamdata = teamData;
	}
	
	
	public Teams() {
		super();
	}


	public List<Team> toList() {
		return teamdata;
	}

	public void setTeamData(List<Team> teamData) {
		this.teamdata = teamData;
	}

	@Override
	public int hashCode() {
		return Objects.hash(teamdata);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Teams other = (Teams) obj;
		return Objects.equals(teamdata, other.teamdata);
	}

	@Override
	public String toString() {
		return "Teams [teamData=" + teamdata + "]";
	}
	

}
