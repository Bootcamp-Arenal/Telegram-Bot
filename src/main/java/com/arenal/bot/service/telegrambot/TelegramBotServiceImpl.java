package com.arenal.bot.service.telegrambot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import com.arenal.bot.utils.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.arenal.bot.persistence.model.telegrambot.Chat;
import com.arenal.bot.persistence.model.telegrambot.Team;
import com.arenal.bot.persistence.model.telegrambot.Teams;
import com.arenal.bot.persistence.repository.telegrambot.ChatRepository;
import com.arenal.bot.service.telegrambot.exception.FileNotModifiedException;
import com.arenal.bot.service.telegrambot.jsondigest.JsonDigestService;
import com.arenal.bot.utils.ColorLogger;

@Service
@PropertySource("classpath:bot.info")
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private ColorLogger logger;

    private ChatRepository chatRepository;
    private JsonDigestService jsonDigestService;

    @Autowired
    public TelegramBotServiceImpl(ChatRepository chatRepository, JsonDigestService jsonDigestService) {
        super();
        this.logger = new ColorLogger();
        this.chatRepository = chatRepository;
        this.jsonDigestService = jsonDigestService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String greet() {
        return "Greetings from TelegramBotService!";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveChatId(String chatId) {
        chatRepository.save(new Chat(chatId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forwardGitHubChangesToTelegram(String githubJsonFile) {

        List<Chat> telegramChats = chatRepository.findAll();

        if (telegramChats.isEmpty()) {
            logger.warn("No chat ids were found");
        } else {
            try {
                String teamDataJson = jsonDigestService.getJsonTeamdata(githubJsonFile);
                logger.debug("TeamDataJson: \n" + teamDataJson);
                Teams teams = jsonDigestService.getTeams(teamDataJson);
                Teams winnerTeams = getWinnerTeams(teams);
                String winnerTeamsMessage = getWinnerTeamsMessage(winnerTeams);

                sendWinnerTeamsMessage(telegramChats, winnerTeamsMessage);
            } catch (FileNotModifiedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendWinnerTeamsMessage(List<Chat> telegramChats, String winnerTeamsMessage) {
        SendMessage messageToBeSent = new SendMessage();
        SendMessage changeMessage = new SendMessage();
        changeMessage.setText("🆕 There has been an update!");
        for (Chat telegramChat : telegramChats) {
            changeMessage.setChatId(telegramChat.getChatId());

            messageToBeSent.setChatId(telegramChat.getChatId());
            messageToBeSent.setText(winnerTeamsMessage);
            try {
                execute(changeMessage);
                execute(messageToBeSent);
                logger.info("Message sent to chatId: " + telegramChat.getChatId());
            } catch (TelegramApiException e) {
                logger.error("Error sending message to chatId: " + telegramChat.getChatId());
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teams getWinnerTeams(Teams teams) {
        if (teams.toList().isEmpty()) {
            logger.info("No teams were found");
        } else {
            int highestScore = teams.toList().stream().mapToInt(Team::getTotalScore).max().getAsInt();

            List<Team> winnerTeams = teams.toList().stream().filter(team -> team.getTotalScore() == highestScore)
                    .collect(Collectors.toList());

            teams.setTeamData(winnerTeams);
            logger.info("Winner teams " + winnerTeams);

        }
        return teams;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateReceived(Update update) {
        Commands commands = new Commands(chatRepository, jsonDigestService);

        SendMessage messageToBeSent = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        messageToBeSent.setChatId(chatId);
        String aux = update.getMessage().getText().toString();
        logger.debug("message sent = " + aux);
        String[] updateReceived = aux.split(" ");

        switch (updateReceived[0]) {
            case "/start":
                commands.start(messageToBeSent, chatId);
                break;
            case "/help":
                commands.help(messageToBeSent);
                break;
            case "/scoreboard":
                commands.scoreboard(messageToBeSent);
                break;
            case "/team":
                if (updateReceived.length < 2) {
                    messageToBeSent.setText("😵 Incorrect format. Please use /team example team");
                } else {
                    String teamName = "";
                    for (String elem : updateReceived) {
                        if (!elem.equals(updateReceived[0])) {
                            teamName += elem + " ";
                        }
                    }
                    commands.team(messageToBeSent, teamName.trim().toUpperCase());
                }
                break;
            case "/web":
                messageToBeSent.setText("Web link: https://scoring-app-nine.vercel.app/");
                break;
            default:
                messageToBeSent.setText("Select a valid command. Use /help to get all available commands ");
                break;
        }

        try {
            execute(messageToBeSent);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    private String getWinnerTeamsMessage(Teams winnerTeams) {
        StringBuilder message = new StringBuilder("");
        List<Team> winners = winnerTeams.toList();

        boolean isMultipleWinners = winners.size() > 1;
        if (isMultipleWinners) {

            message.append("The teams taking the lead are ");

            int winnersCount = winners.size();

            for (int i = 0; i < winnersCount - 1; i++) {
                logger.debug(winners.get(i).getName().toString());
                message.append(winners.get(i).getName());
                if (i != winnersCount-2) {
                    message.append(", ");
                }
            }
            message.append(" and ");
            message.append(winners.get(winnersCount - 1).getName());

            /*for (Team team : winners) {
                message.append(team.getName());
                Team lastInWinners = winners.get(winners.size() - 1);
                if (winners.size() == 2) {
                    boolean isLastInWinners = team.equals(lastInWinners);
                    if (!isLastInWinners) {
                        boolean isSecondLastInWinners = team.equals(secondLastInWinners);
                        if (isSecondLastInWinners) {
                            message.append(" y ");
                            continue;
                        }
                        message.append(", ");
                    }
                } else {
                    Team secondLastInWinners = winners.get(winners.size() - 2);
                    boolean isLastInWinners = team.equals(lastInWinners);
                    if (!isLastInWinners) {
                        boolean isSecondLastInWinners = team.equals(secondLastInWinners);
                        if (isSecondLastInWinners) {
                            message.append(" y ");
                            continue;
                        }
                        message.append(", ");
                    }
                }


                boolean isLastInWinners = team.equals(lastInWinners);
                if (!isLastInWinners) {
                    boolean isSecondLastInWinners = team.equals(secondLastInWinners);
                    if (isSecondLastInWinners) {
                        message.append(" y ");
                        continue;
                    }
                    message.append(", ");
                }
            }*/
        } else {
            Team team = winners.get(0);
            message.append("The team taking the lead is " + team.getName());
        }

        int highestScore = winners.get(0).getTotalScore();
        message.append(" with " + highestScore + " points");

        return message.toString();
    }

}
