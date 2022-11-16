package com.arenal.telegrambot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.arenal.telegrambot.application.telegramBot.JsonDigestService;

@Component
@PropertySource("classpath:application.properties")
// @ConfigurationProperties(prefix = "telegram-bot")
public class BootcampArenalBot extends TelegramLongPollingBot {

    private String username;
    private String token;
    @Value("${bot.token}")
    String token_prop;
    private List<String> chatIds;

    @Autowired
    JsonDigestService jsonDigestService;

    @Override
    public void onUpdateReceived(Update update) {
//        if (!update.hasMessage() || !update.getMessage().hasText()) {
//            return;
//        }
        SendMessage sendMessage = new SendMessage();

        if (update.getMessage().getText().equals("/scoreboard")) {

            String chatId = update.getMessage().getChatId().toString();
            sendMessage.setChatId(chatId);

            String scoreboard = jsonDigestService.updateScoreboard();
            sendMessage.setText(scoreboard);

            try {
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (update.getMessage().getText().equals("/start")) {

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

        if (update.getMessage().getText().equals("/web")) {

            String chatId = update.getMessage().getChatId().toString();
            sendMessage.setChatId(chatId);

            String webLink = "El enlace es: https://scoring-app-nine.vercel.app/";
            sendMessage.setText(webLink);

            try {
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (update.getMessage().getText().equals("/help")) {

            String chatId = update.getMessage().getChatId().toString();
            sendMessage.setChatId(chatId);

            try {
                File file = new File("./src/main/resources/commands.properties");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                String line;
                StringBuilder bld = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    String aux = "/" + line + "\n";
                    bld.append(aux);
                }

                sendMessage.setText(bld.toString());
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (update.getMessage().getText().contains("/team")) {
            String message;
            if (update.getMessage().getText().equals("/team")) {
                message = "\uD83D\uDE00Incorrect format. Please use \"/team teamName\" ";
            } else {
                String aux = update.getMessage().getText().toString();
                String[] receivedMessage = aux.split(" ");
                String teamName = receivedMessage[1];

                message = jsonDigestService.getTeamScores(teamName);
            }

            String chatId = update.getMessage().getChatId().toString();
            sendMessage.setChatId(chatId);

            sendMessage.setText(message);

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

    /**
     * This function returns a list of chat ids
     *
     * @return A list of chatIds
     */
    public List<String> getChatIds() {
        return this.chatIds;
    }

    /**
     * Sets the chatIds of the message
     *
     * @param chatIds A list of chat IDs to send the message to.
     */
    public void setChatIds(List<String> chatIds) {
        this.chatIds = chatIds;
    }

    @Autowired
    public BootcampArenalBot(@Value("${bot.token}") String token_prop) {
        super();
        this.username = "bootcamp_arenal_bot";
        this.token = token_prop;
        this.chatIds = new ArrayList<String>();
    }

}
