package com.arenal.bot.service.telegrambot.exception;

public class FileNotModifiedException extends Exception {
	private static final long serialVersionUID = 1L;

	public FileNotModifiedException(String errorMessage) {
        super(errorMessage);
    }

}
