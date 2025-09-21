package com.example.disciteomnes.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Diese Klasse repräsentiert ein Benutzerprofil aus der Supabase-Tabelle "profiles".
 *
 * Sie enthält grundlegende Informationen wie die Benutzer-ID und den angezeigten Namen.
 * Wird z. B. verwendet, um Benutzerinformationen in Gruppen oder Aufgaben darzustellen.
 */
public class Profile {

    // Die eindeutige ID des Benutzers (UUID)
    @SerializedName("id")
    private String id;

    // Der angezeigte Name des Benutzers
    @SerializedName("name")
    private String name;

    /**
     * Konstruktor zum Erstellen eines neuen Profile-Objekts.
     *
     * @param id   die eindeutige Benutzer-ID
     * @param name der Anzeigename des Benutzers
     */
    public Profile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter und Setter

    /**
     * Gibt die Benutzer-ID zurück.
     *
     * @return die ID des Benutzers
     */
    public String getId() {
        return id;
    }

    /**
     * Setzt die Benutzer-ID.
     *
     * @param id die neue Benutzer-ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Benutzers zurück.
     *
     * @return der Anzeigename
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Benutzers.
     *
     * @param name der neue Anzeigename
     */
    public void setName(String name) {
        this.name = name;
    }
}
