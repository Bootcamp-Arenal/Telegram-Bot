package com.arenal.telegrambot.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenal.telegrambot.application.telegramBot.TelegramBotService;
import com.arenal.telegrambot.model.Team;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping(path = "/payload")
public class TelegramBotController {

	private TelegramBotService telegramBotService;

	@Autowired
	public TelegramBotController(TelegramBotService telegramBotService) {
		this.telegramBotService = telegramBotService;
	}

	@PostMapping
	public void forwardChangesToTelegram(@RequestBody String jsonFile) {
		telegramBotService.forwardChangesToTelegram();
		// 1. Leer de la rama main
		// 2. Leer los modificados
		// 3. Si en los modificados está el archivo teamdata.js
		// 4. Leer desde URL teamdata.js
		// https://www.codespeedy.com/how-to-read-file-from-url-in-java/
		// 5. Sumar los puntos de cada proyecto de cada equipo (mapa con <String
		// nombreEquipo, int puntuacion>)
		// 6. Enviar al telegramBot el nombre del equipo ganador

//		String file = telegramBotService.readJson(jsonFile);
	
			
			
			
//			Map<String, String> jsonMap = mapper.readValue(jsonFile, Map.class);
//			mapper.readValue(jsonFile, Map.class).get("head_commit");
//			System.out.println(jsonMap.get("head_commit"));
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}

//		try {
//			HashMap<String, Object> result = new ObjectMapper().readValue(jsonFile, HashMap.class);
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String digest = telegramBotService.digest(jsonFile);
		System.out.println(digest);

	}

	@GetMapping
	public String testMapping() {
		return "This is an API test";
	}

}
