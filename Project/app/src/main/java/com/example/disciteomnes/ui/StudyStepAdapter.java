package com.example.disciteomnes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.models.StudyStep;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter-Klasse für die RecyclerView-Darstellung einzelner StudySteps (Lernschritte).
 * Verwaltet das Layout und die Datenbindung für jeden Eintrag in der Liste.
 */
public class StudyStepAdapter
        extends RecyclerView.Adapter<StudyStepAdapter.StepViewHolder> {

    // Liste der aktuell angezeigten Lernschritte
    private final List<StudyStep> steps = new ArrayList<>();

    /**
     * Konstruktor: erstellt eine leere Liste.
     * Daten werden später über updateData(...) eingefügt.
     */
    public StudyStepAdapter() {
        // Keine Aktion nötig – Liste ist initial leer
    }

    /**
     * Ersetzt die vorhandenen Schritte durch neue und aktualisiert die Anzeige.
     * @param newSteps Liste mit neuen Lernschritten
     */
    public void updateData(List<StudyStep> newSteps) {
        steps.clear();             // Alte Daten entfernen
        steps.addAll(newSteps);    // Neue Daten hinzufügen
        notifyDataSetChanged();    // RecyclerView aktualisieren
    }

    /**
     * Erstellt ein neues ViewHolder-Objekt mit dem Layout für einen einzelnen Step.
     */
    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // XML-Layout für einzelne StudyStep-Zeile laden
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_study_step, parent, false);
        return new StepViewHolder(v);
    }

    /**
     * Bindet die Daten eines StudySteps an die UI-Elemente im ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int pos) {
        StudyStep s = steps.get(pos);  // Aktuelles Element holen
        holder.txtTitle.setText(s.getTitle());                // Titel setzen
        holder.txtDate.setText("Fällig bis: " + s.getDueDate()); // Datum formatieren
    }

    /**
     * Gibt die Anzahl der Einträge zurück.
     */
    @Override
    public int getItemCount() {
        return steps.size();
    }

    /**
     * ViewHolder-Klasse kapselt die UI-Elemente einer einzelnen Step-Zeile.
     */
    static class StepViewHolder extends RecyclerView.ViewHolder {
        final TextView txtTitle, txtDate;

        StepViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.studyStepTitle); // Referenz auf Titel-Textfeld
            txtDate  = itemView.findViewById(R.id.studyStepDate);  // Referenz auf Datum-Textfeld
        }
    }
}
