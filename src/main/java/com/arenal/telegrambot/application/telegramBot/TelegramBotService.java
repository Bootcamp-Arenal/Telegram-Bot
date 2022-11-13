package com.arenal.telegrambot.application.telegramBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;

import com.arenal.telegrambot.BootcampArenalBot;

public interface TelegramBotService {

	public String digest(String jsonFile);
	public BootcampArenalBot createAndInitializeBot();
	public String digestLocal(String jsonFile);
	void forwardChangesToTelegram(String message, BootcampArenalBot bot);

}
