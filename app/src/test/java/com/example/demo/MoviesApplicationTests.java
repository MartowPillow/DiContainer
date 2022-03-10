package com.example.demo;

import com.example.demo.MovieFinders.FileMovieFinder;
import com.example.demo.MovieFinders.IMovieFinder;
import com.example.demo.MovieFinders.WebMovieFinder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class MoviesApplicationTests {

	@Test
	void CheckScanContainer() throws Exception {

		String rootPackageName = MoviesApplication.class.getPackage().getName();
		Container conteneur =  Container.ContainerFromScan(rootPackageName);

		Set<Object> instances = conteneur.getInstances();
		Set<Object> classes = new HashSet<>();
		for (Object instance:instances.toArray()) {
			classes.add(instance.getClass());
		}

		assert classes.size() == 4;
		assert classes.contains(AuditService.class);
		assert classes.contains(WebMovieFinder.class);
		assert classes.contains(FileMovieFinder.class);
		assert classes.contains(MovieLister.class);
	}

	@Test
	void CheckManualContainer() throws Exception {
		Set<Class<?>> movieClasses = new HashSet<>();
		movieClasses.add(FileMovieFinder.class);
		movieClasses.add(WebMovieFinder.class);

		Container conteneurManuel = new Container(movieClasses);

		conteneurManuel.register(AuditService.class);
		conteneurManuel.register(MovieLister.class);

		Set<Object> instances = conteneurManuel.getInstances();
		Set<Object> classes = new HashSet<>();
		for (Object instance:instances.toArray()) {
			classes.add(instance.getClass());
		}

		assert classes.size() == 4;
		assert classes.contains(AuditService.class);
		assert classes.contains(WebMovieFinder.class);
		assert classes.contains(FileMovieFinder.class);
		assert classes.contains(MovieLister.class);

	}

	@Test
	void CheckBinding() throws Exception {
		Container conteneur = new Container();
		conteneur.register(MovieLister.class);
		conteneur.register(WebMovieFinder.class);
		conteneur.register(FileMovieFinder.class);

		conteneur.bind("movieFinder", MovieLister.class, FileMovieFinder.class);
		MovieLister movieLister = conteneur.newInstance(MovieLister.class);
		assert movieLister.getMovieFinder() instanceof FileMovieFinder;

		conteneur.bind("movieFinder", MovieLister.class, WebMovieFinder.class);
		MovieLister movieListerDeux = conteneur.newInstance(MovieLister.class);
		assert movieListerDeux.getMovieFinder() instanceof WebMovieFinder;
	}

	@Test
	void CheckFileMovieFinder() throws Exception {
		AuditService as = new AuditService();
		FileMovieFinder fmf = new FileMovieFinder();
		fmf.setAuditService(as);

		fmf.Load("src/main/java/com/example/demo/Films.txt");
		ArrayList<Film> films = fmf.getListe();

		System.out.println("\nFMF:");
		for (Film film:fmf.getListe()) {
			System.out.println(film.print());
		}

		assert films.size() == 2;
		assert films.get(0).getName().equals("Name");
	}

	@Test
	void CheckWebMovieFinder() throws Exception {
		AuditService as = new AuditService();
		WebMovieFinder wmf = new WebMovieFinder();
		wmf.setAuditService(as);

		wmf.Load("Shrek");
		ArrayList<Film> films = wmf.getListe();

		System.out.println("\nWMF:");
		boolean flag = true;
		for (Film film:wmf.getListe()) {
			if(!film.getName().contains("Shrek"))
				flag = false;
			System.out.println(film.print());
		}

		assert films.size() > 0;
		assert flag;
	}

	@Test
	void CheckMovieLister() throws Exception {
		String rootPackageName = MoviesApplication.class.getPackage().getName();
		Container conteneur =  Container.ContainerFromScan(rootPackageName);

		conteneur.bind("movieFinder", FileMovieFinder.class, MovieLister.class);
		MovieLister ml = conteneur.newInstance(MovieLister.class);
		ml.getMovieFinder().Load("src/main/java/com/example/demo/Films.txt");

		System.out.println("\nML:");
		boolean flag = true;
		for (Film film:ml.getActorFilm("Acteur2")) {
			if(!Arrays.asList(film.getActors()).contains("Acteur2"))
				flag = false;
			System.out.println(film.print());
		}

		ArrayList<Film> films = ml.getListe();

		assert films.size() > 0;
		assert flag;
	}


}
