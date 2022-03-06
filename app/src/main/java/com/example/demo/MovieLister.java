package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieLister {

    private ArrayList<Film> liste;

    public MovieLister(){
        this.liste = new ArrayList<Film>();
    }

    public ArrayList<Film> getListe(){
        return this.liste;
    }

    public void setListe(ArrayList<Film> liste){
        this.liste = liste;
    }

    public ArrayList<Film> getActorFilm(String actor){
        ArrayList<Film> rep = new ArrayList<Film>();

        for(Film film: liste){
            if(Arrays.asList(film.getActors()).contains(actor)) {
                rep.add(film);
            }
        }

        return rep;
    }
}
