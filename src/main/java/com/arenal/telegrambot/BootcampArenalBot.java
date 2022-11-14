package com.arenal.telegrambot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
// @ConfigurationProperties(prefix = "telegram-bot")
public class BootcampArenalBot extends TelegramLongPollingBot {

	private String username;
	private String token;
	private List<String> chatIds;

	@Override
	public void onUpdateReceived(Update update) {
//        if (!update.hasMessage() || !update.getMessage().hasText()) {
//            return;
//        }
		SendMessage sendMessage = new SendMessage();
		if (update.getMessage().getText().equals("/start")) {
			String chatId = update.getMessage().getChatId().toString();

            if(this.chatIds.contains(chatId)) {
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

	}

	@Override
	public String getBotToken() {
		return this.token;
	}

	@Override
	public String getBotUsername() {
		return this.username;
	}

	public List<String> getChatIds() {
		return this.chatIds;
	}

	public void setChatIds(List<String> chatIds) {
		this.chatIds = chatIds;
	}

    public BootcampArenalBot() {
        super();
        this.username = "arenal_score_bot";
        this.token = "5673639984:AAWMukhCFyazL48Wiqve";
        this.chatIds = new ArrayList<String>();
    }
    

	
//	public void BootcampArenalBot() {
//		this.token = this.getBotToken();
//	}

	// @Override
	// public void onUpdateReceived(Update update) {
	// // TODO Auto-generated method stub

	// }

	// @Override
	// public String getBotUsername() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	// @Override
	// public String getBotToken() {

	// //Obtener inputstream de carpeta resources
	// ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	// InputStream is = classloader.getResourceAsStream("token.txt");

	// StringBuilder sb = new StringBuilder();
	// BufferedReader br = new BufferedReader(new InputStreamReader(is));

	// String line;
	// try {
	// while ((line = br.readLine()) != null) {
	// sb.append(line + System.lineSeparator());
	// }
	// } catch (IOException e) {
	// System.err.println("Error al leer el archivo");
	// e.printStackTrace();
	// }

	// return sb.toString();

	// }

}
