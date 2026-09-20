package com.example.joemovies.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.joemovies.MainActivity;
import com.example.joemovies.R;
import com.example.joemovies.data.FirebaseService;
import com.example.joemovies.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseService.getAuth().getCurrentUser();
            if (currentUser != null) {
                // Ir a la vista principal
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Ir al login
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 2000); // 2 segundos de splash
    }
}
