package com.example.disciteomnes.data.dto;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für das
 * Erstellen einer neuen Gruppe dar.
 *
 * Sie wird verwendet, um den Gruppennamen an den Server zu senden,
 * wenn ein Benutzer eine neue Gruppe anlegen möchte.
 */
public class GroupRequest {

    // Der Name der zu erstellenden Gruppe
    private String name;

    /**
     * Konstruktor zum Erstellen eines neuen GroupRequest-Objekts.
     *
     * @param name der gewünschte Name der neuen Gruppe
     */
    public GroupRequest(String name) {
        this.name = name;
    }

    /**
     * Gibt den Gruppennamen zurück.
     *
     * @return der Name der Gruppe
     */
    public String getName() {
        return name;
    }
}
