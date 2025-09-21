package com.example.disciteomnes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.RegisterRequest;
import com.example.disciteomnes.network.AuthClient;
import com.example.disciteomnes.network.DisciteOmnesApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Diese Activity ermöglicht neuen Benutzern die Registrierung per E-Mail und Passwort.
 * Nach erfolgreicher Registrierung wird der Benutzer zur LoginActivity weitergeleitet.
 * Der angegebene Name wird in SharedPreferences zwischengespeichert, damit beim Login
 * automatisch ein Profil angelegt werden kann.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnRegisterUser, btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ───────────── UI-Elemente binden ─────────────
        etName          = findViewById(R.id.etName);
        etEmail         = findViewById(R.id.etEmail);
        etPassword      = findViewById(R.id.etPassword);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);
        btnBackToLogin  = findViewById(R.id.btnBackToLogin);

        // Registrierung auslösen
        btnRegisterUser.setOnClickListener(v -> {
            String name     = etName.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validierung
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Bitte alle Felder ausfüllen");
                return;
            }

            RegisterRequest authReq = new RegisterRequest(email, password);

            // Supabase Auth-API
            DisciteOmnesApi authApi = AuthClient.getInstance().create(DisciteOmnesApi.class);

            authApi.register(authReq).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // ✅ Name temporär speichern (für Profil-Erstellung in LoginActivity)
                        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                        prefs.edit().putString("pending_name", name).apply();

                        showToast("✅ Registrierung erfolgreich!\nBitte E-Mail bestätigen.");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        // Fehler aus Supabase analysieren
                        try {
                            String err = response.errorBody() != null ? response.errorBody().string() : "";
                            if (err.contains("password")) {
                                showToast("⚠️ Passwort ist zu leicht (mind. 6 Zeichen)");
                            } else if (err.contains("email")) {
                                showToast("⚠️ Diese E-Mail ist bereits registriert");
                            } else {
                                showToast("❌ Registrierung fehlgeschlagen: " + response.code());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            showToast("Unbekannter Fehler bei Registrierung");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Verbindungsfehler: " + t.getMessage());
                }
            });
        });

        // Zurück zur LoginActivity
        btnBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    /**
     * Zeigt eine Toast-Nachricht im UI.
     *
     * @param msg Die anzuzeigende Nachricht
     */
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
