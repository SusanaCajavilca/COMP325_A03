package com.example.assignment03.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment03.R;
import com.example.assignment03.databinding.ActivityMainBinding;

//(4) Defining the MyViewHolder
// there is no change here for Assignment 03

public class MyViewHolder extends RecyclerView.ViewHolder {

    // Assignment 02 - COMP3025 - 02 - Friday 17:00 hrs
    // Susana Julia Cajavilca Turco - #200553998


    TextView movieTitle;
    TextView movieYear;
    ImageView moviePoster;

    MovieClickListener movieClickListener;



    public MyViewHolder(@NonNull View itemView, MovieClickListener movieClickListener) {
        super(itemView);

        this.movieClickListener = this.movieClickListener;

        movieTitle = itemView.findViewById(R.id.titleMovie1);
        movieYear = itemView.findViewById(R.id.yearMovie1);
        moviePoster = itemView.findViewById(R.id.imagePosterMovie1);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "onViewHolder Click");
                MyViewHolder.this.movieClickListener.onClick(view, getAdapterPosition());
            }
        });
    }
}
