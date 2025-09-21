package com.example.disciteomnes.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.dto.TaskRequest;
import com.example.disciteomnes.data.dto.TaskUpdateRequest;
import com.example.disciteomnes.data.models.Task;
import com.example.disciteomnes.network.DatabaseClient;
import com.example.disciteomnes.network.DisciteOmnesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity zur Verwaltung von Aufgaben:
 * - Anzeigen
 * - Erstellen
 * - Aktualisieren (abhaken)
 * - Löschen (long click)
 */
public class TasksActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnAddTask, btnBack;
    private ArrayAdapter<String> adapter;
    private final List<Task> taskList = new ArrayList<>();
    private final List<String> taskTitles = new ArrayList<>();
    private DisciteOmnesApi api;
    private String userId;

    /**
     * Initialisiert die Activity: lädt Auth, API und UI und ruft Aufgaben ab.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // Authentifizierung laden
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String jwt = prefs.getString("access_token", null);
        userId = prefs.getString("user_id", null);

        if (jwt == null || userId == null) {
            Toast.makeText(this, "Nicht angemeldet", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // API-Client initialisieren
        api = DatabaseClient.getInstance(jwt).create(DisciteOmnesApi.class);

        // UI-Elemente verbinden
        listView    = findViewById(R.id.listViewTasks);
        btnAddTask  = findViewById(R.id.btnAddTask);
        btnBack     = findViewById(R.id.btnBackToDashboard);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskTitles);
        listView.setAdapter(adapter);

        // Buttons
        btnAddTask.setOnClickListener(v -> showAddDialog());
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        // Aufgaben abhaken (Click)
        listView.setOnItemClickListener((parent, view, pos, id) -> {
            Task task = taskList.get(pos);
            task.setCompleted(!task.isCompleted());
            updateTask(task, pos);
        });

        // Aufgabe löschen (LongClick)
        listView.setOnItemLongClickListener((parent, view, pos, id) -> {
            deleteTask(taskList.get(pos).getId(), pos);
            return true;
        });

        // Aufgaben laden
        loadTasks();
    }

    /**
     * Holt alle Aufgaben des Benutzers aus Supabase und aktualisiert die Anzeige.
     */
    private void loadTasks() {
        api.getTasksByUserId("eq." + userId, "*").enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskList.clear();
                    taskTitles.clear();
                    for (Task task : response.body()) {
                        taskList.add(task);
                        taskTitles.add((task.isCompleted() ? "✅ " : "⬜ ") + task.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TasksActivity.this, "Fehler beim Laden der Aufgaben", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(TasksActivity.this, "Netzwerkfehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Zeigt einen Dialog zum Hinzufügen einer neuen Aufgabe.
     * Nach Bestätigung wird die Aufgabe in Supabase gespeichert.
     */
    private void showAddDialog() {
        EditText titleInput = new EditText(this);
        titleInput.setHint("Titel");

        EditText dueDateInput = new EditText(this);
        dueDateInput.setHint("Fälligkeitsdatum (yyyy-MM-dd)");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);
        layout.addView(titleInput);
        layout.addView(dueDateInput);

        new AlertDialog.Builder(this)
                .setTitle("Neue Aufgabe")
                .setView(layout)
                .setPositiveButton("Hinzufügen", (dialog, which) -> {
                    String title = titleInput.getText().toString().trim();
                    String dueDate = dueDateInput.getText().toString().trim();

                    if (title.isEmpty() || dueDate.isEmpty()) {
                        Toast.makeText(this, "Titel & Datum erforderlich", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TaskRequest request = new TaskRequest(title, dueDate, false, userId);
                    api.addTask(request).enqueue(new Callback<List<Task>>() {
                        @Override
                        public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                                Task newTask = response.body().get(0);
                                taskList.add(newTask);
                                taskTitles.add("⬜ " + newTask.getTitle());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(TasksActivity.this, "Fehler beim Hinzufügen", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Task>> call, Throwable t) {
                            Toast.makeText(TasksActivity.this, "Netzwerkfehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    /**
     * Aktualisiert den Status (erledigt/nicht) einer Aufgabe in Supabase.
     * @param task Die zu aktualisierende Aufgabe.
     * @param pos Position der Aufgabe in der Liste.
     */
    private void updateTask(Task task, int pos) {
        TaskUpdateRequest request = new TaskUpdateRequest(task.isCompleted());

        api.updateTask("eq." + task.getId(), request).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Task updatedTask = response.body().get(0);
                    taskList.set(pos, updatedTask);
                    taskTitles.set(pos, (updatedTask.isCompleted() ? "✅ " : "⬜ ") + updatedTask.getTitle());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TasksActivity.this, "Update fehlgeschlagen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(TasksActivity.this, "Netzwerkfehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Löscht eine Aufgabe in Supabase und entfernt sie aus der Liste.
     * @param id ID der zu löschenden Aufgabe.
     * @param pos Position in der lokalen Liste.
     */
    private void deleteTask(int id, int pos) {
        api.deleteTask("eq." + id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    taskList.remove(pos);
                    taskTitles.remove(pos);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TasksActivity.this, "Löschen fehlgeschlagen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TasksActivity.this, "Netzwerkfehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
