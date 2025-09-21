package com.example.disciteomnes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.GroupMemberResponse;
import com.example.disciteomnes.data.models.Group;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;
import com.example.disciteomnes.ui.GroupAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * DashboardActivity zeigt dem eingeloggten Benutzer:
 * - seine Gruppen (als Vorschau),
 * - Navigation zu: Gruppen, Aufgaben, Planer und Gruppen-Erstellung.
 *
 * Beim Start werden Benutzerdaten aus SharedPreferences gelesen,
 * Gruppen geladen und ein RecyclerView initialisiert.
 */
public class DashboardActivity extends AppCompatActivity {

    // UI-Elemente
    private TextView tvWelcome;
    private Button btnMyGroups, btnPlanner, btnMyTasks, btnCreateGroup, btnLogout;
    private RecyclerView recyclerGroups;

    // Adapter + Datenquelle
    private GroupAdapter adapter;
    private List<Group> groupList = new ArrayList<>();

    // API-Instanz
    private DisciteOmnesApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // ─────────── UI initialisieren ───────────
        tvWelcome      = findViewById(R.id.tvWelcome);
        btnMyGroups    = findViewById(R.id.btnMyGroups);
        btnPlanner     = findViewById(R.id.btnPlanner);
        btnMyTasks     = findViewById(R.id.btnMyTasks);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnLogout      = findViewById(R.id.btnLogout);
        recyclerGroups = findViewById(R.id.recyclerGroups);

        // ─────────── Auth-Informationen auslesen ───────────
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String jwt      = prefs.getString("access_token", null);
        String userId   = prefs.getString("user_id", null);
        String username = prefs.getString("username", null);

        if (username != null) {
            tvWelcome.setText("Willkommen, " + username + "!");
        }

        // Bei fehlender Authentifizierung zurück zum Login
        if (jwt == null || userId == null) {
            Toast.makeText(this, "Nicht angemeldet", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Retrofit-API initialisieren mit JWT
        api = DatabaseClient.getInstance(jwt).create(DisciteOmnesApi.class);

        // ─────────── Gruppenübersicht (RecyclerView) ───────────
        recyclerGroups.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter(groupList);
        recyclerGroups.setAdapter(adapter);

        // Benutzer-Gruppen laden
        loadUserGroups(userId);

        // ─────────── Navigation Buttons ───────────
        btnMyGroups.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, GroupsActivity.class)));

        btnPlanner.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, PlannerActivity.class)));

        btnMyTasks.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, TasksActivity.class)));

        btnCreateGroup.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, GroupCreateActivity.class)));

        btnLogout.setOnClickListener(v -> {
            // Auth-Infos löschen & zurück zum Login
            prefs.edit().clear().apply();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Lädt alle Gruppen, denen der aktuelle Benutzer zugeordnet ist.
     * Holt Daten aus Supabase (Tabelle group_members → groups JOIN).
     *
     * @param userId ID des aktuellen Benutzers
     */
    private void loadUserGroups(String userId) {
        // Komplette URL mit Filter und relationalem Select
        String url = "https://pruclodpzbyfoaeuefvf.supabase.co/rest/v1/group_members"
                + "?select=group:groups(id,name)&user_id=eq." + userId;

        api.getGroupsByUser(url).enqueue(new Callback<List<GroupMemberResponse>>() {
            @Override
            public void onResponse(Call<List<GroupMemberResponse>> call, Response<List<GroupMemberResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList.clear();
                    for (GroupMemberResponse gmr : response.body()) {
                        groupList.add(gmr.getGroup());
                    }
                    // Alphabetisch sortieren (case-insensitive)
                    groupList.sort(Comparator.comparing(Group::getName, String.CASE_INSENSITIVE_ORDER));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DashboardActivity.this,
                            "Laden der Gruppen fehlgeschlagen: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupMemberResponse>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this,
                        "Netzwerkfehler: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
