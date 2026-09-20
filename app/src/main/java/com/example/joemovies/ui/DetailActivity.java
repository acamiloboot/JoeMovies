package com.example.joemovies.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.joemovies.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

        // Recibir datos
        String title = getIntent().getStringExtra("title");
        String type = getIntent().getStringExtra("type");
        String genre = getIntent().getStringExtra("genre");
        String description = getIntent().getStringExtra("description");
        String director = getIntent().getStringExtra("director");
        String duration = getIntent().getStringExtra("duration");
        int year = getIntent().getIntExtra("year", 2024);
        double rating = getIntent().getDoubleExtra("rating", 0.0);

        // Vincular vistas
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvType = findViewById(R.id.tvDetailType);
        TextView tvRating = findViewById(R.id.tvDetailRating);
        TextView tvYear = findViewById(R.id.tvDetailYear);
        TextView tvDuration = findViewById(R.id.tvDetailDuration);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);
        TextView tvDirector = findViewById(R.id.tvDetailDirector);
        Button btnPlay = findViewById(R.id.btnPlay);

        // Mostrar datos
        tvTitle.setText(title);
        tvType.setText(type != null ? type.toUpperCase() : "CONTENIDO");
        tvRating.setText("⭐ " + rating);
        tvYear.setText(String.valueOf(year));
        tvDuration.setText(duration);
        tvDescription.setText(description);
        tvDirector.setText("Director: " + director);

        btnPlay.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando reproducción de: " + title, Toast.LENGTH_SHORT).show();
        });
    }
}
