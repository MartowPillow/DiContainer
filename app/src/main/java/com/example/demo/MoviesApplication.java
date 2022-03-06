package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class MoviesApplication {

	public static void main(String[] args) {
		//SpringApplication.run(MoviesApplication.class, args);

		Film zero = new Film();
		System.out.println("Zero:\n"+zero.print());

		Film testFilm = new Film("Toto","Baba", new String[]{"Keke", "Lulu"},"25 dec 1995");
		System.out.println("\nTestFilm:\n"+testFilm.print());

		System.out.println("\nFMF:");
		FileMovieFinder fmf = new FileMovieFinder();
		fmf.findFromFile("/home/local.isima.fr/thyriarte/shared/cours/javapro/DiContainer/app/src/main/java/com/example/demo/Films.txt");
		for (Film film:fmf.getListe()) {
			System.out.println(film.print());
		}

		System.out.println("\nML:");
		MovieLister ml = new MovieLister();
		ml.setListe(fmf.getListe());
		for (Film film:ml.getActorFilm("Actor2")) {
			System.out.println(film.print());
		}

		System.out.println("\nWMF:");
		WebMovieFinder wmf = new WebMovieFinder();
		wmf.addFromSearch("shrek");
		for (Film film:wmf.getListe()) {
			System.out.println(film.print());
		}
	}
}
