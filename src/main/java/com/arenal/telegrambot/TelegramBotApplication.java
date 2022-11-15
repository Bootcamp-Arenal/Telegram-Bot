package com.arenal.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arenal.telegrambot.model.Chat;
import com.arenal.telegrambot.repository.ChatRepository;

@SpringBootApplication
public class TelegramBotApplication {
	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
	}

}
