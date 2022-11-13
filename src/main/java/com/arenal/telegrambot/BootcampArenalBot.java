package com.arenal.telegrambot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
// @ConfigurationProperties(prefix = "telegram-bot")
public class BootcampArenalBot extends TelegramLongPollingBot {

    private String username;
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        System.out.println(username + " " + token);
        SendMessage sendMessage = new SendMessage();
        if (update.getMessage().getText().equals("/start")) {
            sendMessage.setText("Hello, " + update.getMessage().getFrom().getFirstName() + "!");
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            try {
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getBotToken() {
        return "5732632626:AAGbGOF26WCUxdidgNrixs5iGIiVFoQw_gE";
    }

    @Override
    public String getBotUsername() {
        return "bootcamp_arenal_bot";
    }

}
