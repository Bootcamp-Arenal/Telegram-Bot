package com.arenal.bot.persistence.model.telegrambot;

import java.util.Objects;

public class Activity {
    public String name;
    public int puntos;

    public Activity(String name, int puntos) {
        this.name = name;
        this.puntos = puntos;
    }


    public Activity() {
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getPuntos() {
        return puntos;
    }


    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, puntos);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Activity other = (Activity) obj;
        return Objects.equals(name, other.name) && puntos == other.puntos;
    }

    @Override
    public String toString() {
        return "Activity: " + name + ", points=" + puntos;
    }


}
