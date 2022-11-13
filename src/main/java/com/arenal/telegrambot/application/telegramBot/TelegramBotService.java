package com.arenal.telegrambot.application.telegramBot;

public interface TelegramBotService {

	public void forwardChangesToTelegram();
	public String digest(String jsonFile);

}
