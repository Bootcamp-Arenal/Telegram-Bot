package com.arenal.telegrambot.application.telegramBot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.arenal.telegrambot.BootcampArenalBot;
import com.arenal.telegrambot.application.telegramBot.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.logger.ColorLogger;
import com.arenal.telegrambot.model.Team;
import com.arenal.telegrambot.model.Teams;

@Service
public class TelegramBotServiceImpl implements TelegramBotService {
	private ColorLogger logger = new ColorLogger();

	private JsonDigestService jsonDigest;

	@Autowired
	public TelegramBotServiceImpl(JsonDigestService jsonDigest) {
		super();
		this.jsonDigest = jsonDigest;
	}
	
	@Override
	public void forwardChangesToTelegram(String message, BootcampArenalBot bot) {
		if (bot.getChatIds().size() < 1) {
			logger.warn("No chat ids found");
		} else {
			for (String chatId : bot.getChatIds()) {
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(chatId);
				sendMessage.setText(message);
				try {
					bot.execute(sendMessage);
					logger.info("Message sent to chatId: " + chatId);
				} catch (TelegramApiException e) {
					logger.error("Error sending message to chatId: " + chatId);
					e.printStackTrace();
				}
			}
			logger.info(message + " was sent to all chatIds");
		}
	}

	@Override
	public String getJsonFile(String githubEvent) throws FileNotModifiedException {
		String jsonFile = jsonDigest.getJsonFileFromGithub(githubEvent);
		return getMessage(jsonFile);
	}

	@Override
	public String getMessage(String jsonFile) throws FileNotModifiedException {
		String message = "";

		Teams teamsData = jsonDigest.digestJsonFile(jsonFile);
		List<Team> teamDataList = teamsData.toList();

		if (teamDataList.size() > 1) {
			message = "Los equipos que van ganando son ";
			for (Team team : teamDataList) {
				message += team.getName();
				if (!team.equals(teamDataList.get(teamDataList.size() - 1))) {
					if (team.equals(teamDataList.get(teamDataList.size() - 2))) {
						message += " y ";
						continue;
					}
					message += ", ";
				}
			}
		} else {
			Team team = teamDataList.get(0);
			message += String.format("El equipo que va ganando es %s", team.getName());
		}
		message += String.format(" con %d puntos", jsonDigest.maxTeamsScore(teamsData));
		return message;
	}
}
