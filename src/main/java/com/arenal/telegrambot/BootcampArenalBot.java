package com.arenal.telegrambot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
// @ConfigurationProperties(prefix = "telegram-bot")
public class BootcampArenalBot extends TelegramLongPollingBot {

    private String username;
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
//        if (!update.hasMessage() || !update.getMessage().hasText()) {
//            return;
//        }
        System.out.println("?????????????????");
        System.out.println(this.getBotUsername() + " " + this.getBotToken());
        SendMessage sendMessage = new SendMessage();
        if (update.getMessage().getText().equals("/start")) {
            sendMessage.setText("Hello, " + update.getMessage().getFrom().getFirstName() + "!");
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            try {
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getBotToken() {
        return "5732632626:AAGbGOF26WCUxdidgNrixs5iGIiVFoQw_gE";
    }

    @Override
    public String getBotUsername() {
        return "bootcamp_arenal_bot";
    }

//	public void BootcampArenalBot() {
//		this.token = this.getBotToken();
//	}

	// @Override
	// public void onUpdateReceived(Update update) {
	// 	// TODO Auto-generated method stub
		
	// }

	// @Override
	// public String getBotUsername() {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }

	// @Override
	// public String getBotToken() {
		
	// 	//Obtener inputstream de carpeta resources
	// 	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	// 	InputStream is = classloader.getResourceAsStream("token.txt");
		
    //     StringBuilder sb = new StringBuilder();
	// 	BufferedReader br = new BufferedReader(new InputStreamReader(is));
		 
    //     String line;
    //     try {
	// 		while ((line = br.readLine()) != null) {
	// 		    sb.append(line + System.lineSeparator());
	// 		}
	// 	} catch (IOException e) {
	// 		System.err.println("Error al leer el archivo");
	// 		e.printStackTrace();
	// 	}
        
    //     return sb.toString();

	// }
	
}
