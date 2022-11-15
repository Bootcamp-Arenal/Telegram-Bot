package com.arenal.telegrambot.application.telegramBot;

import java.util.List;

import javax.validation.Valid;

import com.arenal.telegrambot.BootcampArenalBot;
import com.arenal.telegrambot.application.telegramBot.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.model.Chat;

public interface TelegramBotService {
	void forwardChangesToTelegram(String message, BootcampArenalBot bot);
	public String getJsonFile(String githubEvent) throws FileNotModifiedException;
	public String getMessage(String jsonFile) throws FileNotModifiedException;
	public void save(@Valid String chatId);
	public List<Chat> findAll();


}
