package com.arenal.telegrambot.application.telegramBot.exceptions;

public class FileNotModifiedException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileNotModifiedException(String errorMessage) {
        super(errorMessage);
    }

}
