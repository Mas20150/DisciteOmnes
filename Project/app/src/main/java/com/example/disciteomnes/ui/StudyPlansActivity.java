// StudyPlansActivity.java
package com.example.disciteomnes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.GroupMemberResponse;
import com.example.disciteomnes.data.models.StudyPlan;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Diese Activity zeigt eine Liste von Lernplänen für Gruppen,
 * denen der aktuell eingeloggte Benutzer angehört.
 */
public class StudyPlansActivity extends AppCompatActivity {

    private Spinner spinnerGroups;                      // Dropdown-Menü zur Gruppenauswahl
    private RecyclerView recyclerView;                  // RecyclerView zur Anzeige der StudyPlans
    private StudyPlanAdapter adapter;                   // Adapter für RecyclerView
    private Button btnBackToDashboard = findViewById(R.id.btnBackToDashboard);  // Zurück-Button

    private final List<String> groupIds   = new ArrayList<>();  // Liste von Gruppen-IDs
    private final List<String> groupNames = new ArrayList<>();  // Liste von Gruppen-Namen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_plans);

        // UI-Elemente referenzieren
        spinnerGroups = findViewById(R.id.spinnerGroups);
        recyclerView  = findViewById(R.id.studyPlanRecycler);

        // RecyclerView konfigurieren
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudyPlanAdapter(new ArrayList<>(), plan -> {
            // Bei Klick auf einen StudyPlan → StudyStepsActivity öffnen
            Intent intent = new Intent(this, StudyStepsActivity.class);
            intent.putExtra("plan_id", plan.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Zurück zum Dashboard
        btnBackToDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        // Gruppen des Benutzers abrufen
        fetchGroups();
    }

    /**
     * Ruft die Gruppen aus Supabase ab, in denen der aktuelle Benutzer Mitglied ist.
     */
    private void fetchGroups() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String jwtToken = prefs.getString("access_token", null);
        String userId   = prefs.getString("user_id", null);

        // Falls Benutzer nicht eingeloggt ist, zurück zur Login-Seite
        if (jwtToken == null || userId == null) {
            Toast.makeText(this, "Bitte erst einloggen", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // API-Client mit JWT erstellen
        DisciteOmnesApi api = DatabaseClient
                .getInstance(jwtToken)
                .create(DisciteOmnesApi.class);

        // Supabase-URL für gruppenbasierte Abfrage
        String url = "rest/v1/group_members"
                + "?select=group:groups(id,name)"
                + "&user_id=eq." + userId;

        // Anfrage senden
        api.getGroupsByUser(url).enqueue(new Callback<List<GroupMemberResponse>>() {
            @Override
            public void onResponse(Call<List<GroupMemberResponse>> call,
                                   Response<List<GroupMemberResponse>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    // Gruppeninformationen extrahieren
                    for (GroupMemberResponse gm : resp.body()) {
                        groupIds.add(gm.getGroup().getId());
                        groupNames.add(gm.getGroup().getName());
                    }
                    // Dropdown-Menü befüllen
                    setupSpinner();
                } else {
                    Toast.makeText(StudyPlansActivity.this,
                            "Konnte Gruppen nicht laden",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupMemberResponse>> call, Throwable t) {
                Toast.makeText(StudyPlansActivity.this,
                        "Netzwerkfehler: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialisiert den Spinner mit Gruppennamen und registriert Listener
     */
    private void setupSpinner() {
        // Spinner-Adapter konfigurieren
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                groupNames
        );
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroups.setAdapter(spinAdapter);

        // Bei Auswahl einer Gruppe → zugehörige Pläne laden
        spinnerGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                loadPlansFor(groupIds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Keine Aktion nötig
            }
        });
    }

    /**
     * Lädt die StudyPlans für eine bestimmte Gruppe und zeigt sie in der Liste.
     * @param groupId ID der ausgewählten Gruppe
     */
    private void loadPlansFor(String groupId) {
        String jwt = getSharedPreferences("auth", MODE_PRIVATE)
                .getString("access_token", "");

        DisciteOmnesApi api = DatabaseClient
                .getInstance(jwt)
                .create(DisciteOmnesApi.class);

        api.getStudyPlans("eq." + groupId).enqueue(new Callback<List<StudyPlan>>() {
            @Override
            public void onResponse(Call<List<StudyPlan>> call,
                                   Response<List<StudyPlan>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    // Daten an Adapter übergeben
                    adapter.updateData(resp.body());
                } else {
                    Toast.makeText(StudyPlansActivity.this,
                            "Fehler beim Laden der Plans",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StudyPlan>> call, Throwable t) {
                Toast.makeText(StudyPlansActivity.this,
                        "Netzwerkfehler: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
