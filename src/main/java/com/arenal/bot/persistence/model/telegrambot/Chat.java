package com.arenal.bot.persistence.model.telegrambot;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "chat")
@Entity
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull
	@Column(nullable = false)
	private String chatId;

	public Chat(String chatId) {
		this.chatId = chatId;
	}

	public Chat() {
	}

	public Long getId() {
		return id;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	@Override
	public String toString() {
		return "Chat{" + "id=" + id + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Chat chat = (Chat) o;
		return id == chat.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
