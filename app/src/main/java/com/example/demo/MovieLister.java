package com.example.demo;

import com.example.demo.MovieFinders.IMovieFinder;

import java.util.ArrayList;
import java.util.Arrays;

@Scannable
public class MovieLister {

    @Injectable
    private IMovieFinder movieFinder;

    public void setMovieFinder(IMovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    public ArrayList<Film> getListe(){
        return this.movieFinder.getListe();
    }

    public ArrayList<Film> getActorFilm(String actor){
        ArrayList<Film> rep = new ArrayList<Film>();

        for(Film film: this.getListe()){
            if(Arrays.asList(film.getActors()).contains(actor)) {
                rep.add(film);
            }
        }

        return rep;
    }
}
