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
import com.example.disciteomnes.data.dto.StudyPlanRequest;
import com.example.disciteomnes.data.models.StudyPlan;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlannerActivity extends AppCompatActivity {

    private RecyclerView recyclerPlans;
    private EditText etTitle;
    private Button btnAdd, btnBackToDashboard;
    private DisciteOmnesApi api;
    private StudyPlanAdapter adapter;
    private String groupId, jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        // ─── UI ─────────────────────────
        recyclerPlans = findViewById(R.id.recyclerStudyPlans);
        recyclerPlans.setLayoutManager(new LinearLayoutManager(this));
        etTitle = findViewById(R.id.etStudyTitle);
        btnAdd = findViewById(R.id.btnAddPlan);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);

        // ─── JWT und group_id laden ─────
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        jwt = prefs.getString("access_token", null);
        groupId = prefs.getString("group_id", null);

        if (jwt == null || groupId == null) {
            Toast.makeText(this, "Nicht angemeldet oder Gruppe fehlt", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // ─── API & Adapter ───────────────
        api = DatabaseClient.getInstance(jwt).create(DisciteOmnesApi.class);
        adapter = new StudyPlanAdapter(new ArrayList<>(), plan -> {
            Intent i = new Intent(PlannerActivity.this, StudyStepsActivity.class);
            i.putExtra("plan_id", plan.getId());
            startActivity(i);
        });
        recyclerPlans.setAdapter(adapter);  // Adapter direkt setzen, auch wenn leer

        // ─── Events ──────────────────────
        btnBackToDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Titel eingeben!", Toast.LENGTH_SHORT).show();
                return;
            }

            StudyPlanRequest req = new StudyPlanRequest(groupId, title);
            api.addStudyPlan(req).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> resp) {
                    if (resp.isSuccessful()) {
                        Toast.makeText(PlannerActivity.this, "Hinzugefügt", Toast.LENGTH_SHORT).show();
                        etTitle.setText("");
                        loadPlans();  // Liste neu laden
                    } else {
                        Toast.makeText(PlannerActivity.this, "Fehler beim Speichern", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PlannerActivity.this, "Netzwerkfehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // ─── Initiales Laden ─────────────
        loadPlans();
    }

    private void loadPlans() {
        api.getStudyPlans("eq." + groupId).enqueue(new Callback<List<StudyPlan>>() {
            @Override
            public void onResponse(Call<List<StudyPlan>> call, Response<List<StudyPlan>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    adapter.updateData(resp.body());
                } else {
                    Toast.makeText(PlannerActivity.this, "Keine Pläne gefunden", Toast.LENGTH_SHORT).show();
                    adapter.updateData(new ArrayList<>()); // auch bei leerer Antwort updaten
                }
            }

            @Override
            public void onFailure(Call<List<StudyPlan>> call, Throwable t) {
                Toast.makeText(PlannerActivity.this, "Fehler beim Laden: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
