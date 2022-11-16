package com.arenal.telegrambot.application.telegramBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import com.arenal.telegrambot.application.telegramBot.exceptions.FileNotModifiedException;
import com.arenal.telegrambot.model.Activity;
import com.arenal.telegrambot.model.Team;
import com.arenal.telegrambot.model.Teams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class JsonDigestService {

    private String[] firstSecondThirdEmojis = {"\uD83E\uDD47", "\uD83E\uDD48", "\uD83E\uDD49"};

    /**
     * It takes a json file as input, parses it, and returns a list of winning teams
     *
     * @param jsonFile The file path to the JSON file
     * @return A list of winning teams.
     */
    public Teams digestLocal(String jsonFile) throws FileNotModifiedException {
        List<Team> winningTeams = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Teams teamData = new Teams();
        try {
            teamData = mapper.readValue(jsonFile, Teams.class);

            int maxTeamScore = maxTeamsScore(teamData);
            Map<Team, Integer> teamDataMap = initializeTeamDataMap(teamData);
            winningTeams = getWinningTeams(teamDataMap, maxTeamScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Teams(winningTeams);
    }

    /**
     * It reads the JSON file, creates a map with the teams and their scores, and returns a list of the teams with the
     * highest score
     *
     * @param githubEvent The event that triggered the webhook.
     * @return A list of teams that have the highest score.
     */
    public Teams digest(String githubEvent) throws FileNotModifiedException {
        List<Team> winningTeams = new ArrayList<>();
        String jsonFile = "";

        JsonObject root = JsonParser.parseString(githubEvent).getAsJsonObject();
        JsonObject headCommit = root.get("head_commit").getAsJsonObject();
        JsonArray modified = headCommit.get("modified").getAsJsonArray();
        String ref = root.get("ref").toString();

        if (!ref.contains("refs/heads/main")) {
            throw new FileNotModifiedException("Ha habido un push que no ha sido en la rama main");
        }

        if (!modified.toString().contains("src/data/teamdata.json")) {
            throw new FileNotModifiedException("Ha habido un push que no ha modificado el archivo de los datos");
        }

        ObjectMapper mapper = new ObjectMapper();

        jsonFile = readFile();

        Teams teamData = new Teams();
        try {
            teamData = mapper.readValue(jsonFile, Teams.class);

            int maxTeamScore = maxTeamsScore(teamData);
            Map<Team, Integer> teamDataMap = initializeTeamDataMap(teamData);
            winningTeams = getWinningTeams(teamDataMap, maxTeamScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Teams(winningTeams);
    }

    /**
     * The function iterates through the list of teams, and for each team it iterates through the list of activities,
     * adding the points of each activity to the team's score. If the team's score is greater than the current max score,
     * the max score is updated
     *
     * @param teamData The data of all the teams.
     * @return The maximum score of all the teams.
     */
    public int maxTeamsScore(Teams teamData) {
        int maxTeamScore = 0;
        for (Team team : teamData.toList()) {
            int teamScore = 0;
            List<Activity> actividades = team.getActividades();
            for (Activity actividad : actividades) {
                teamScore += actividad.getPuntos();
            }
            if (teamScore >= maxTeamScore) {
                maxTeamScore = teamScore;
            }
        }
        return maxTeamScore;
    }

    /**
     * It reads a JSON file, parses it into a Java object, creates a map with the data, sorts the map by value, and returns
     * a string with the sorted data
     *
     * @return The scoreboard is being returned.
     */
    public String updateScoreboard() {

        String jsonFile = "";
        ObjectMapper mapper = new ObjectMapper();
        Map<Team, Integer> teamDataMap = new HashMap<>();

        jsonFile = readFile();

        Teams teamData = new Teams();
        try {
            teamData = mapper.readValue(jsonFile, Teams.class);
            teamDataMap = initializeTeamDataMap(teamData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Sort map by value
        LinkedHashMap<Team, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> valueList = new ArrayList<>();

        for (Map.Entry<Team, Integer> entry : teamDataMap.entrySet()) {
            valueList.add(entry.getValue());
        }
        Collections.sort(valueList, Collections.reverseOrder());

        for (int num : valueList) {
            for (Entry<Team, Integer> entry : teamDataMap.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }

        int position = 0;
        String scoreboard = "";

        for (Map.Entry<Team, Integer> team : sortedMap.entrySet()) {
            scoreboard += ((position <= 2) ? firstSecondThirdEmojis[position++] : ++position + "º -") + " " + team.getKey().getName() + " : " + team.getValue() + " puntos\n";
        }


        // String scoreboard = teamDataMap.entrySet().stream()
        // 		.sorted(Map.Entry.<Team, Integer>comparingByValue().reversed())
        // 		.map(entry -> entry.getKey().getName() + ": " + entry.getValue())
        // 		.collect(Collectors.joining("\n"));

        return "Clasificacion actual: \n\n" + scoreboard;

    }

    /**
     * It reads a file from a URL and returns the contents of the file as a string
     *
     * @return A string of the JSON file.
     */
    private String readFile() {
        String file = "";
        URL url;
        BufferedReader br = null;
        InputStreamReader isr = null;
        InputStream is = null;
        try {

            url = new URL(
                    "https://raw.githubusercontent.com/Bootcamp-Arenal/Scoring-App/main/src/data/teamdata.json");
            URLConnection uc = url.openConnection();
            uc.setUseCaches(false);

            uc.setRequestProperty("X-Requested-With", "Curl");

            is = uc.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                file += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;

    }

    /**
     * This function takes a list of teams and returns a map of teams and their scores
     *
     * @param teamData The data of the teams.
     * @return A map with the teams and their scores.
     */
    private Map<Team, Integer> initializeTeamDataMap(Teams teamData) {

        Map<Team, Integer> teamDataMap = new HashMap<>();

        for (Team team : teamData.toList()) {
            int teamScore = 0;
            List<Activity> actividades = team.getActividades();
            for (Activity actividad : actividades) {
                teamScore += actividad.getPuntos();
            }
            teamDataMap.put(team, teamScore);
        }
        return teamDataMap;
    }

    /**
     * It takes a map of teams and their scores, and returns a list of teams that have the highest score
     *
     * @param teamDataMap  A map of Team objects to their scores.
     * @param maxTeamScore The maximum score of the team.
     * @return A list of teams that have the highest score.
     */
    private List<Team> getWinningTeams(Map<Team, Integer> teamDataMap, int maxTeamScore) {

        return teamDataMap.keySet().stream().filter(elem -> teamDataMap.get(elem).compareTo(maxTeamScore) == 0)
                .collect(Collectors.toList());
    }

    /**
     * It reads the json file, creates a list of teams, and then iterates through the list to find the team with the name
     * that was passed as a parameter. If the team is found, it returns a string with the team name and the scores of each
     * activity. If the team is not found, it returns a message saying that the team does not exist
     *
     * @param teamName The name of the team you want to get the scores for.
     * @return A string with the team name and the activities of the team.
     */
    public String getTeamScores(String teamName) {
        String jsonFile = readFile();
        ObjectMapper mapper = new ObjectMapper();

        StringBuilder message = new StringBuilder();

        try {
            for (Team team : mapper.readValue(jsonFile, Teams.class).toList()) {
                if (team.name.equals(teamName)) {
                    message.append(teamName).append(":\n");
                    List<Activity> actividades = team.getActividades();
                    for (Activity actividad : actividades) {
                        message.append(actividad.toString()).append("\n");
                    }
                    return message.toString();
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "The team do not exist. Make sure the team name is correct.";
    }
}
