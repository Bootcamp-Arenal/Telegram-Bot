package com.arenal.bot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ColorLogger {
    public static final String reset = "\u001B[0m";
    public static final String black = "\u001B[30m";
    public static final String red = "\u001B[31m";
    public static final String green = "\u001B[32m";
    public static final String lightYellow = "\u001B[93m";
    public static final String yellow = "\u001B[33m";
    public static final String yellowBackground = "\u001B[43m";
    public static final String blue = "\u001B[34m";
    public static final String purple = "\u001B[35m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";
    public static final String bold = "\u001B[1m";
    public static final String unbold = "\u001B[21m";
    public static final String underline = "\u001B[4m";
    public static final String noUnderline = "\u001B[24m";
    public static final String blink = "\u001B[5m";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ColorLogger.class);
    
    public void info(String message) {
        LOGGER.info(reset + message + reset);
    }
    public void warn(String message) {
        LOGGER.warn(yellow + message + reset);
    }
    public void debug(String message) {
        LOGGER.debug(blue + message + reset);
    }
    public void error(String message) {
        LOGGER.error(red + message + reset);
    }
}