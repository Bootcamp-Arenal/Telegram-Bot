package com.arenal.telegrambot.component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.arenal.telegrambot.model.Chat;
import com.arenal.telegrambot.service.TelegramBotService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Bot extends TelegramLongPollingBot {
    
    private TelegramBotService telegramBotService;

    private String username;
    private String token;
    private List<String> chatIds;

    // @Autowired
    // public Bot() {
    //     this.telegramBotService = telegramBotService;
    // }

    @Autowired
    public Bot(TelegramBotService telegramBotService) {
        super();
        this.username = "arenalJorgeBot";
        this.token = "5788244126:AAF6q63DGbclHm_Z42UOWj79J9_2_nb4tIQ";
        // this.username = "bootcamp_arenal_bot";
        // this.token = "5732632626:AAGbGOF26WCUxdidgNrixs5iGIiVFoQw_gE";
        this.telegramBotService = telegramBotService;
        this.chatIds = telegramBotService.findAll().stream().map(Chat::getChatId).collect(Collectors.toList());
    }

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.getMessage().getText().equals("/start")) {
            String chatId = update.getMessage().getChatId().toString();

            if (this.chatIds.contains(chatId)) {
                sendMessage.setText("Ya estás suscrito a los cambios");
            } else {
                telegramBotService.save(chatId);
                sendMessage.setText("Te has suscrito a los cambios");
            }
            sendMessage.setChatId(chatId);
            try {
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void setChatIds(List<String> chatIds) {
        this.chatIds = chatIds;
    }

    public List<String> getChatIds() {
        return this.chatIds;
    }
}
