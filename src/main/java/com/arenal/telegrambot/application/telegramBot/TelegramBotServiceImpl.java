package com.arenal.telegrambot.application.telegramBot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenal.telegrambot.application.telegramBot.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.model.Team;
import com.arenal.telegrambot.model.Teams;

@Service
public class TelegramBotServiceImpl implements TelegramBotService {

	private JsonDigestService jsonDigest;

	@Autowired
	public TelegramBotServiceImpl(JsonDigestService jsonDigest) {
		super();
		this.jsonDigest = jsonDigest;
	}

	@Override
	public void forwardChangesToTelegram() {
	}

	@Override
	public String digest(String jsonFile) {
		String string = "";
		try {
			Teams teamsData = jsonDigest.digest(jsonFile);
			
			List<Team> teamDataList = teamsData.toList();
			
			if (teamDataList.size() > 1) {
				string = "Los equipos que van ganando son ";
				for (Team team : teamDataList) {
					string += team.getName();
					if (!team.equals(teamDataList.get(teamDataList.size() - 1))) {
						if(team.equals(teamDataList.get(teamDataList.size() -2))) {
							string += " y ";
							continue;
						}
						string += ", ";
					}
				}
			} else {
				Team team = teamDataList.get(0);
				string += String.format("El equipo que va ganando es %s", team.getName());
			}
			string += String.format(" con %d puntos", jsonDigest.maxTeamsScore(teamsData));
			return string;
		} catch (FileNotModifiedException e) {
			string = e.getMessage();
		}
		return string;
	}
		
		
		

}
