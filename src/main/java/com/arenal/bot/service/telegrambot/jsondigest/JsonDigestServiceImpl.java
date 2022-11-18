package com.arenal.bot.service.telegrambot.jsondigest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.arenal.bot.persistence.model.telegrambot.Teams;
import com.arenal.bot.service.telegrambot.exception.FileNotModifiedException;
import com.arenal.bot.utils.ColorLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
@PropertySource("classpath:application.properties")
public class JsonDigestServiceImpl implements JsonDigestService {
	private static final String BRANCH_MODIFIED = "main";
	private static final String FILE = "teamdata.json";
	private static final String FILE_PATH = "src/data/" + FILE;
	private static final String BASE_URL = "https://raw.githubusercontent.com/danibanez/bootcampsolera/";
	private static final String URL = BASE_URL + BRANCH_MODIFIED + "/" + FILE_PATH;

	private ColorLogger logger;

	public JsonDigestServiceImpl() {
		logger = new ColorLogger();
	}
	@Override
	public Teams getTeams(String jsonFile) {
		Teams teamData = new Teams();
		ObjectMapper mapper = new ObjectMapper();
		logger.debug("jsonFile is:\n" + jsonFile);
		try {
			teamData = mapper.readValue(jsonFile, Teams.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error("Error reading JSON file");
		}
		return teamData;
	}

	@Override
	public String getJsonTeamdata(String githubJsonFile) throws FileNotModifiedException {

		if (!isTeamdataModified(githubJsonFile)) {
			throw new FileNotModifiedException(FILE + " file was not modified");
		}
		logger.debug("gotIntoJsonTeamData");
		return readTeamdataJsonFile();
	}

	private boolean isTeamdataModified(String githubJsonFile) {

		JsonObject root = JsonParser.parseString(githubJsonFile).getAsJsonObject();
		JsonObject headCommit = root.get("head_commit").getAsJsonObject();
		JsonArray modified = headCommit.get("modified").getAsJsonArray();
		String ref = root.get("ref").toString();

		return ref.contains("refs/heads/" + BRANCH_MODIFIED) && modified.toString().contains(FILE_PATH);
	}

	@Override
	public String readTeamdataJsonFile() {
		String file = "";
		URL url;
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		try {

			url = new URL(URL);
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
			logger.debug("Json file:\n" + file);
		} catch (IOException e) {
			logger.error("Error reading Json file");
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
				logger.warn("Error closing streams");
				e.printStackTrace();
			}
		}
		logger.info("Json file successfully read");
		return file;
	}
}
