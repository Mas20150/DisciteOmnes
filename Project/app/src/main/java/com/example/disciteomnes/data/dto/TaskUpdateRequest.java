package com.example.disciteomnes.data.dto;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für das Aktualisieren
 * des Status einer Aufgabe (Task) dar.
 *
 * Sie wird verwendet, um eine bestehende Aufgabe als erledigt oder nicht erledigt zu markieren,
 * typischerweise über einen PATCH- oder PUT-Request an die API.
 */
public class TaskUpdateRequest {

    // Gibt an, ob die Aufgabe als erledigt markiert ist
    private boolean completed;

    /**
     * Konstruktor zum Erstellen eines neuen TaskUpdateRequest-Objekts.
     *
     * @param completed true, wenn die Aufgabe abgeschlossen ist; false sonst
     */
    public TaskUpdateRequest(boolean completed) {
        this.completed = completed;
    }

    /**
     * Gibt den aktuellen Status der Aufgabe zurück.
     *
     * @return true, wenn erledigt; false, wenn offen
     */
    public boolean isCompleted() {
        return completed;
    }
}
