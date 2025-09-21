package com.example.disciteomnes.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Diese Klasse repräsentiert ein Modell für einen einzelnen Lernschritt (Study Step),
 * wie er in der Supabase-Datenbank gespeichert ist.
 *
 * Ein StudyStep gehört zu einem bestimmten Lernplan (plan_id) und enthält Informationen
 * wie Titel, Fälligkeitsdatum und eine Liste von Benutzer-IDs, die diesen Schritt bereits abgeschlossen haben.
 */
public class StudyStep {

    // Eindeutige ID des Lernschritts
    @SerializedName("id")
    private int id;

    // ID des zugehörigen Lernplans
    @SerializedName("plan_id")
    private int planId;

    // Titel des Lernschritts (z. B. "Kapitel 1 lesen")
    @SerializedName("title")
    private String title;

    // Fälligkeitsdatum im Format "YYYY-MM-DD"
    @SerializedName("due_date")
    private String dueDate;

    // Liste von Benutzer-IDs, die diesen Schritt als abgeschlossen markiert haben
    @SerializedName("completed_by")
    private List<String> completedBy;

    // optional: wenn Du das Erstell‐Datum brauchst, kannst Du es so hinzufügen:
    // @SerializedName("created_at")
    // private String createdAt;

    /** Leerer Standardkonstruktor – notwendig für die Deserialisierung durch Gson */
    public StudyStep() { }

    /**
     * Konstruktor zum Erstellen eines vollständigen StudyStep-Objekts.
     *
     * @param id           die ID des Schritts
     * @param planId       die ID des zugehörigen Lernplans
     * @param title        der Titel des Lernschritts
     * @param dueDate      das Fälligkeitsdatum
     * @param completedBy  Liste der Benutzer-IDs, die den Schritt abgeschlossen haben
     */
    public StudyStep(int id,
                     int planId,
                     String title,
                     String dueDate,
                     List<String> completedBy) {
        this.id = id;
        this.planId = planId;
        this.title = title;
        this.dueDate = dueDate;
        this.completedBy = completedBy;
    }

    /**
     * Gibt die ID des Schritts zurück.
     *
     * @return die eindeutige Schritt-ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt die ID des zugehörigen Lernplans zurück.
     *
     * @return die plan_id
     */
    public int getPlanId() {
        return planId;
    }

    /**
     * Gibt den Titel des Schritts zurück.
     *
     * @return der Titel des Lernschritts
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gibt das Fälligkeitsdatum zurück.
     *
     * @return das Fälligkeitsdatum im Format "YYYY-MM-DD"
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Gibt die Liste der Benutzer-IDs zurück, die den Schritt abgeschlossen haben.
     *
     * @return Liste von Benutzer-UUIDs
     */
    public List<String> getCompletedBy() {
        return completedBy;
    }

    /**
     * Setzt die Liste der Benutzer, die den Schritt abgeschlossen haben.
     *
     * @param completedBy Liste von Benutzer-UUIDs
     */
    public void setCompletedBy(List<String> completedBy) {
        this.completedBy = completedBy;
    }
}
