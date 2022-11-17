package com.arenal.bot.service.telegrambot.jsondigest;

import com.arenal.bot.persistence.model.telegrambot.Teams;
import com.arenal.bot.service.telegrambot.exception.FileNotModifiedException;

public interface JsonDigestService {
	public Teams getTeams(String jsonFile);
	public String getJsonTeamdata(String githubJsonFile) throws FileNotModifiedException;

	String readTeamdataJsonFile();
}
