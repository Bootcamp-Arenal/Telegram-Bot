package com.arenal.telegrambot.application.telegramBot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.arenal.telegrambot.BootcampArenalBot;
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
	public BootcampArenalBot createAndInitializeBot() {
		BootcampArenalBot bot = new BootcampArenalBot();
		System.out.println("Hola, he sido inicializado!!!!");
		TelegramBotsApi botsApi = null;

		try {
			botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		return bot;
	}

	@Override
	public void forwardChangesToTelegram(String message, BootcampArenalBot bot) {
		for(String chatId : bot.getChatIds()) {
			SendMessage sendMessage = new SendMessage();
			sendMessage.setChatId(chatId);
			sendMessage.setText(message);
			try {
				bot.execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public String digestLocal(String jsonFile) {
		String string = "";
		try {
			Teams teamsData = jsonDigest.digestLocal(jsonFile);

			List<Team> teamDataList = teamsData.toList();

			if (teamDataList.size() > 1) {
				string = "Los equipos que van ganando son ";
				for (Team team : teamDataList) {
					string += team.getName();
					if (!team.equals(teamDataList.get(teamDataList.size() - 1))) {
						if (team.equals(teamDataList.get(teamDataList.size() - 2))) {
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

	@Override
	public String digest(String githubEvent) {
		String string = "";
		try {
			Teams teamsData = jsonDigest.digest(githubEvent);

			List<Team> teamDataList = teamsData.toList();

			if (teamDataList.size() > 1) {
				string = "Los equipos que van ganando son ";
				for (Team team : teamDataList) {
					string += team.getName();
					if (!team.equals(teamDataList.get(teamDataList.size() - 1))) {
						if (team.equals(teamDataList.get(teamDataList.size() - 2))) {
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
