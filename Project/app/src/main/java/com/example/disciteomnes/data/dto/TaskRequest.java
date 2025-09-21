package com.example.disciteomnes.data.dto;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für das Erstellen einer neuen Aufgabe (Task) dar.
 *
 * Sie wird verwendet, um Aufgabeninformationen vom Client an den Server (z. B. Supabase) zu senden,
 * einschließlich Titel, Fälligkeitsdatum, Bearbeitungsstatus und zugehöriger Benutzer-ID.
 */
public class TaskRequest {

    // Der Titel der Aufgabe
    private String title;

    // Das Fälligkeitsdatum der Aufgabe im Format "YYYY-MM-DD"
    private String due_date;

    // Gibt an, ob die Aufgabe bereits erledigt ist
    private boolean completed;

    // Die ID des Benutzers, dem diese Aufgabe zugeordnet ist
    private String user_id;

    /**
     * Konstruktor zum Erstellen eines neuen TaskRequest-Objekts.
     *
     * @param title     der Titel der Aufgabe
     * @param due_date  das Fälligkeitsdatum (z. B. "2025-07-10")
     * @param completed true, wenn die Aufgabe bereits erledigt ist
     * @param user_id   die ID des Benutzers, dem die Aufgabe gehört
     */
    public TaskRequest(String title, String due_date, boolean completed, String user_id) {
        this.title = title;
        this.due_date = due_date;
        this.completed = completed;
        this.user_id = user_id;
    }

    // Getter (falls benötigt)

    /**
     * Gibt den Titel der Aufgabe zurück.
     *
     * @return der Titel der Aufgabe
     */
    public String getTitle() { return title; }

    /**
     * Gibt das Fälligkeitsdatum zurück.
     *
     * @return das Fälligkeitsdatum im Format "YYYY-MM-DD"
     */
    public String getDue_date() { return due_date; }

    /**
     * Gibt zurück, ob die Aufgabe als erledigt markiert ist.
     *
     * @return true, wenn erledigt – sonst false
     */
    public boolean isCompleted() { return completed; }

    /**
     * Gibt die Benutzer-ID zurück, der diese Aufgabe zugeordnet ist.
     *
     * @return die ID des Benutzers
     */
    public String getUser_id() { return user_id; }
}
