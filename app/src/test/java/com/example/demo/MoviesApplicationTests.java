package com.example.demo;

import com.example.demo.MovieFinders.FileMovieFinder;
import com.example.demo.MovieFinders.IMovieFinder;
import com.example.demo.MovieFinders.WebMovieFinder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class MoviesApplicationTests {

	@Test
	void CheckScan() throws Exception {

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

}
