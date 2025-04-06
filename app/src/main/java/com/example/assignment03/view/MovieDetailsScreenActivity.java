package com.example.assignment03.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.assignment03.R;
import com.example.assignment03.databinding.ActivityMainBinding;
import com.example.assignment03.databinding.ActivityMovieDetailsScreenBinding;
import com.example.assignment03.model.Movie;
import com.example.assignment03.viewmodel.MovieViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


//(8) the last pages to implement are the activities
// (f) Adding FirebaseAuth and FirebaseFirestore and Implementing the add to Fav button /
// I had to create an instance of Movie to store the results and paste it to the database
// also, I had to implement a way to avoid duplicated favorite movies

public class MovieDetailsScreenActivity extends AppCompatActivity {

    // Assignment 02 - COMP3025 - 02 - Friday 17:00 hrs
    // Susana Julia Cajavilca Turco - #200553998

    //2nd page

    // While debugging I realized that each binding name only works for one layout
    ActivityMovieDetailsScreenBinding binding;

    MovieViewModel movieViewModel;

    Button goBack;

    Button addToFav;

    private FirebaseAuth mAuth; // *adding this
    private FirebaseFirestore db; // *adding this

    private Movie currentMovie; // *adding this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieDetailsScreenBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());  // this replace -> setContentView(R.layout.activity_movie_details_screen);

        //*Initializing Firebase Authentication and Firebase Firestore
        mAuth = FirebaseAuth.getInstance();  //  *adding this
        db = FirebaseFirestore.getInstance(); // *adding this

        //* getting current user
        FirebaseUser currentUser = mAuth.getCurrentUser();



        String imdbID = getIntent().getStringExtra("IMDB_ID");

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getMovieDetails().observe(this, movie -> {
            Log.d("MovieDetailsScreen", "Movie Details: " + movie.getTitle());
            if (movie != null) {
                // Updating UI with movie details
                binding.titleMovie2.setText(movie.getTitle());
                binding.yearMovie2.setText(movie.getYear());
                binding.metascoreMovie.setText(movie.getMetascore());
                binding.directorMovie.setText(movie.getDirector());
                binding.plotMovie.setText(movie.getPlot());
                binding.genreMovie.setText(movie.getGenre());
                binding.runtimeMovie.setText(movie.getRuntime());

                // Load movie poster using Glide
                Glide.with(MovieDetailsScreenActivity.this)
                        .load(movie.getPoster() != null ? movie.getPoster() : R.drawable.no_poster_available)
                        .into(binding.imagePosterMovie2);

                currentMovie = movie; // *adding this
            }
            else {
                Log.e("MovieDetailsScreen", "Movie details not found");
            }

        });

        // Fetching the movie details using the IMDb ID
        if (imdbID != null) {
            movieViewModel.fetchMovieDetails(imdbID);
        }

        goBack = binding.returnButton;

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //* implementing the Add to Favorites button functionality
        addToFav = binding.addToFavButton;

        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser == null) {
                    Toast.makeText(MovieDetailsScreenActivity.this, "You must be logged in to add favorites.", Toast.LENGTH_SHORT).show();
                    return;
                }


                String userId = currentUser.getUid();

                db.collection("favorites")
                        .document(userId)
                        .collection("movies")
                        .document(imdbID)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // if the Movie already exists in favorites list of the user
                                Toast.makeText(MovieDetailsScreenActivity.this, "Movie is already in your favorites!", Toast.LENGTH_SHORT).show();
                            } else {
                                // if the Movie does not exists in favorites, then add it
                                String title = currentMovie.getTitle();
                                String year = currentMovie.getYear();
                                String poster = currentMovie.getPoster();
                                String metascore = currentMovie.getMetascore();
                                String director = currentMovie.getDirector();
                                String plot = currentMovie.getPlot();
                                String genre = currentMovie.getGenre();
                                String runtime = currentMovie.getRuntime();

                                Map<String, Object> movieData = new HashMap<>();
                                movieData.put("title", title);
                                movieData.put("year", year);
                                movieData.put("poster", poster);
                                movieData.put("metascore", metascore);
                                movieData.put("director", director);
                                movieData.put("plot", plot);
                                movieData.put("genre", genre);
                                movieData.put("runtime", runtime);

                                // Add the movie to the favorites collection
                                db.collection("favorites")
                                        .document(userId)
                                        .collection("movies")
                                        .document(imdbID)
                                        .set(movieData)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(MovieDetailsScreenActivity.this, "Movie added to favorites!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(MovieDetailsScreenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("FIREBASE_ERROR", "Error saving movie", e);
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handling any errors that may occur during the check
                            Toast.makeText(MovieDetailsScreenActivity.this, "Error checking for existing movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FIREBASE_ERROR", "Error checking for existing movie", e);
                        });



            }

        });



    }
}