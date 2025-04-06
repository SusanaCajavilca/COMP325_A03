package com.example.assignment03.view;

//(6) Defining the Adapter, which is the connection between Movie and the RecycleView
// there is no change here for Assignment 03

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment03.R;
import com.example.assignment03.databinding.ActivityMainBinding;
import com.example.assignment03.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MyViewHolder> {

    // Assignment 02 - COMP3025 - 02 - Friday 17:00 hrs
    // Susana Julia Cajavilca Turco - #200553998


    List<Movie> movieList;

    Context context;

    public MovieClickListener movieClickListener;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movieList = movies;
    }

    public void setClickListener(MovieClickListener myListener){
        this.movieClickListener = myListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, this.movieClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Movie movie = movieList.get(position);

        holder.movieTitle.setText(movie.getTitle());
        holder.movieYear.setText(movie.getYear());
        Glide.with(holder.itemView.getContext())
                .load(movie.getPoster() != null ? movie.getPoster() : R.drawable.no_poster_available)
                //.load(movie.getPoster())
                .into(holder.moviePoster);

        // the following 2 lines were added while debugging the 2nd activity
        if (movieClickListener != null) {
            holder.itemView.setOnClickListener(v -> movieClickListener.onClick(v, position));
        }

    }

    @Override
    public int getItemCount() {

        return movieList.size();
    }
}
