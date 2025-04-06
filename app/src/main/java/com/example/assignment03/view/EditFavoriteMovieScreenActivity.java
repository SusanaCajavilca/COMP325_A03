package com.example.assignment03.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.assignment03.R;
import com.example.assignment03.databinding.ActivityEditFavoriteMovieScreenBinding;
import com.example.assignment03.model.Movie;
import com.example.assignment03.viewmodel.MovieViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// (h) Implementing this whole activity, including delete and update functions

public class EditFavoriteMovieScreenActivity extends AppCompatActivity {

    ActivityEditFavoriteMovieScreenBinding binding;

    MovieViewModel movieViewModel;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Movie currentMovie;

    Button goBack;

    Button delete;

    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditFavoriteMovieScreenBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //*Initializing Firebase Authentication and Firebase Firestore
        mAuth = FirebaseAuth.getInstance();  //  *adding this
        db = FirebaseFirestore.getInstance(); // *adding this



        //* getting current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String imdbID = getIntent().getStringExtra("IMDB_ID");

        if (imdbID != null && currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("favorites")
                    .document(userId)
                    .collection("movies")
                    .document(imdbID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Loading plot from Firestore Database
                            String storedPlot = documentSnapshot.getString("plot");
                            binding.plotMovie3.setText(storedPlot != null ? storedPlot : "");

                            Log.d("EditFavMovie", "Loaded Firestore plot: " + storedPlot);
                        } else {
                            Log.d("EditFavMovie", "No document found for this IMDb ID.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("EditFavMovie", "Failed to load movie from Firestore", e);
                    });
        }

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getMovieDetails().observe(this, movie -> {
            Log.d("FavoriteMovieDetailsScreen", "Movie Details: " + movie.getTitle());
            if (movie != null) {
                // Updating UI with movie details
                binding.titleMovie3.setText(movie.getTitle());
                binding.yearMovie3.setText(movie.getYear());
                binding.metascoreMovie3.setText(movie.getMetascore());
                binding.directorMovie3.setText(movie.getDirector());
                //binding.plotMovie3.setText(movie.getPlot()); // plot is from the database since it is the only editable field
                binding.genreMovie3.setText(movie.getGenre());
                binding.runtimeMovie3.setText(movie.getRuntime());

                // Load movie poster using Glide
                Glide.with(EditFavoriteMovieScreenActivity.this)
                        .load(movie.getPoster() != null ? movie.getPoster() : R.drawable.no_poster_available)
                        .into(binding.imagePosterMovie3);

                currentMovie = movie; // *adding this
            }
            else {
                Log.e("FavoriteMovieDetailsScreen", "Movie details not found");
            }

        });

        // Fetch the movie details using the IMDb ID
        if (imdbID != null) {
            movieViewModel.fetchMovieDetails(imdbID);
        }

        goBack = binding.backFavButton;
        delete = binding.deleteFavButton;
        update = binding.updateFavButton;

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentMovie != null && currentMovie.getImdbID() != null) {
                    String userId = currentUser.getUid();
                    db.collection("favorites")
                            .document(userId)
                            .collection("movies")
                            .document(currentMovie.getImdbID()) // Delete the movie by IMDb ID
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(EditFavoriteMovieScreenActivity.this, "Movie deleted successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity after deleting
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(EditFavoriteMovieScreenActivity.this, "Error deleting movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(EditFavoriteMovieScreenActivity.this, "Error: Movie data is missing", Toast.LENGTH_SHORT).show();
                }

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentMovie != null) {
                    // Getting updated plot from UI (only the plot is editable)
                    String updatedPlot = binding.plotMovie3.getText().toString();

                    // Creating a map with only the updated data (plot in this case)
                    Map<String, Object> updatedMovie = new HashMap<>();
                    updatedMovie.put("plot", updatedPlot); // Only update the plot field

                    // Updating the movie details in Firestore
                    String userId = currentUser.getUid();
                    db.collection("favorites")
                            .document(userId)
                            .collection("movies")
                            .document(currentMovie.getImdbID()) // Update by IMDb ID
                            .update(updatedMovie) // Use update to modify the plot field
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(EditFavoriteMovieScreenActivity.this, "Movie updated successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(EditFavoriteMovieScreenActivity.this, "Error updating movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(EditFavoriteMovieScreenActivity.this, "Error: Movie data is missing", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }





}