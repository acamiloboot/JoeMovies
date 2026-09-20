package com.example.joemovies.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.joemovies.R;
import com.example.joemovies.data.FirebaseService;
import com.example.joemovies.data.model.MovieContent;

public class AddContentActivity extends AppCompatActivity {

    private EditText etTitle, etYear, etRating, etDuration, etDescription, etDirector;
    private Spinner spType, spGenre;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        etTitle = findViewById(R.id.etContentTitle);
        etYear = findViewById(R.id.etYear);
        etRating = findViewById(R.id.etRating);
        etDuration = findViewById(R.id.etDuration);
        etDescription = findViewById(R.id.etDescription);
        etDirector = findViewById(R.id.etDirector);
        spType = findViewById(R.id.spType);
        spGenre = findViewById(R.id.spGenre);
        btnSave = findViewById(R.id.btnSaveContent);

        // Configurar Spinners
        String[] types = {"Pelicula", "Serie"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(typeAdapter);

        String[] genres = {"Acción", "Sci-Fi", "Drama", "Thriller", "Comedia"};
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenre.setAdapter(genreAdapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveContent());
    }

    private void saveContent() {
        String title = etTitle.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String director = etDirector.getText().toString().trim();
        String type = spType.getSelectedItem().toString();
        String genre = spGenre.getSelectedItem().toString();

        if (title.isEmpty() || yearStr.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = Integer.parseInt(yearStr);
        double rating = Double.parseDouble(ratingStr);

        MovieContent content = new MovieContent(title, type, genre, year, rating, duration, description, director);

        btnSave.setEnabled(false);
        FirebaseService.getFirestore().collection("catalog")
                .document(content.getId())
                .set(content)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Contenido guardado en base de datos", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
