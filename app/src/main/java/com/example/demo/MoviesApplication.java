package com.example.demo;

import com.example.demo.MovieFinders.FileMovieFinder;
import com.example.demo.MovieFinders.WebMovieFinder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class MoviesApplication {

	public static void main(String[] args) throws Exception {
		//SpringApplication.run(MoviesApplication.class, args);

		////Methode 2 : Pas de container, tout a la main
		/*
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

		System.out.println("\nWMF:");
		WebMovieFinder wmf = new WebMovieFinder();
		wmf.addFromSearch("shrek");
		for (Film film:wmf.getListe()) {
			System.out.println(film.print());
		}

		System.out.println("\nML:");
		MovieLister ml = new MovieLister();
		ml.setMovieFinder(wmf);
		for (Film film:ml.getActorFilm("Christopher Knights")) {
			System.out.println(film.print());
		}
		*/

		////Methode 2 : Container rempli a la main
		/*
		//MovieLister requires IMovieFinder
		//IMovieFinder requires AuditService
		Set<Class<?>> movieClasses = new HashSet<>();
		movieClasses.add(FileMovieFinder.class);
		movieClasses.add(WebMovieFinder.class);
		movieClasses.add(MovieLister.class);
		//movieClasses.add(AuditService.class); //on le fait apres pour tester

		Container conteneur = new Container(movieClasses);

		conteneur.register(AuditService.class);

		FileMovieFinder fmf = conteneur.newInstance(FileMovieFinder.class);
		WebMovieFinder wmf = conteneur.newInstance(WebMovieFinder.class);
		MovieLister ml = conteneur.newInstance(MovieLister.class);
		AuditService as = conteneur.newInstance(AuditService.class);

		System.out.println("\nFMF:");
		fmf.Load("/home/local.isima.fr/thyriarte/shared/cours/javapro/DiContainer/app/src/main/java/com/example/demo/Films.txt");
		for (Film film:fmf.getListe()) {
			System.out.println(film.print());
		}

		System.out.println("\nWMF:");
		wmf.Load("shrek");
		for (Film film:wmf.getListe()) {
			System.out.println(film.print());
		}

		System.out.println("\nML:");
		for (Film film:ml.getActorFilm("Christopher Knights")) {
			System.out.println(film.print());
		}
		*/

		////Methode 3 : Container rempli auto par scan du package

		String rootPackageName = MoviesApplication.class.getPackage().getName();
		Container conteneur =  Container.ContainerFromScan(rootPackageName);
		conteneur.bind("movieFinder", MovieLister.class, WebMovieFinder.class);

		FileMovieFinder fmf = conteneur.newInstance(FileMovieFinder.class);
		WebMovieFinder wmf = conteneur.newInstance(WebMovieFinder.class);
		MovieLister ml = conteneur.newInstance(MovieLister.class);

		System.out.println("\nFMF:");
		fmf.Load("src/main/java/com/example/demo/Films.txt");
		for (Film film:fmf.getListe()) {
			System.out.println(film.print());
		}

		System.out.println("\nWMF:");
		wmf.Load("shrek");
		for (Film film:wmf.getListe()) {
			System.out.println(film.print());
		}

		System.out.println("\nML:");
		for (Film film:ml.getActorFilm("Christopher Knights")) {
			System.out.println(film.print());
		}

	}
}
