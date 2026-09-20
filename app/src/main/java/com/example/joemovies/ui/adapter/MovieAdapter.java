package com.example.joemovies.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.joemovies.R;
import com.example.joemovies.data.model.MovieContent;
import com.example.joemovies.ui.DetailActivity;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<MovieContent> movieList;

    public MovieAdapter(List<MovieContent> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieContent movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvRating.setText("⭐ " + movie.getRating());
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("type", movie.getType());
            intent.putExtra("genre", movie.getGenre());
            intent.putExtra("description", movie.getDescription());
            intent.putExtra("director", movie.getDirector());
            intent.putExtra("duration", movie.getDuration());
            intent.putExtra("year", movie.getYear());
            intent.putExtra("rating", movie.getRating());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRating;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvRating = itemView.findViewById(R.id.tvMovieRating);
        }
    }
}
