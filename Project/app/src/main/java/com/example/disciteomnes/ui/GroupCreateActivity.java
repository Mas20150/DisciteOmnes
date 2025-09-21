package com.example.disciteomnes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.GroupMemberRequest;
import com.example.disciteomnes.data.dto.GroupMemberResponse;
import com.example.disciteomnes.data.dto.GroupRequest;
import com.example.disciteomnes.data.models.Group;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;
import com.example.disciteomnes.ui.GroupAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Ermöglicht das Erstellen neuer Gruppen und den Beitritt zu bestehenden Gruppen.
 * Zeigt außerdem alle Gruppen an, denen der eingeloggte Benutzer angehört.
 */
public class GroupCreateActivity extends AppCompatActivity {

    // UI-Elemente
    private EditText etGroupName, etGroupDesc, etJoinGroupId;
    private Button btnCreateGroup, btnJoinGroup, btnBackToDashboard;
    private RecyclerView recyclerView;

    // Gruppenanzeige
    private GroupAdapter adapter;
    private List<Group> groupList = new ArrayList<>();

    // API-Zugriff
    private DisciteOmnesApi api;

    // Aktuelle Benutzer-ID
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // ─────────── UI-Bindings ───────────
        etGroupName        = findViewById(R.id.etGroupName);
        etGroupDesc        = findViewById(R.id.etGroupDesc);
        etJoinGroupId      = findViewById(R.id.etJoinGroupId);
        btnCreateGroup     = findViewById(R.id.btnCreateGroup);
        btnJoinGroup       = findViewById(R.id.btnJoinGroup);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);
        recyclerView       = findViewById(R.id.recyclerViewGroups);

        // ─────────── RecyclerView für Gruppenübersicht ───────────
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter(groupList);
        recyclerView.setAdapter(adapter);

        // ─────────── Authentifizierung auslesen ───────────
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String jwt = prefs.getString("access_token", null);
        userId     = prefs.getString("user_id",      null);

        if (jwt == null || userId == null) {
            Toast.makeText(this, "Nicht angemeldet", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // API-Client mit JWT initialisieren
        api = DatabaseClient.getInstance(jwt).create(DisciteOmnesApi.class);

        // Gruppen anzeigen
        loadGroups();

        // ─────────── Button-Events ───────────
        btnBackToDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        btnCreateGroup.setOnClickListener(v -> createGroup());

        btnJoinGroup.setOnClickListener(v -> joinGroup());
    }

    /**
     * Erstellt eine neue Gruppe basierend auf dem eingegebenen Gruppennamen.
     * Nach erfolgreicher Erstellung wird der Benutzer automatisch beigetreten.
     */
    private void createGroup() {
        String name = etGroupName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Bitte Gruppennamen eingeben", Toast.LENGTH_SHORT).show();
            return;
        }

        GroupRequest request = new GroupRequest(name);
        api.addGroup(request).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Neue Gruppe direkt beitreten
                    String newGroupId = response.body().get(0).getId();
                    joinGroupWithId(newGroupId);
                } else {
                    Toast.makeText(GroupCreateActivity.this,
                            "Erstellen fehlgeschlagen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Toast.makeText(GroupCreateActivity.this,
                        "Netzwerkfehler beim Erstellen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Führt den Gruppenbeitritt basierend auf der eingegebenen Gruppen-ID durch.
     */
    private void joinGroup() {
        String grpId = etJoinGroupId.getText().toString().trim();
        if (grpId.isEmpty()) {
            Toast.makeText(this, "Bitte Gruppen-ID eingeben", Toast.LENGTH_SHORT).show();
            return;
        }
        joinGroupWithId(grpId);
    }

    /**
     * Tritt einer Gruppe anhand der ID bei.
     * Nach erfolgreichem Beitritt wird die Gruppenliste aktualisiert.
     *
     * @param groupId UUID der Gruppe
     */
    private void joinGroupWithId(String groupId) {
        GroupMemberRequest req = new GroupMemberRequest(userId, groupId);
        api.joinGroup(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                if (resp.isSuccessful() || resp.code() == 204) {
                    Toast.makeText(GroupCreateActivity.this,
                            "Beitritt erfolgreich", Toast.LENGTH_SHORT).show();
                    loadGroups();
                } else {
                    Toast.makeText(GroupCreateActivity.this,
                            "Beitreten fehlgeschlagen: " + resp.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GroupCreateActivity.this,
                        "Netzwerkfehler beim Beitreten", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Lädt alle Gruppen, in denen der Benutzer Mitglied ist, und aktualisiert das RecyclerView.
     */
    private void loadGroups() {
        String url = "https://pruclodpzbyfoaeuefvf.supabase.co/rest/v1/group_members"
                + "?select=group:groups(id,name)&user_id=eq." + userId;

        api.getGroupsByUser(url).enqueue(new Callback<List<GroupMemberResponse>>() {
            @Override
            public void onResponse(Call<List<GroupMemberResponse>> call,
                                   Response<List<GroupMemberResponse>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    groupList.clear();
                    for (GroupMemberResponse gmr : resp.body()) {
                        groupList.add(gmr.getGroup());
                    }
                    Collections.sort(groupList, Comparator.comparing(Group::getName));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroupCreateActivity.this,
                            "Fehler beim Laden der Gruppen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupMemberResponse>> call, Throwable t) {
                Toast.makeText(GroupCreateActivity.this,
                        "Netzwerkfehler beim Laden", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
