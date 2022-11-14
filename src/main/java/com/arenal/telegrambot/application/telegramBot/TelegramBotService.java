package com.arenal.telegrambot.application.telegramBot;

import com.arenal.telegrambot.BootcampArenalBot;
import com.arenal.telegrambot.application.telegramBot.exceptions.FileNotModifiedException;

import java.io.IOException;

public interface TelegramBotService {
	void forwardChangesToTelegram(String message, BootcampArenalBot bot);
	public String getJsonFile(String githubEvent) throws FileNotModifiedException;
	public String getMessage(String jsonFile) throws FileNotModifiedException;


}
