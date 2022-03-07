package com.example.demo.MovieFinders;

import com.example.demo.AuditService;
import com.example.demo.Film;
import com.example.demo.Injectable;
import com.example.demo.MovieFinders.IMovieFinder;
import com.example.demo.Scannable;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

@Scannable
public class WebMovieFinder implements IMovieFinder {
    private ArrayList<Film> liste;

    @Injectable
    private AuditService auditService;

    private String apiUrl = "https://www.omdbapi.com";
    private String apiKey = "6cb81bba";

    public WebMovieFinder(){
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

    public void addOneFromJson(JSONObject obj){
        try {
            String name = obj.getString("Title");
            String author = obj.getString("Director");
            String[] actors = obj.getString("Actors").split(", ");
            String date = obj.getString("Released");

            Film film = new Film(name, author, actors, date);
            this.liste.add(film);

            this.auditService.call();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addByDbId(String dbId){
        try {
            String url = this.apiUrl + "/?i=" + dbId + "&apikey=" + this.apiKey;
            JSONObject obj = readJsonFromUrl(url);
            addOneFromJson(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFromSearch(String search){
        try {
            String url = this.apiUrl + "/?s=" + search + "&apikey=" + this.apiKey;
            JSONObject obj = readJsonFromUrl(url);
            if(obj.toString().contains("Search")) {
                JSONArray arr = obj.getJSONArray("Search");
                for (int i = 0; i < arr.length(); i++) {
                    String film_id = arr.getJSONObject(i).getString("imdbID");
                    addByDbId(film_id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    //json
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        } finally {
            is.close();
        }
    }
}


