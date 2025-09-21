package com.example.disciteomnes.data.dto;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für das Erstellen eines neuen Lernplans dar.
 *
 * Sie wird verwendet, um einen neuen Study Plan in der Datenbank zu speichern – einschließlich
 * des zugehörigen Gruppenkontexts und eines Titels für den Plan.
 */
public class StudyPlanRequest {

    // Die ID der Gruppe, zu der der Lernplan gehört
    private String group_id;

    // Der Titel des Lernplans
    private String title;

    /**
     * Konstruktor zum Erstellen eines neuen StudyPlanRequest-Objekts.
     *
     * @param group_id die Gruppen-ID, der der Lernplan zugeordnet ist
     * @param title    der Titel des neuen Lernplans
     */
    public StudyPlanRequest(String group_id, String title) {
        this.group_id = group_id;
        this.title = title;
    }

    /**
     * Gibt die Gruppen-ID zurück.
     *
     * @return die ID der Gruppe
     */
    public String getGroup_id() {
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
