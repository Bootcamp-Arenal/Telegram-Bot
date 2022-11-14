package com.arenal.telegrambot.model;

import java.util.List;
import java.util.Objects;

public class Team {
	public int id;
	public String name;
	public List<Activity> actividades;
	
	public Team(int id, String name, List<Activity> actividades) {
		this.id = id;
		this.name = name;
		this.actividades = actividades;
	}
	
	public Team() {
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Activity> getActividades() {
		return actividades;
	}


	public void setActividades(List<Activity> actividades) {
		this.actividades = actividades;
	}

	public Integer getTotalScore () {
		Integer score = 0;
		for (Activity activity : actividades) {
			score += activity.getPuntos();
		}
		return score;
	}


	@Override
	public int hashCode() {
		return Objects.hash(actividades, id, name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return Objects.equals(actividades, other.actividades) && id == other.id && Objects.equals(name, other.name);
	}
	
	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", actividades=" + actividades + "]";
	}
}
