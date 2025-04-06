package com.example.assignment03.model;

// (2) Defining the Movie Model
// there is no change here for Assignment 03

public class Movie {

    // Assignment 03 - COMP3025 - 02 - Friday 17:00 hrs
    // Susana Julia Cajavilca Turco - #200553998

    // The first 4 attributes will be populated with the first API call
    private String title;
    private String year;       // Will be populated with the movie year
    private String poster;     // Will be populated with the movie poster URL
    private String imdbID;     // Will be populated with the movie ID

    // The last 5 attributes will be populated with the second API call
    // that is why by default are null
    private String genre = null;
    private String metascore = null;
    private String director = null;

    private String runtime = null;
    private String plot = null;


    // Default constructor (no-argument constructor)
    public Movie() {
    }

    // Constructor for search results (first 4 attributes)
    public Movie(String title, String year, String poster, String imdbID) {
        this.title = title;
        this.year = year;
        this.poster = poster;
        this.imdbID = imdbID;
    }

    // Constructor for detailed movie info (all 9 attributes)
    public Movie(String title, String year, String poster, String imdbID, String metascore, String director,
                 String plot, String genre, String runtime) {
        this.title = title;
        this.year = year;
        this.poster = poster;
        this.imdbID = imdbID;
        this.genre = genre;
        this.metascore = metascore;
        this.director = director;
        this.runtime = runtime;
        this.plot = plot;

    }

    // Getters and Setters
    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getYear() {

        return year;
    }

    public void setYear(String year) {

        this.year = year;
    }

    public String getPoster() {

        return poster;
    }

    public void setPoster(String poster) {

        this.poster = poster;
    }

    public String getImdbID() {

        return imdbID;
    }

    public void setImdbID(String imdbID) {

        this.imdbID = imdbID;
    }

    public String getMetascore() {

        return metascore;
    }

    public void setMetascore(String metascore) {

        this.metascore = metascore;
    }

    public String getDirector() {

        return director;
    }

    public void setDirector(String director) {

        this.director = director;
    }

    public String getPlot() {

        return plot;
    }

    public void setPlot(String plot) {

        this.plot = plot;
    }

    public String getGenre() {

        return genre;
    }

    public void setGenre(String genre) {

        this.genre = genre;
    }

    public String getRuntime() {

        return runtime;
    }

    public void setRuntime(String runtime) {

        this.runtime = runtime;
    }
}
