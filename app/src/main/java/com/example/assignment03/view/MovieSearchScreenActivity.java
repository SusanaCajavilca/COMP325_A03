package com.example.assignment03.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment03.R;
import com.example.assignment03.databinding.ActivityMainBinding;
import com.example.assignment03.model.Movie;
import com.example.assignment03.viewmodel.MovieViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


//(7) the last pages to implement are the activities
//(e) Adding the mAuth code and the bottom navigation menu here

public class MovieSearchScreenActivity extends AppCompatActivity implements MovieClickListener {

    // Assignment 02 - COMP3025 - 02 - Friday 17:00 hrs
    // Susana Julia Cajavilca Turco - #200553998


    //1st page
    ActivityMainBinding binding;

    private FirebaseAuth mAuth;

    List<Movie> movieList = new ArrayList<>();;

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    MovieViewModel movieViewModel;

    EditText searchEditText;
    Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot()); // this replace -> setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentuser = mAuth.getCurrentUser();

        if (currentuser == null) {
            // If user is not logged in, redirecting to login screen activity
            Intent intent = new Intent(MovieSearchScreenActivity.this, LoginScreenActivity.class);
            startActivity(intent);
            finish();
        }

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.setSelectedItemId(R.id.menu_search);

        bottomNavigationView.findViewById(R.id.menu_search).setOnClickListener(v -> {
            // Nothing, I am already in Search

        });

        bottomNavigationView.findViewById(R.id.menu_favorites).setOnClickListener(v -> {
            // Navigate to the Favorites screen

            Intent favoritesIntent = new Intent(MovieSearchScreenActivity.this, FavoriteMovieListScreenActivity.class);
            
            startActivity(favoritesIntent);
        });





        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleViewMovie.setLayoutManager(layoutManager);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieAdapter= new MovieAdapter(getApplicationContext(), movieList);

        binding.recycleViewMovie.setAdapter(movieAdapter);




        movieViewModel.getMovieSearchResults().observe(this, movies -> {
            if (movies != null) {
                movieList.clear();
                movieList.addAll(movies);
                movieAdapter.notifyDataSetChanged(); // <- this line was added while debugging
            }
        });

        searchButton = binding.searchButton;
       searchEditText = binding.enterMovieText;

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                movieViewModel.searchMovies(query);  // Call ViewModel to search movies
            }
            else{
                Toast.makeText(MovieSearchScreenActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
            }
        });

        movieAdapter.setClickListener(this);

    }

    @Override
    public void onClick(View view, int adapterPosition) {

        Movie clickedMovie = movieList.get(adapterPosition);

        // Open the MovieDetailScreenActivity with the IMDb ID
        Intent intent = new Intent(MovieSearchScreenActivity.this, MovieDetailsScreenActivity.class);
        intent.putExtra("IMDB_ID", clickedMovie.getImdbID());  // Pass the IMDb ID
        startActivity(intent);  // Start the detail screen activity

    }
}