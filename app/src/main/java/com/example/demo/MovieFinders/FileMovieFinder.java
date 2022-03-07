package com.example.demo.MovieFinders;

import com.example.demo.AuditService;
import com.example.demo.Film;
import com.example.demo.Injectable;
import com.example.demo.Scannable;

import java.io.*;
import java.util.ArrayList;

@Scannable
public class FileMovieFinder implements IMovieFinder {
    private ArrayList<Film> liste;

    @Injectable
    private AuditService auditService;

    public FileMovieFinder(){
        this.liste = new ArrayList<Film>();
    }

    public AuditService getAuditService(){
        return  this.auditService;
    }
    public void setAuditService(AuditService auditService){
        this.auditService = auditService;
    }

    public ArrayList<Film> getListe(){
        return this.liste;
    }

    //file line = Name;Author;Actor1;Actor2;ActorN;Date
    public void findFromFile(String filename){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String currentLine = reader.readLine();
            while(currentLine != null) {

                String[] ligne = currentLine.split(";");
                int nb_fields = ligne.length;

                if (nb_fields >= 4){
                    String name = ligne[0];
                    String author = ligne[1];
                    String[] actors = new String[nb_fields - 3];
                    for(int i = 2; i <= nb_fields - 2; i++){
                        actors[i-2]=ligne[i];
                    }
                    String date = ligne[nb_fields-1];

                    Film film = new Film(name, author, actors, date);
                    this.liste.add(film);
                }

                currentLine = reader.readLine();
            }
            reader.close();
            this.auditService.call();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
