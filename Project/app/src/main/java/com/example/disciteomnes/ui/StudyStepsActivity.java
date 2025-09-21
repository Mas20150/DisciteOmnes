package com.example.disciteomnes.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.StudyStepRequest;
import com.example.disciteomnes.data.models.StudyStep;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity zur Anzeige und Verwaltung von StudySteps für einen bestimmten StudyPlan.
 * Der Benutzer kann neue Lernschritte hinzufügen und bestehende anzeigen lassen.
 */
public class StudyStepsActivity extends AppCompatActivity {

    private RecyclerView recyclerSteps;
    private StudyStepAdapter stepAdapter;
    private EditText etTitle, etDate;
    private Button btnAddStep;
    private TextView tvEmpty;
    private String jwt;
    private int planId;

    /**
     * Initialisiert die Activity, lädt die UI-Elemente und ruft die Steps aus der Datenbank ab.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_steps);

        // 1) Views binden
        etTitle       = findViewById(R.id.etStepTitle);
        etDate        = findViewById(R.id.etStepDate);
        btnAddStep    = findViewById(R.id.btnAddStep);
        tvEmpty       = findViewById(R.id.tvEmptySteps);
        recyclerSteps = findViewById(R.id.recyclerStudySteps);

        // 2) RecyclerView initialisieren
        recyclerSteps.setLayoutManager(new LinearLayoutManager(this));
        stepAdapter = new StudyStepAdapter();
        recyclerSteps.setAdapter(stepAdapter);

        // 3) Auth-Daten (JWT + Plan-ID) laden
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        jwt    = prefs.getString("access_token", null);
        planId = getIntent().getIntExtra("plan_id", -1);
        if (jwt == null || planId < 0) {
            Toast.makeText(this, "Auth oder Plan fehlt", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 4) Button-Listener
        btnAddStep.setOnClickListener(v -> {
            // Tastatur ausblenden
            InputMethodManager imm =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            addStep();
        });

        // 5) Schritte laden
        loadSteps();
    }

    /**
     * Lädt alle StudySteps für den aktuellen Plan aus der Datenbank.
     * Zeigt bei leerer Liste einen Hinweistext an.
     */
    private void loadSteps() {
        DisciteOmnesApi api = DatabaseClient
                .getInstance(jwt)
                .create(DisciteOmnesApi.class);

        String filter = "eq." + planId;
        Log.d("StudySteps", "Calling getStudyStepsByPlanId with filter=" + filter);

        api.getStudyStepsByPlanId(filter).enqueue(new Callback<List<StudyStep>>() {
            @Override
            public void onResponse(Call<List<StudyStep>> call, Response<List<StudyStep>> resp) {
                Log.d("StudySteps", "onResponse: isSuccessful=" + resp.isSuccessful()
                        + " code=" + resp.code());

                if (resp.isSuccessful() && resp.body() != null) {
                    List<StudyStep> list = resp.body();
                    Log.d("StudySteps", "Loaded " + list.size() + " steps");

                    // Anzeige je nach Ergebnis
                    if (list.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        recyclerSteps.setVisibility(View.GONE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        recyclerSteps.setVisibility(View.VISIBLE);
                        stepAdapter.updateData(list);
                    }

                } else {
                    String errBody = "";
                    try {
                        errBody = resp.errorBody() != null
                                ? resp.errorBody().string()
                                : "null";
                    } catch (Exception e) {
                        Log.e("StudySteps", "Error reading errorBody", e);
                    }
                    Log.e("StudySteps", "Fehler beim Laden: HTTP " + resp.code() + " – " + errBody);
                    Toast.makeText(StudyStepsActivity.this,
                            "Fehler beim Laden (siehe Logcat)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StudyStep>> call, Throwable t) {
                Log.e("StudySteps", "Network failure in loadSteps", t);
                Toast.makeText(StudyStepsActivity.this,
                        "Netzwerkfehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sendet einen neuen Step an den Server und aktualisiert die Anzeige bei Erfolg.
     * Vor dem Absenden wird geprüft, ob beide Eingabefelder ausgefüllt sind.
     */
    private void addStep() {
        String title = etTitle.getText().toString().trim();
        String date  = etDate .getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Titel und Datum eingeben", Toast.LENGTH_SHORT).show();
            return;
        }

        StudyStepRequest req = new StudyStepRequest(planId, title, date);
        Log.d("StudySteps", "Calling addStudyStep with request=" + req);

        DisciteOmnesApi api = DatabaseClient
                .getInstance(jwt)
                .create(DisciteOmnesApi.class);

        api.addStudyStep(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                Log.d("StudySteps", "addStudyStep onResponse: isSuccessful=" +
                        resp.isSuccessful() + " code=" + resp.code());

                if (resp.isSuccessful()) {
                    Log.d("StudySteps", "Step erfolgreich hinzugefügt");
                    etTitle.setText("");
                    etDate.setText("");
                    loadSteps(); // Nach dem Hinzufügen erneut laden
                } else {
                    String errBody = "";
                    try {
                        errBody = resp.errorBody() != null
                                ? resp.errorBody().string()
                                : "null";
                    } catch (Exception e) {
                        Log.e("StudySteps", "Error reading errorBody", e);
                    }
                    Log.e("StudySteps", "Fehler beim Hinzufügen: HTTP " +
                            resp.code() + " – " + errBody);
                    Toast.makeText(StudyStepsActivity.this,
                            "Fehler beim Hinzufügen (siehe Logcat)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("StudySteps", "Network failure in addStep", t);
                Toast.makeText(StudyStepsActivity.this,
                        "Netzwerkfehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
