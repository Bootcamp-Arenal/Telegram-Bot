package com.arenal.telegrambot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arenal.telegrambot.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String>{
    List<Chat> findAll();
    
    public long count();
}
