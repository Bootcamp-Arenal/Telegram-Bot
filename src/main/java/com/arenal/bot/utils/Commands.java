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

    private final String[] inPodiumEmojis = { "\uD83E\uDD47", "\uD83E\uDD48", "\uD83E\uDD49" };

    private String getTeamEmoji(Map<String, String> teamNameToEmoji, Map.Entry<Team, Integer> team) {
        return teamNameToEmoji.get(team.getKey().getName());
    }

    private Map<String, String> getTeamNameToEmoji() {
        Map<String, String> teamNameToEmoji = new LinkedHashMap<>();

        teamNameToEmoji.put("ARENAL+", "🏖");
        teamNameToEmoji.put("EQUIPO AA", "🍻");
        teamNameToEmoji.put("LOS PIBES DEL FONDO", "🥘");
        teamNameToEmoji.put("CRUCE DE BANDERAS", "🎌");
        teamNameToEmoji.put("LUNERA", "🌙");
        teamNameToEmoji.put("DEGARA", "🚲");
        teamNameToEmoji.put("CODEPIRINHA", "🍹");
        teamNameToEmoji.put("JSLOVERS", "👩🏽‍💻");
        teamNameToEmoji.put("BUGABIT", "🐛");
        teamNameToEmoji.put("VALLE SILICONA", "⛰");
        return teamNameToEmoji;
    }

    private Map<Team, Integer> teamsSortedMap(Teams teams) {
        return teams.toList().stream()
                .sorted(Comparator.comparing(Team::getTotalScore).reversed())
                .collect(Collectors.toMap(t -> t, Team::getTotalScore, (t1, t2) -> t1, LinkedHashMap::new));
    }

    public void scoreboard(SendMessage messageToBeSent) {
        Teams teams = jsonDigestService.getTeams(jsonDigestService.readTeamdataJsonFile());
        StringBuilder message = new StringBuilder();

        Map<Team, Integer> sortedMap = teamsSortedMap(teams);

        Map<String, String> teamNameToEmoji = getTeamNameToEmoji();
        int index = 0;
        for (Map.Entry<Team, Integer> team : sortedMap.entrySet()) {
            if (index <= 2) {
                message.append(inPodiumEmojis[index] + "  ");
            } else if (index <= 9) {
                message.append(index + "th ");
            } else {
                message.append(index + "th");
            }

            message.append(!getTeamEmoji(teamNameToEmoji, team).isEmpty()
                    ? " " + getTeamEmoji(teamNameToEmoji, team)
                    : "Error")
                    .append(" ")
                    .append(team.getKey().getName())
                    .append(" : ")
                    .append(team.getValue() + "\n");
            index++;
        }
        messageToBeSent.setText(String.valueOf(message));
    }

    public void detailScore(SendMessage messageToBeSent) {
        messageToBeSent.enableHtml(true);
        Teams teams = jsonDigestService.getTeams(jsonDigestService.readTeamdataJsonFile());
        StringBuilder message = new StringBuilder();

        Map<Team, Integer> sortedMap = teamsSortedMap(teams);

        Map<String, String> teamNameToEmoji = getTeamNameToEmoji();
        for (Map.Entry<Team, Integer> team : sortedMap.entrySet()) {
            String teamName = team.getKey().getName();
            Integer teamScore = team.getValue();

            message.append(getTeamEmoji(teamNameToEmoji, team))
                    .append(" <b>")
                    .append(teamName)
                    .append(" | ")
                    .append(teamScore)
                    .append("</b>\n");
            for (Activity activity : team.getKey().getActividades()) {
                message.append(activity.toString()).append("\n");
            }
            message.append("\n");
        }
        messageToBeSent.setText(message.toString());
    }

    public void team(SendMessage messageToBeSent, String[] commandReceivedSplit) {
        if (commandReceivedSplit.length < 2) {
            messageToBeSent.setText("😵 Incorrect format. Please use /team example team");
            return;
        }

        StringBuilder teamNameSB = new StringBuilder();
        for (String elem : commandReceivedSplit) {
            if (!elem.equals(commandReceivedSplit[0])) {
                teamNameSB.append(elem + " ");
            }
        }
        String teamName = teamNameSB.toString().trim().toUpperCase();
        // return team(messageToBeSent, teamName.toString().trim().toUpperCase());
        messageToBeSent.enableHtml(true);

        Teams teams = jsonDigestService.getTeams(jsonDigestService.readTeamdataJsonFile());
        StringBuilder message = new StringBuilder();
        Optional<Team> teamOptional = teams.toList().stream().filter(team -> team.getName().equals(teamName)).findAny();

        Map<String, String> teamNameToEmoji = getTeamNameToEmoji();
        if (teamOptional.isEmpty()) {
            messageToBeSent.setText("😢 I couldn't find " + teamName + ". Make sure the team's name is correct");
            return;
        }

        Team team = teamOptional.get();
        message.append(teamNameToEmoji.get(teamName))
                .append(" <b>")
                .append(teamName)
                .append(" | ")
                .append(team.getTotalScore())
                .append("</b>\n");

        for (Activity activity : team.getActividades()) {
            message.append(activity.toString()).append("\n");
        }
        messageToBeSent.setText(message.toString());
    }

    public void start(SendMessage messageToBeSent, String chatId) {
        boolean isChatIdPresent = chatRepository.findByChatId(chatId).isPresent();
        if (isChatIdPresent) {
            messageToBeSent.setText("😵 You're already subscribed to the service");
            return;
        }
        chatRepository.save(new Chat(chatId));
        messageToBeSent.setText("Hello World! 🎉\nYou're now subscribed to the service");
    }

    public void help(SendMessage messageToBeSent) {
        String commands = "/help - Available commands\n" +
                "/team example team - The team's score\n" +
                "/scoreboard - Each team's score\n" +
                "/detailscore - Each team's score and activities\n" +
                "/start - Subscribe to receive updates\n" +
                "/web - Web link\n";
        StringBuilder bld = new StringBuilder(commands);

        logger.debug("Commands:\n" + bld.toString());
        messageToBeSent.setText(bld.toString());
    }
}
