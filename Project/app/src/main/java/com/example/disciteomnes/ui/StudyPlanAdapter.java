package com.example.disciteomnes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.models.StudyPlan;

import java.util.List;

/**
 * Adapter zur Darstellung einer Liste von StudyPlans in einem RecyclerView.
 * Jeder Plan kann angeklickt werden, um z. B. zur Detailansicht zu navigieren.
 */
public class StudyPlanAdapter extends RecyclerView.Adapter<StudyPlanAdapter.PlanViewHolder> {

    /**
     * Interface zur Übergabe eines Klick-Events für ein StudyPlan-Element.
     */
    public interface OnItemClick {
        void onClick(StudyPlan plan);
    }

    private final List<StudyPlan> plans;     // Liste der darzustellenden Pläne
    private final OnItemClick listener;      // Listener für Klicks

    /**
     * Konstruktor für den Adapter.
     *
     * @param plans    Liste der StudyPlans
     * @param listener Callback bei Klick auf einen Plan
     */
    public StudyPlanAdapter(List<StudyPlan> plans, OnItemClick listener) {
        this.plans = plans;
        this.listener = listener;
    }

    /**
     * Aktualisiert die angezeigten Daten im Adapter.
     *
     * @param newPlans Neue Liste von StudyPlans
     */
    public void updateData(List<StudyPlan> newPlans) {
        plans.clear();
        plans.addAll(newPlans);
        notifyDataSetChanged(); // RecyclerView neu rendern
    }

    /**
     * Erstellt die ViewHolder für jedes RecyclerView-Element.
     */
    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_study_plan, parent, false);
        return new PlanViewHolder(view);
    }

    /**
     * Verknüpft die Daten mit den ViewHoldern (wird für jedes Element aufgerufen).
     */
    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int pos) {
        StudyPlan plan = plans.get(pos);
        holder.title.setText(plan.getTitle());
        holder.itemView.setOnClickListener(v -> listener.onClick(plan));
    }

    /**
     * Gibt die Gesamtanzahl der Einträge zurück.
     */
    @Override
    public int getItemCount() {
        return plans.size();
    }

    /**
     * ViewHolder für die Anzeige eines einzelnen StudyPlans.
     */
    static class PlanViewHolder extends RecyclerView.ViewHolder {
        final TextView title;

        PlanViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.studyPlanTitle);
        }
    }
}
