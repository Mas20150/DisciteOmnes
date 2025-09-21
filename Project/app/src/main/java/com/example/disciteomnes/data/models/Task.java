package com.example.disciteomnes.data.models;

/**
 * Diese Klasse repräsentiert ein Aufgabenmodell (Task),
 * wie es in der Supabase-Datenbank gespeichert ist.
 *
 * Eine Aufgabe enthält einen Titel, ein Fälligkeitsdatum,
 * einen Bearbeitungsstatus (completed) und eine eindeutige ID.
 */
public class Task {

    // Eindeutige ID der Aufgabe (wird von der Datenbank vergeben)
    private int id;

    // Titel der Aufgabe (z. B. "Hausaufgabe abschließen")
    private String title;

    // Fälligkeitsdatum im Format "YYYY-MM-DD"
    private String due_date;

    // Gibt an, ob die Aufgabe bereits erledigt wurde
    private boolean completed;

    /**
     * Konstruktor zum Erstellen eines Task-Objekts.
     *
     * @param id         eindeutige Aufgaben-ID
     * @param title      Titel der Aufgabe
     * @param due_date   Fälligkeitsdatum
     * @param completed  true, wenn die Aufgabe erledigt ist
     */
    public Task(int id, String title, String due_date, boolean completed) {
        this.id = id;
        this.title = title;
        this.due_date = due_date;
        this.completed = completed;
    }

    /**
     * Gibt die ID der Aufgabe zurück.
     *
     * @return die Aufgaben-ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt den Titel der Aufgabe zurück.
     *
     * @return der Titel
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gibt das Fälligkeitsdatum zurück.
     *
     * @return das Datum im Format "YYYY-MM-DD"
     */
    public String getDue_date() {
        return due_date;
    }

    /**
     * Gibt zurück, ob die Aufgabe abgeschlossen ist.
     *
     * @return true, wenn erledigt – sonst false
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Setzt den Erledigungsstatus der Aufgabe.
     *
     * @param completed true, wenn erledigt
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
