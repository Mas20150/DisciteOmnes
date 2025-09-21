package com.example.disciteomnes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.GroupMemberResponse;
import com.example.disciteomnes.data.models.Group;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Diese Activity zeigt dem Benutzer alle Gruppen an, denen er zugeordnet ist.
 * Die Gruppen werden aus Supabase geladen und alphabetisch dargestellt.
 * Zudem gibt es einen Zurück-Button zur DashboardActivity.
 */
public class GroupsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GroupAdapter adapter;
    private List<Group> groupList = new ArrayList<>();

    private DisciteOmnesApi api;
    private Button btnBackToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        // ───────────── UI-Elemente initialisieren ─────────────
        recyclerView = findViewById(R.id.recyclerViewGroups);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter(groupList);
        recyclerView.setAdapter(adapter);

        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);

        // Zurück zur DashboardActivity
        btnBackToDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        // ───────────── Authentifizierungsdaten prüfen ─────────────
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String jwt    = prefs.getString("access_token", null);
        String userId = prefs.getString("user_id", null);

        if (jwt == null || userId == null) {
            Toast.makeText(this, "Nicht angemeldet", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Retrofit-Instanz mit JWT-Header
        api = DatabaseClient.getInstance(jwt).create(DisciteOmnesApi.class);

        // URL zur Abfrage aller Gruppen des Benutzers (JOIN über group_members → groups)
        String url = "https://pruclodpzbyfoaeuefvf.supabase.co/rest/v1/group_members"
                + "?select=group:groups(id,name)&user_id=eq." + userId;

        // ───────────── Gruppen von Supabase laden ─────────────
        api.getGroupsByUser(url).enqueue(new Callback<List<GroupMemberResponse>>() {
            @Override
            public void onResponse(Call<List<GroupMemberResponse>> call, Response<List<GroupMemberResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList.clear();
                    for (GroupMemberResponse gmr : response.body()) {
                        groupList.add(gmr.getGroup());
                    }
                    // Alphabetisch sortieren (nach Gruppennamen)
                    Collections.sort(groupList, Comparator.comparing(Group::getName));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroupsActivity.this, "Fehler: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupMemberResponse>> call, Throwable t) {
                Toast.makeText(GroupsActivity.this, "Netzwerkfehler", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
