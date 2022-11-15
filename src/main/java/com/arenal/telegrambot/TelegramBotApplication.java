package com.arenal.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.arenal.telegrambot.model.Chat;
import com.arenal.telegrambot.repository.ChatRepository;

@SpringBootApplication
@ComponentScan(basePackages = "com.arenal.telegrambot")
public class TelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
	}

}
