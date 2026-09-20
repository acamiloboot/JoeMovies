package com.example.joemovies.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.joemovies.R;
import com.example.joemovies.data.FirebaseService;
import com.example.joemovies.ui.auth.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnLogout = findViewById(R.id.btnAdminLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseService.getAuth().signOut();
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            finish();
        });

        findViewById(R.id.fabAddContent).setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AddContentActivity.class));
        });
    }
}
