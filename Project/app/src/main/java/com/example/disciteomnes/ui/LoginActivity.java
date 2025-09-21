package com.example.disciteomnes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.GroupMemberResponse;
import com.example.disciteomnes.data.dto.LoginRequest;
import com.example.disciteomnes.data.dto.LoginResponse;
import com.example.disciteomnes.data.models.Profile;
import com.example.disciteomnes.network.AuthClient;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Diese Activity verwaltet den Login-Vorgang:
 * 1. Login über Supabase Auth (E-Mail + Passwort)
 * 2. Abruf von User-Daten (user_id)
 * 3. Profil-Erstellung bei Bedarf (pending_name aus Registrierung)
 * 4. Speichern von Token, user_id und optional group_id
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button   btnLogin, btnToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI-Elemente binden
        etEmail       = findViewById(R.id.etEmail);
        etPassword    = findViewById(R.id.etPassword);
        btnLogin      = findViewById(R.id.btnLoginUser);
        btnToRegister = findViewById(R.id.btnToRegister);

        // Login-Button
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Zur Registrierung weiterleiten
        btnToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    /**
     * Führt den Login durch:
     * - sendet LoginRequest an Supabase Auth
     * - ruft bei Erfolg das JWT ab
     */
    private void attemptLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Bitte alle Felder ausfüllen");
            return;
        }

        DisciteOmnesApi authApi = AuthClient
                .getInstance() // Nur mit apikey
                .create(DisciteOmnesApi.class);

        authApi.login(new LoginRequest(email, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> resp) {
                        if (!resp.isSuccessful() || resp.body() == null) {
                            showToast("Login fehlgeschlagen: " + resp.code());
                            return;
                        }

                        String jwt = resp.body().getAccessToken();
                        fetchUserDetails(jwt);
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        showToast("Netzwerkfehler: " + t.getMessage());
                    }
                });
    }

    /**
     * Holt die user_id via `auth/v1/user` und speichert sie.
     * Falls pending_name vorhanden ist → Profil erstellen.
     * Danach → group_id abfragen und speichern.
     */
    private void fetchUserDetails(String jwt) {
        DisciteOmnesApi api = DatabaseClient
                .getInstance(jwt) // apikey + Authorization: Bearer jwt
                .create(DisciteOmnesApi.class);

        api.getUser().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resp) {
                if (!resp.isSuccessful() || resp.body() == null) {
                    showToast("Fehler beim Laden des Benutzers");
                    return;
                }

                try {
                    JSONObject userJson = new JSONObject(resp.body().string());
                    String userId = userJson.getString("id");

                    // SharedPreferences vorbereiten
                    SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                    final SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("access_token", jwt);
                    editor.putString("user_id", userId);

                    // Profil erstellen, falls "pending_name" aus Registrierung vorhanden ist
                    String pendingName = prefs.getString("pending_name", null);
                    if (pendingName != null) {
                        Profile profile = new Profile(userId, pendingName);
                        api.createProfile(profile).enqueue(new Callback<Profile>() {
                            @Override
                            public void onResponse(Call<Profile> c, Response<Profile> r) {
                                Log.d("Profile", "Profil gespeichert");
                            }

                            @Override
                            public void onFailure(Call<Profile> c, Throwable t) {
                                Log.e("Profile", "Fehler beim Speichern: " + t.getMessage());
                            }
                        });
                        editor.remove("pending_name");
                    }

                    // Gruppeninformationen laden
                    fetchGroupId(api, userId, editor);

                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Fehler beim Verarbeiten des Benutzers");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("Verbindungsfehler: " + t.getMessage());
            }
        });
    }

    /**
     * Fragt die erste Gruppe des Benutzers ab und speichert deren ID.
     * Danach wird zur DashboardActivity navigiert.
     */
    private void fetchGroupId(DisciteOmnesApi api, String userId, SharedPreferences.Editor editor) {
        String url = "rest/v1/group_members?select=group:groups(id,name)&user_id=eq." + userId;

        api.getGroupsByUser(url).enqueue(new Callback<List<GroupMemberResponse>>() {
            @Override
            public void onResponse(Call<List<GroupMemberResponse>> call, Response<List<GroupMemberResponse>> resp) {
                if (resp.isSuccessful() && resp.body() != null && !resp.body().isEmpty()) {
                    String groupId = resp.body().get(0).getGroupId();
                    editor.putString("group_id", groupId);
                } else {
                    Log.w("Login", "Keine Gruppe gefunden");
                }

                // Alle Änderungen speichern und weiterleiten
                editor.apply();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<List<GroupMemberResponse>> call, Throwable t) {
                editor.apply(); // editor trotzdem speichern
                showToast("Fehler beim Laden der Gruppe: " + t.getMessage());
            }
        });
    }

    /**
     * Zeigt eine Toast-Nachricht an.
     *
     * @param msg Die Nachricht, die angezeigt werden soll
     */
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
