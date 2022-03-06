package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;

public class Film {
    private String name;
    private String author;
    private String date;
    private String[] actors;

    public Film(String name, String author, String[] actors, String date){
        this.name=name;
        this.author=author;
        this.actors=actors;
        this.date=date;
    }
    public Film(){
        this.name="name";
        this.author="author";
        this.actors=new String[]{"actor"};
        this.date="date";
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setActors(String[] actors){
        this.actors=actors;
    }
    public String[] getActors(){
        return this.actors;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getDate(){
        return this.date;
    }
    public String print(){
        String rep =  "'" + this.name + "' by " + this.author + " was released on " + this.date;
        if(this.actors != null && this.actors.length > 0) {
            rep += ". Main actors are: ";
            for (int i = 0; i < this.actors.length; i++) {
                rep += this.actors[i];
                if (i < this.actors.length - 1) {
                    rep += ", ";
                }
            }
        }
        rep += ".";
        return rep;
    }
}
