package com.arenal.telegrambot.application.telegramBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.arenal.telegrambot.application.telegramBot.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.model.Activity;
import com.arenal.telegrambot.model.Team;
import com.arenal.telegrambot.model.Teams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class JsonDigestService {
	
	public Teams digestLocal(String jsonFile) throws FileNotModifiedException {
		List<Team> winningTeams = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		Teams teamData = new Teams();
		try {
			teamData = mapper.readValue(jsonFile, Teams.class);

			int maxTeamScore = maxTeamsScore(teamData);
			Map<Team, Integer> teamDataMap = initializeTeamDataMap(teamData);
			winningTeams = getWinningTeams(teamDataMap, maxTeamScore);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Teams(winningTeams);
	}

	public Teams digest(String githubEvent) throws FileNotModifiedException {
		List<Team> winningTeams = new ArrayList<>();
		String jsonFile = "";

		JsonObject root = JsonParser.parseString(githubEvent).getAsJsonObject();
		JsonObject headCommit = root.get("head_commit").getAsJsonObject();
		JsonArray modified = headCommit.get("modified").getAsJsonArray();
		String ref = root.get("ref").toString();

		if (!modified.toString().contains("src/data/teamdata.json")) {
			throw new FileNotModifiedException("Ha habido un push que no ha modificado el archivo de los datos");
		}

		if(!ref.equals("refs/heads/main")){
			throw new FileNotModifiedException("Ha habido un push que no ha sido en la rama main");
		}

		ObjectMapper mapper = new ObjectMapper();

		jsonFile = readFile();

		Teams teamData = new Teams();
		try {
			teamData = mapper.readValue(jsonFile, Teams.class);

			int maxTeamScore = maxTeamsScore(teamData);
			Map<Team, Integer> teamDataMap = initializeTeamDataMap(teamData);
			winningTeams = getWinningTeams(teamDataMap, maxTeamScore);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Teams(winningTeams);
	}

	public int maxTeamsScore(Teams teamData) {
		int maxTeamScore = 0;
		for (Team team : teamData.toList()) {
			int teamScore = 0;
			List<Activity> actividades = team.getActividades();
			for (Activity actividad : actividades) {
				teamScore += actividad.getPuntos();
			}
			if (teamScore >= maxTeamScore) {
				maxTeamScore = teamScore;
			}
		}
		return maxTeamScore;
	}

	private String readFile() {
		String file = "";
		URL url;
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		try {

			url = new URL(
					"https://raw.githubusercontent.com/Bootcamp-Arenal/Scoring-App/develop/src/data/teamdata.json");
			URLConnection uc = url.openConnection();
			uc.setUseCaches(false);

			uc.setRequestProperty("X-Requested-With", "Curl");

			is = uc.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				file += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;

	}

	private Map<Team, Integer> initializeTeamDataMap(Teams teamData) {

		Map<Team, Integer> teamDataMap = new HashMap<>();

		for (Team team : teamData.toList()) {
			int teamScore = 0;
			List<Activity> actividades = team.getActividades();
			for (Activity actividad : actividades) {
				teamScore += actividad.getPuntos();
			}
			teamDataMap.put(team, teamScore);
		}
		return teamDataMap;
	}

	private List<Team> getWinningTeams(Map<Team, Integer> teamDataMap, int maxTeamScore) {

		return teamDataMap.keySet().stream().filter(elem -> teamDataMap.get(elem).compareTo(maxTeamScore) == 0)
				.collect(Collectors.toList());
	}
}
