package com.example.joemovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joemovies.data.FirebaseService;
import com.example.joemovies.data.model.MovieContent;
import com.example.joemovies.data.repository.AuthRepository;
import com.example.joemovies.ui.adapter.MovieAdapter;
import com.example.joemovies.ui.admin.AdminActivity;
import com.example.joemovies.ui.auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AuthRepository authRepository;
    private View layoutHome, layoutSearch, layoutProfile;
    private TextView tvProfileName, tvProfileEmail;
    private RecyclerView rvCatalog, rvSearchResults;
    private MovieAdapter movieAdapter, searchAdapter;
    private List<MovieContent> movieContentList, searchList;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        authRepository = new AuthRepository();
        
        layoutHome = findViewById(R.id.layoutHome);
        layoutSearch = findViewById(R.id.layoutSearch);
        layoutProfile = findViewById(R.id.layoutProfile);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        rvCatalog = findViewById(R.id.rvCatalog);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        etSearch = findViewById(R.id.etSearch);

        movieContentList = new ArrayList<>();
        movieAdapter = new MovieAdapter(movieContentList);
        rvCatalog.setAdapter(movieAdapter);

        searchList = new ArrayList<>();
        searchAdapter = new MovieAdapter(searchList);
        rvSearchResults.setAdapter(searchAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.btnSettings).setOnClickListener(v -> toggleTheme());
        
        FirebaseUser user = FirebaseService.getAuth().getCurrentUser();
        if (user == null) {
            navigateToLogin();
            return;
        }

        if (user != null) {
            tvProfileEmail.setText(user.getEmail());
        }

        // Verificar rol y cargar catálogo
        checkUserRole(user.getUid());
        loadCatalog();

        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showView(layoutHome);
                return true;
            } else if (id == R.id.nav_search) {
                showView(layoutSearch);
                return true;
            } else if (id == R.id.nav_profile) {
                showView(layoutProfile);
                return true;
            }
            return false;
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            FirebaseService.getAuth().signOut();
            navigateToLogin();
        });
    }

    private void loadCatalog() {
        FirebaseService.getFirestore().collection("catalog")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    movieContentList.clear();
                    movieContentList.addAll(queryDocumentSnapshots.toObjects(MovieContent.class));
                    movieAdapter.notifyDataSetChanged();
                    
                    // Inicializar lista de búsqueda
                    searchList.clear();
                    searchList.addAll(movieContentList);
                    searchAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar catálogo", Toast.LENGTH_SHORT).show();
                });
    }

    private void filter(String text) {
        List<MovieContent> filteredList = new ArrayList<>();
        for (MovieContent item : movieContentList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        searchList.clear();
        searchList.addAll(filteredList);
        searchAdapter.notifyDataSetChanged();
    }

    private void toggleTheme() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "Modo Claro activado", Toast.LENGTH_SHORT).show();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(this, "Modo Oscuro activado", Toast.LENGTH_SHORT).show();
        }
    }

    private void showView(View viewToShow) {
        layoutHome.setVisibility(View.GONE);
        layoutSearch.setVisibility(View.GONE);
        layoutProfile.setVisibility(View.GONE);
        viewToShow.setVisibility(View.VISIBLE);
    }

    private void checkUserRole(String uid) {
        authRepository.getUserRole(uid, new AuthRepository.RoleCallback() {
            @Override
            public void onRoleFetched(String role) {
                if ("ADMIN".equals(role)) {
                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Bienvenido a StreamVault", Toast.LENGTH_SHORT).show();
                    fetchUserProfile(uid);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "Error al verificar permisos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserProfile(String uid) {
        FirebaseService.getFirestore().collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        tvProfileName.setText(name);
                    }
                });
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
