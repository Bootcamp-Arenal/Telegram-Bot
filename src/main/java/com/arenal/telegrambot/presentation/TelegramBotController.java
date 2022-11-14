package com.arenal.telegrambot.presentation;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenal.telegrambot.BootcampArenalBot;
import com.arenal.telegrambot.application.telegramBot.TelegramBotService;
import com.arenal.telegrambot.application.telegramBot.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.logger.ColorLogger;

@RestController
@RequestMapping(path = "/payload")
public class TelegramBotController {
	private ColorLogger logger = new ColorLogger();
	
	@Inject
	private BootcampArenalBot bot;

	private TelegramBotService telegramBotService;

	@Autowired
	public TelegramBotController(TelegramBotService telegramBotService) {
		this.telegramBotService = telegramBotService;
	}

	@PostMapping
	public void forwardChangesToTelegram(@RequestBody String jsonFile) {
		try {
			String message = telegramBotService.getMessage(jsonFile);
			telegramBotService.forwardChangesToTelegram(message, bot);
		} catch (FileNotModifiedException e) {
			logger.info(e.getMessage());
		}

	}

	@GetMapping
	public String testMapping() {
		return "API Test";
	}

}
