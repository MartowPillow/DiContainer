package com.example.demo;

public class AuditService {

    private int count;

    public AuditService(){
        this.count = 0;
    }

    public void call(){
        this.count++;
    }

    public int getCount(){
        return count;
    }

}
