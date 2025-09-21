package com.example.disciteomnes.data.models;

/**
 * Diese Klasse repräsentiert ein Modell für einen Lernplan (Study Plan),
 * wie er in der Supabase-Datenbank gespeichert ist.
 *
 * Ein StudyPlan ist einer bestimmten Gruppe zugeordnet und enthält einen Titel.
 */
public class StudyPlan {

    // Die eindeutige ID des Lernplans (automatisch von der Datenbank vergeben)
    private int id;

    // Die Gruppen-ID, zu der dieser Lernplan gehört
    private String group_id;

    // Der Titel des Lernplans (z. B. "Mathe-Wiederholung Juli")
    private String title;

    /**
     * Konstruktor zum Erstellen eines neuen StudyPlan-Objekts.
     *
     * @param id        die eindeutige ID des Lernplans
     * @param group_id  die ID der Gruppe, zu der dieser Lernplan gehört
     * @param title     der Titel des Lernplans
     */
    public StudyPlan(int id, String group_id, String title) {
        this.id = id;
        this.group_id = group_id;
        this.title = title;
    }

    /**
     * Gibt die ID des Lernplans zurück.
     *
     * @return die ID des Lernplans
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt die Gruppen-ID zurück, der der Lernplan zugeordnet ist.
     *
     * @return die Gruppen-ID
     */
    public String getGroupId() {
        return group_id;
    }

    /**
     * Gibt den Titel des Lernplans zurück.
     *
     * @return der Titel des Lernplans
     */
    public String getTitle() {
        return title;
    }
}
