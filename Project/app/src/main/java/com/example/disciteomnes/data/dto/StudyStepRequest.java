package com.example.disciteomnes.data.dto;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für das Erstellen eines neuen Lernschritts dar.
 *
 * Sie wird verwendet, um einen Schritt (Step) zu einem bestehenden Lernplan (Study Plan) hinzuzufügen,
 * inklusive Titel und Fälligkeitsdatum.
 */
public class StudyStepRequest {

    // Die ID des übergeordneten Lernplans, zu dem dieser Schritt gehört
    private int plan_id;

    // Der Titel des Lernschritts (z. B. "Kapitel 3 lesen")
    private String title;

    // Das Fälligkeitsdatum im Format "YYYY-MM-DD"
    private String due_date;

    /**
     * Konstruktor zum Erstellen eines neuen StudyStepRequest-Objekts.
     *
     * @param plan_id   die ID des zugehörigen Lernplans
     * @param title     der Titel des Lernschritts
     * @param due_date  das Fälligkeitsdatum dieses Schritts (z. B. "2025-07-10")
     */
    public StudyStepRequest(int plan_id, String title, String due_date) {
        this.plan_id  = plan_id;
        this.title    = title;
        this.due_date = due_date;
    }

    /**
     * Gibt die Plan-ID zurück.
     *
     * @return die ID des zugehörigen Study Plans
     */
    public int getPlan_id() {
        return plan_id;
    }

    /**
     * Gibt den Titel des Lernschritts zurück.
     *
     * @return der Titel des Schritts
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gibt das Fälligkeitsdatum zurück.
     *
     * @return das Fälligkeitsdatum im Format "YYYY-MM-DD"
     */
    public String getDue_date() {
        return due_date;
    }
}
