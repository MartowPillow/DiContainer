package com.example.demo.MovieFinders;

import com.example.demo.AuditService;
import com.example.demo.Film;

import java.util.ArrayList;

public interface IMovieFinder {
    public ArrayList<Film> getListe();

    public AuditService getAuditService();
    public void setAuditService(AuditService auditService);

    public void Load(String input);

}
