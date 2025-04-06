package com.example.assignment03.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment03.model.Movie;
import com.example.assignment03.utils.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//(3) Defining the ViewModel
// there is no change here for Assignment 03

public class MovieViewModel extends ViewModel {

    // Assignment 02 - COMP3025 - 02 - Friday 17:00 hrs
    // Susana Julia Cajavilca Turco - #200553998

    private MutableLiveData<List<Movie>> movieSearchResults = new MutableLiveData<>();
    private MutableLiveData<Movie> movieDetails = new MutableLiveData<>();

    public LiveData<List<Movie>> getMovieSearchResults() {
        return movieSearchResults;
    }

    public LiveData<Movie> getMovieDetails() {
        return movieDetails;
    }

    public void searchMovies(String query) {

        //  Declaring the  URL for the 1st search query with my apikey from OMDB: 5c7b7c4

        // initially I was using 'http'; while debugging I had to update it to 'https'
        String url = "https://www.omdbapi.com/?apikey=5c7b7c4&s=" + query + "&type=movie";


        // we have to add &type=movie because the database has movies, series, etc

        ApiClient.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                // the server is down
                Log.e("MovieViewModel", "Error: " + e.getMessage());
                movieSearchResults.postValue(null);

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                // this code was provided in class

                assert response.body() != null;
                String responseBody = response.body().string();

                try {
                    // Parse the response body into a JSONObject
                    JSONObject jsonResponse = new JSONObject(responseBody);

                    if (jsonResponse.has("Response") && jsonResponse.getString("Response").equals("False")) {
                        Log.e("MovieViewModel", "API Error: " + jsonResponse.getString("Error"));
                        movieSearchResults.postValue(null);
                        return;
                    }

                    JSONArray searchArray = jsonResponse.getJSONArray("Search");

                    // List to store movies
                    List<Movie> movieList = new ArrayList<>();

                    // Loop through the search array and create movie objects
                    for (int i = 0; i < searchArray.length(); i++) {
                        JSONObject movieObject = searchArray.getJSONObject(i);

                        // Extracting movie details
                        String title = movieObject.getString("Title");
                        String year = movieObject.getString("Year");
                        String poster = movieObject.getString("Poster");
                        String imdbID = movieObject.getString("imdbID");

                        // if there is no poster available in OMDB
                        if (poster.equals("N/A")) {
                            poster = null;  // then, we modify Glide part in MyViewHolder
                        }

                        // Creating a Movie object and add it to the list
                        Movie movie = new Movie(title, year, poster, imdbID);
                        movieList.add(movie);
                    }

                    // Posting the list of movies to LiveData to update the screen
                    movieSearchResults.postValue(movieList);

                }catch (JSONException e)

                {    throw new RuntimeException(e);

                }



            }
        });

    }

    public void fetchMovieDetails(String imdbID){


        //  Declaring the  URL for the 2nd search query with my apikey from OMDB: 5c7b7c4, and the ID of the movie

        // initially I was using 'http'; while debugging I had to update it to 'https'
        String url = "https://www.omdbapi.com/?apikey=5c7b7c4&i=" + imdbID;

        ApiClient.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                // the server is down
                Log.e("MovieViewModel", "Error: " + e.getMessage());
                movieDetails.postValue(null);

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                assert response.body() != null;
                String responseBody = response.body().string();

                try {
                    // Parse the response body into a JSONObject
                    JSONObject jsonResponse = new JSONObject(responseBody);

                    if (jsonResponse.has("Response") && jsonResponse.getString("Response").equals("False")) {
                        Log.e("MovieViewModel", "API Error: " + jsonResponse.getString("Error"));
                        movieDetails.postValue(null);
                        return;
                    }

                    // Extracting movie details from the response
                    String title = jsonResponse.getString("Title");
                    String year = jsonResponse.getString("Year");
                    String poster = jsonResponse.getString("Poster");
                    String imdbID2 = jsonResponse.getString("imdbID"); // because the argument is imdbID, I cannot use it twice
                    String metascore = jsonResponse.getString("Metascore");
                    String director = jsonResponse.getString("Director");
                    String plot = jsonResponse.getString("Plot");
                    String genre = jsonResponse.getString("Genre");
                    String runtime = jsonResponse.getString("Runtime");

                    // if there is no poster available in OMDB
                    if (poster.equals("N/A")) {
                        poster = null;   // then, we modify Glide part in MovieDetailsScreenActivity
                    }

                    // Creating a Movie object with the detailed information
                    Movie movie = new Movie(title, year, poster, imdbID2, metascore, director, plot, genre, runtime);

                    // Posting the movie details to LiveData to update the UI
                    movieDetails.postValue(movie);

                } catch (JSONException e) {

                    throw new RuntimeException(e);
                }

            }
        });




    }







}
