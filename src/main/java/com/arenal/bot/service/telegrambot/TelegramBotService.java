package com.arenal.bot.service.telegrambot;

import com.arenal.bot.persistence.model.telegrambot.Teams;

public interface TelegramBotService {
	
	/**
	 * Delete when no REST API easy testing is required.
	 * Example function to test the service.
	 * Its implementation may vary throughout the development.
	 * @return a string with a greeting message from the service
	 */
	public String greet();
	
	
	/**
	 * Through a GitHub webhook's event, this will parse a JSON file 
	 * and send a message with the telegram bot to all subscribed users.
	 * @param githubJsonFile the GitHub's HTTP request body.
	 */
	public void forwardGitHubChangesToTelegram(String githubJsonFile);
	
	/**
	 * Returns a list of winner teams given a list
	 * of teams.
	 * @param teams A list of teams.
	 * @return A list of the winner teams.
	 */
	public Teams getWinnerTeams(Teams teams);
	
	/**
	 * Stores a chatId into the database.
	 * @param chatId string used by the telegram bot to reach a chat.
	 */
	public void saveChatId(String chatId);
}
