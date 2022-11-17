package com.arenal.bot.utils;

import com.arenal.bot.persistence.model.telegrambot.Activity;
import com.arenal.bot.persistence.model.telegrambot.Chat;
import com.arenal.bot.persistence.model.telegrambot.Team;
import com.arenal.bot.persistence.model.telegrambot.Teams;
import com.arenal.bot.persistence.repository.telegrambot.ChatRepository;
import com.arenal.bot.service.telegrambot.jsondigest.JsonDigestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

    private final String[] inPodiumEmojis = {"\uD83E\uDD47", "\uD83E\uDD48", "\uD83E\uDD49"};
    private final String[] outOfPodiumEmojis = {"4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "🔟"};

    public SendMessage scoreboard(SendMessage messageToBeSent) {
        Teams teams = jsonDigestService.getTeams(jsonDigestService.readTeamdataJsonFile());
        StringBuilder message = new StringBuilder();

        Map<String, Integer> sortedMap = teams.toList().stream()
                .sorted(Comparator.comparing(Team::getTotalScore).reversed())
                .collect(Collectors.toMap(Team::getName, Team::getTotalScore, (t1, t2) -> t1, LinkedHashMap::new));

        int index = 0;
        for (Map.Entry<String, Integer> team : sortedMap.entrySet()) {
            message.append((index <= 2) ? inPodiumEmojis[index++] : ++index + "th")
                    .append(" ")
                    .append(team.getKey())
                    .append(" : ")
                    .append(team.getValue())
                    .append(" points\n");

        }
        messageToBeSent.setText(String.valueOf(message));
        return messageToBeSent;
    }

    public SendMessage team(SendMessage messageToBeSent, String teamName) {
        Teams teams = jsonDigestService.getTeams(jsonDigestService.readTeamdataJsonFile());
        StringBuilder message = new StringBuilder();
        Optional<Team> teamOptional = teams.toList().stream().filter(team -> team.getName().equals(teamName)).findAny();

        if (teamOptional.isEmpty()) {
            messageToBeSent.setText("😢 I couldn't find " + teamName + ". Make sure the team's name is correct");
            return messageToBeSent;
        }
        Team team = teamOptional.get();
        message.append(teamName).append("\n");

        for (Activity activity : team.getActividades()) {
            message.append(activity.toString()).append("\n");
        }
        messageToBeSent.setText(message.toString());

        return messageToBeSent;

    }

    public SendMessage start(SendMessage messageToBeSent, String chatId) {
        boolean isChatIdPresent = chatRepository.findByChatId(chatId).isPresent();
        if (isChatIdPresent) {
            messageToBeSent.setText("😵 You're already subscribed to the service");
        } else {
            chatRepository.save(new Chat(chatId));
            messageToBeSent.setText("Hello World! 🎉\nYou're now subscribed to the service");
        }
        return messageToBeSent;
    }

    public SendMessage help(SendMessage messageToBeSent) {
        String commands = "/help - Available commands\n" +
                "/team example team - The team's score\n" +
                "/scoreboard - Each team's score\n" +
                "/start - Subscribe to receive updates\n" +
                "/web - Web link\n";
        StringBuilder bld = new StringBuilder(commands);

        logger.debug("Commands:\n" + bld.toString());
        messageToBeSent.setText(bld.toString());

        return messageToBeSent;
    }
}
