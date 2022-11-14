package com.arenal.telegrambot.model;

import java.util.List;
import java.util.Objects;

public class Chats{
    public List<Chat> chats;

    public Chats(List<Chat> chats) {
        this.chats = chats;
    }

    public Chats(){}

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chats chats = (Chats) o;
        return Objects.equals(this.chats, chats.chats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chats);
    }

    @Override
    public String toString() {
        return "Chats{" +
                "chat=" + chats +
                '}';
    }
}
