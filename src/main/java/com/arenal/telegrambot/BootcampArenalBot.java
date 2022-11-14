package com.arenal.telegrambot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.arenal.telegrambot.model.Chat;
import com.arenal.telegrambot.model.Chats;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BootcampArenalBot extends TelegramLongPollingBot {

    private String username;
    private String token;
    private List<String> chatIds;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.getMessage().getText().equals("/start")) {
            String chatId = update.getMessage().getChatId().toString();

            if (this.chatIds.contains(chatId)) {
                sendMessage.setText("Ya estás suscrito a los cambios");
            } else {
                try {
                    writeChatId(chatId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

    public BootcampArenalBot() throws IOException {
        super();
        this.username = "bootcamp_arenal_bot";
        this.token = "5732632626:AAGbGOF26WCUxdidgNrixs5iGIiVFoQw_gE";
        this.chatIds = this.readChatIds();
    }

    public List<String> readChatIds() throws IOException {

        FileInputStream fileInputStream = new FileInputStream("src/main/resources/ChatIdList.json");
        String jsonData = IOUtils.toString(fileInputStream, "UTF-8");
        Chats chats = mapper.readValue(jsonData, Chats.class);
        List<String> ids = new ArrayList<>();
        for(Chat chat : chats.getChats()){
            ids.add(chat.getId());
        }
        fileInputStream.close();
        return ids;
    }

    public void writeChatId(String chatId) throws IOException {
        chatIds.add(chatId);
        List<Chat> newChats = new ArrayList<>();
        Chats chats = new Chats();
        for(String id: chatIds){
            Chat chatToAdd = new Chat(id);
            newChats.add(chatToAdd);
        }
        chats.setChats(newChats);
        FileWriter myWriter = new FileWriter("src/main/resources/ChatIdList.json");
        myWriter.write(new Gson().toJson(chats));
        myWriter.close();
    }

    public List<String> getChatIds() {
        return this.chatIds;
    }
}
