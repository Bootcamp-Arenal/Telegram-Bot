package com.arenal.bot.utils;

import com.arenal.bot.persistence.model.telegrambot.Activity;
import com.arenal.bot.persistence.model.telegrambot.Chat;
import com.arenal.bot.persistence.model.telegrambot.Team;
import com.arenal.bot.persistence.model.telegrambot.Teams;
import com.arenal.bot.persistence.repository.telegrambot.ChatRepository;
import com.arenal.bot.service.telegrambot.jsondigest.JsonDigestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Commands {
    private ColorLogger logger;

    private JsonDigestService jsonDigestService;
    private ChatRepository chatRepository;

    @Autowired
    public Commands(ChatRepository chatRepository, JsonDigestService jsonDigestService) {
        this.chatRepository = chatRepository;
        this.jsonDigestService = jsonDigestService;
        this.logger = new ColorLogger();
    }

    private final String[] firstSecondThirdEmojis = { "\uD83E\uDD47", "\uD83E\uDD48", "\uD83E\uDD49" };

    public SendMessage scoreboard(SendMessage messageToBeSent) {
        Teams teams = jsonDigestService.getTeams(jsonDigestService.readTeamdataJsonFile());
        StringBuilder message = new StringBuilder();

        Map<String, Integer> sortedMap = teams.toList().stream()
                .sorted(Comparator.comparing(Team::getTotalScore).reversed())
                .collect(Collectors.toMap(Team::getName, Team::getTotalScore, (t1, t2) -> t1, LinkedHashMap::new));

        int position = 0;
        for (Map.Entry<String, Integer> team : sortedMap.entrySet()) {
            message.append((position <= 2) ? firstSecondThirdEmojis[position++] : ++position + "º -")
                    .append(" ")
                    .append(team.getKey())
                    .append(" : ")
                    .append(team.getValue())
                    .append(" puntos\n");
        }
        messageToBeSent.setText("Current scoreboard: \n\n" + message.toString());
        return messageToBeSent;
    }

    public SendMessage team(SendMessage messageToBeSent, String teamName) {
        Teams teams = jsonDigestService.getTeams(jsonDigestService.readTeamdataJsonFile());
        StringBuilder message = new StringBuilder();
        Optional<Team> teamOptional = teams.toList().stream().filter(team -> team.getName().equals(teamName)).findAny();

        if (teamOptional.isEmpty()) {
            messageToBeSent.setText("The team do not exist. Make sure the team name is correct");
            return messageToBeSent;
        }
        Team team = teamOptional.get();
        message.append(teamName).append(":\n");

        for (Activity activity : team.getActividades()) {
            message.append(activity.toString()).append("\n");
        }
        messageToBeSent.setText(message.toString());

        return messageToBeSent;

    }

    public SendMessage start(SendMessage messageToBeSent, String chatId) {
        boolean isChatIdPresent = chatRepository.findByChatId(chatId).isPresent();
        if (isChatIdPresent) {
            messageToBeSent.setText("Ya estás suscrito a los cambios");
        } else {
            chatRepository.save(new Chat(chatId));
            messageToBeSent.setText("Te has suscrito a los cambios");
        }
        return messageToBeSent;
    }

    public SendMessage help(SendMessage messageToBeSent) {
        String commands = "/help - Available commands\n" +
                "/team {teamName} - Scores of the given team\n" +
                "/scoreboard - The score for each team\n" +
                "/start - Susbcribe to receive updates\n" +
                "/web - Web link\n";
        StringBuilder bld = new StringBuilder(commands);

        logger.debug("Commands:\n" + bld.toString());
        messageToBeSent.setText(bld.toString());

        return messageToBeSent;
    }
}
