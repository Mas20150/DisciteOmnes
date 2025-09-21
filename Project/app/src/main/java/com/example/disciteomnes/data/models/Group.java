package com.example.disciteomnes.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Diese Klasse repräsentiert ein Gruppenmodell, wie es aus der Supabase-Datenbank
 * oder einer API-Antwort geladen wird.
 *
 * Sie enthält Basisinformationen über eine Lerngruppe wie ID, Name, Beschreibung
 * und den Ersteller der Gruppe.
 */
public class Group {

    // Die eindeutige ID der Gruppe (UUID)
    private String id;

    // Der Name der Gruppe
    private String name;

    // Eine optionale Beschreibung der Gruppe (z. B. "Mathe-Gruppe für Semester 2")
    @SerializedName("description")
    private String description;

    // Die Benutzer-ID des Erstellers der Gruppe
    @SerializedName("created_by")
    private String createdBy;

    // Leerer Konstruktor (wird z. B. von Gson für die Deserialisierung benötigt)
    public Group() {}

    /**
     * Gibt die Gruppen-ID zurück.
     *
     * @return die ID der Gruppe (UUID)
     */
    public String getId() {
        return id;
    }

    /**
     * Gibt den Namen der Gruppe zurück.
     *
     * @return der Gruppenname
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Beschreibung der Gruppe zurück.
     *
     * @return die Gruppenbeschreibung
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gibt die ID des Erstellers der Gruppe zurück.
     *
     * @return die Benutzer-ID des Gruppenerstellers
     */
    public String getCreatedBy() {
        return createdBy;
    }
}
