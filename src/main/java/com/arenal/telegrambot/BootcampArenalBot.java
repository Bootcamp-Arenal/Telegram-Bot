package com.arenal.telegrambot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BootcampArenalBot extends TelegramLongPollingBot{
	
	private String token;

	public void BootcampArenalBot() {
		this.token = this.getBotToken();
	}

	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBotToken() {
		
		//Obtener inputstream de carpeta resources
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("token.txt");
		
        StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		 
        String line;
        try {
			while ((line = br.readLine()) != null) {
			    sb.append(line + System.lineSeparator());
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo");
			e.printStackTrace();
		}
        
        return sb.toString();

	}
	
}
