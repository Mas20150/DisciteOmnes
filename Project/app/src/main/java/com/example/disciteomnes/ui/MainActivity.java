package com.example.disciteomnes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.disciteomnes.R;

/**
 * Die MainActivity dient als Startbildschirm der App.
 * Sie bietet zwei Optionen:
 * - Login aufrufen
 * - Registrierung starten
 */
public class MainActivity extends AppCompatActivity {

    // Buttons für Navigation
    Button btnGoToLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI-Elemente initialisieren
        btnGoToLogin    = findViewById(R.id.btnGoToLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // → Zur LoginActivity navigieren
        btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        // → Zur RegisterActivity navigieren
        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }
}
