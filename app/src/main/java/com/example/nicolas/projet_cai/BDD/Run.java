package com.example.nicolas.projet_cai.BDD;

import java.util.Date;

/**
 * Created by Nicolas on 03/04/2018.
 */


public class Run {

    private int id;
    private String name;
    private Date date;
    private String login;
    private double distance;
    private double vitesse;

    public int getId() {return id;}
    public String getName() {return name;}
    public Date getDate() {return date;}
    public String getLogin() {return login;}
    public double getDistance() {
        return distance;
    }
    public double getVitesse() {
        return vitesse;
    }

    public Run(int id, String name, Date date, String login, double distance, double vitesse) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.login = login;
        this.distance = distance;
        this.vitesse = vitesse;
    }

    public Run(String name, String login, double distance){
        this.name=name;
        this.login=login;
        this.distance=distance;
        //this.vitesse=vitesse;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
    }



}
