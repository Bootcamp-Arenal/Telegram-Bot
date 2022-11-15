package com.arenal.telegrambot.service;

import java.util.List;

import javax.validation.Valid;

import com.arenal.telegrambot.service.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.component.Bot;
import com.arenal.telegrambot.model.Chat;

public interface TelegramBotService {
	void forwardChangesToTelegram(String message, Bot bot);
	public String getJsonFile(String githubEvent) throws FileNotModifiedException;
	public String getMessage(String jsonFile) throws FileNotModifiedException;
	public void save(@Valid String chatId);
	public List<Chat> findAll();


}
