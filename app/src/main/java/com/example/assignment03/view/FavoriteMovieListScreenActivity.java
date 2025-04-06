package com.example.assignment03.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment03.R;
import com.example.assignment03.databinding.ActivityFavoriteMovieListScreenBinding;
import com.example.assignment03.model.Movie;
import com.example.assignment03.viewmodel.MovieViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


// (g) Implementing this whole activity, reusing the movie_layout_item, the adapter and the movieclicklistener
// also setting up to get the updated Favorite Movie List after adding/deleting favorites movies

public class FavoriteMovieListScreenActivity extends AppCompatActivity implements MovieClickListener{

    ActivityFavoriteMovieListScreenBinding binding;

    List<Movie> movieList = new ArrayList<>();;

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    MovieViewModel movieViewModel;

    private FirebaseAuth mAuth; // *Initializing Firebase Authentication
    private FirebaseFirestore db; // *Initializing Firebase Firestore

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteMovieListScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();  // *Initializing Firebase Authentication
        db = FirebaseFirestore.getInstance(); // *Initializing Firebase Firestore
        FirebaseUser currentUser = mAuth.getCurrentUser();  // *getting current User


        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.findViewById(R.id.menu_search).setOnClickListener(v -> {

            // Navigating to the Search screen
            Intent favoritesIntent = new Intent(FavoriteMovieListScreenActivity.this, MovieSearchScreenActivity.class);

            startActivity(favoritesIntent);

        });

        bottomNavigationView.setSelectedItemId(R.id.menu_favorites); // highlighting the icon from the current page

        bottomNavigationView.findViewById(R.id.menu_favorites).setOnClickListener(v -> {

            // Nothing, I am already in Favorites

        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewFavorite.setLayoutManager(layoutManager);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieAdapter= new MovieAdapter(getApplicationContext(), movieList);

        binding.recyclerViewFavorite.setAdapter(movieAdapter);

        if (currentUser != null) {
            userId = currentUser.getUid();
            //fetchFavoriteMovies(userId); // anuling this line because of the onResume function
        }

        movieAdapter.setClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userId != null) {
            fetchFavoriteMovies(userId); // this is to fetch the updated list
        }
    }





    @Override
    public void onClick(View view, int adapterPosition) {

       Movie clickedMovie = movieList.get(adapterPosition);

       // Opening the MovieDetailScreenActivity with the IMDb ID
        Intent intent = new Intent(FavoriteMovieListScreenActivity.this, EditFavoriteMovieScreenActivity.class);
        intent.putExtra("IMDB_ID", clickedMovie.getImdbID());  // Pass the IMDb ID
        startActivity(intent);  // Start the detail screen activity


    }



    private void fetchFavoriteMovies(String userId) {
        db.collection("favorites")
                .document(userId)
                .collection("movies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    movieList.clear(); // important to have movie list updated
                    if (!queryDocumentSnapshots.isEmpty()) {
                        //List<Movie> movies = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Movie movie = documentSnapshot.toObject(Movie.class);
                            if (movie != null) {
                                movie.setImdbID(documentSnapshot.getId());  // Set the IMDb ID
                                movieList.add(movie);
                            }
                        }

                        movieAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the list
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FavoriteMovieListScreenActivity.this, "Error fetching favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

