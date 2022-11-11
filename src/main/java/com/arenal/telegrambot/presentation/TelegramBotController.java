package com.arenal.telegrambot.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenal.telegrambot.application.telegramBot.TelegramBotService;

@RestController
@RequestMapping(path = "api/v1/")
public class TelegramBotController {

	private TelegramBotService telegramBotService;

	@Autowired
	public TelegramBotController(TelegramBotService telegramBotService) {
		this.telegramBotService = telegramBotService;
	}

	@PostMapping
	public String forwardChangesToTelegram(@RequestBody String jsonFile) {
		telegramBotService.forwardChangesToTelegram();
		return "";
	}

	@GetMapping
	public String testMapping() {
		return "This is an API test";
	}

}
