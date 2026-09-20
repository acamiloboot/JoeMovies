package com.example.joemovies.data.model;

import java.util.UUID;

public class MovieContent {
    private String id;
    private String title;
    private String type; // "Pelicula" o "Serie"
    private String genre;
    private int year;
    private double rating;
    private String duration;
    private String description;
    private String director;

    public MovieContent() {
        // Requerido para Firestore
    }

    public MovieContent(String title, String type, String genre, int year, double rating, String duration, String description, String director) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.type = type;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.duration = duration;
        this.description = description;
        this.director = director;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
}
