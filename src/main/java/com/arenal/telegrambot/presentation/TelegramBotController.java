package com.arenal.telegrambot.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import com.arenal.telegrambot.BootcampArenalBot;
import com.arenal.telegrambot.application.telegramBot.TelegramBotService;

@RestController
@RequestMapping(path = "/payload")
public class TelegramBotController {
	
	private BootcampArenalBot bot;

	private TelegramBotService telegramBotService;

	@Autowired
	public TelegramBotController(TelegramBotService telegramBotService) {
		this.telegramBotService = telegramBotService;
	}

	@PostMapping
	public void forwardChangesToTelegram(@RequestBody String jsonFile) {
		String message = telegramBotService.digestLocal(jsonFile);
		telegramBotService.forwardChangesToTelegram(message, bot);
		System.out.println(message);
	}

	@GetMapping
	public String testMapping() {
		bot = telegramBotService.createAndInitializeBot();
		return "Bot Initialized";
	}

}