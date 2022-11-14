package com.arenal.telegrambot.model;

import java.util.List;
import java.util.Objects;

public class Teams{
	public List<Team> teamData;

	public Teams(List<Team> teamData) {
		super();
		this.teamData = teamData;
	}
	
	
	public Teams() {
		super();
	}


	public List<Team> toList() {
		return teamData;
	}

	public void setTeamData(List<Team> teamData) {
		this.teamData = teamData;
	}

	@Override
	public int hashCode() {
		return Objects.hash(teamData);
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
		return Objects.equals(teamData, other.teamData);
	}

	@Override
	public String toString() {
		return "Teams [teamData=" + teamData + "]";
	}
	

}
