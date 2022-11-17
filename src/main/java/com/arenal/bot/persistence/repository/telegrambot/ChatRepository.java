package com.arenal.bot.persistence.repository.telegrambot;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arenal.bot.persistence.model.telegrambot.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
	
	Optional<String> findByChatId(String chatId);
}
