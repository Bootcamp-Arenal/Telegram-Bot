package com.arenal.telegrambot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BootcampArenalBot extends TelegramLongPollingBot {

    private String username;
    private String token;
    private List<String> chatIds;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText() || !update.getMessage().getText().equals("/start")) {
            return;
        }

        SendMessage sendMessage = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();

        if (this.chatIds.contains(chatId)) {
            sendMessage.setText("Ya estás suscrito a los cambios");
        } else {
            this.chatIds.add(chatId);
            sendMessage.setText("Te has suscrito a los cambios");
        }
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    public List<String> getChatIds() {
        return this.chatIds;
    }

    public void setChatIds(List<String> chatIds) {
        this.chatIds = chatIds;
    }

    public BootcampArenalBot() {
        super();
        this.username = "arenalJorgeBot";
        this.token = "5788244126:AAF6q63DGbclHm_Z42UOWj79J9_2_nb4tIQ";
        this.chatIds = new ArrayList<String>();
    }
}
