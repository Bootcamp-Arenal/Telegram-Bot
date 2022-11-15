package com.arenal.telegrambot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.arenal.telegrambot.service.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.logger.ColorLogger;
import com.arenal.telegrambot.model.Team;
import com.arenal.telegrambot.model.Teams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class JsonDigestService {
	private ColorLogger logger = new ColorLogger();

	public Teams digestJsonFile(String jsonFile) throws FileNotModifiedException {
		List<Team> winningTeams = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		Teams teamData = new Teams();
		try {
			teamData = mapper.readValue(jsonFile, Teams.class);
			if (teamData.toList().size() >= 1) {
				int maxTeamScore = teamData.toList().stream().mapToInt(Team::getTotalScore).max().getAsInt();

				winningTeams = teamData.toList().stream()
						.filter(team -> team.getTotalScore() == maxTeamScore)
						.collect(Collectors.toList());
				teamData.setTeamdata(winningTeams);
				
				logger.info("Winning teams: " + winningTeams);
			} else {
				logger.warn("No winning teams found");
			}
		} catch (IOException e) {
			logger.error("Error reading Json file");
		}

		return teamData;
	}

	public String getJsonFileFromGithub(String githubEvent) throws FileNotModifiedException {
		String jsonFile = "";

		JsonObject root = JsonParser.parseString(githubEvent).getAsJsonObject();
		JsonObject headCommit = root.get("head_commit").getAsJsonObject();
		JsonArray modified = headCommit.get("modified").getAsJsonArray();

		if (!modified.toString().contains("src/data/teamdata.json")) {
			throw new FileNotModifiedException("teamdata.json file was not modified");
		}
		jsonFile = readFile();

		return jsonFile;
	}

	public int maxTeamsScore(Teams teamData) {
		int maxTeamScore = 0;
		for (Team team : teamData.toList()) {
			int teamScore = team.getTotalScore();
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
			logger.error("Error reading Json file");
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
				logger.warn("Error closing streams");
				e.printStackTrace();
			}
		}
		logger.info("Json file successfully read");
		return file;
	}
}
