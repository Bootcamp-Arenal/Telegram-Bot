package com.arenal.bot.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenal.bot.service.telegrambot.TelegramBotService;

@RestController
@RequestMapping(path = "/payload")
public class ArenalBotController {

	@Autowired
	private TelegramBotService telegramBotService;
	
	@GetMapping
	public String getMapping() {
		return telegramBotService.greet();
	}

	@PostMapping
	public void forwardGitHubChangesToTelegram(@RequestBody String githubJsonFile) {
		telegramBotService.forwardGitHubChangesToTelegram(githubJsonFile);
	}
}
