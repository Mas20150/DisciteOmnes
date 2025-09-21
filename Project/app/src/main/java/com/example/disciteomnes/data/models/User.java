package com.example.disciteomnes.data.models;

/**
 * Diese Klasse repräsentiert ein einfaches Benutzerobjekt (User),
 * das z. B. für Registrierungs- oder Anmeldevorgänge verwendet wird.
 *
 * Sie enthält grundlegende Informationen wie Name, E-Mail und Passwort.
 */
public class User {

    // Der vollständige Name des Benutzers
    private String name;

    // Die E-Mail-Adresse des Benutzers
    private String email;

    // Das Passwort des Benutzers (im Klartext – sollte nur kurzfristig verwendet werden!)
    private String password;

    /**
     * Leerer Standardkonstruktor (z. B. für Datenbindung oder JSON-Deserialisierung erforderlich).
     */
    public User() {}

    /**
     * Konstruktor zum Erstellen eines neuen User-Objekts.
     *
     * @param name     der vollständige Name des Benutzers
     * @param email    die E-Mail-Adresse des Benutzers
     * @param password das Passwort des Benutzers
     */
    public User(String name, String email, String password) {
        this.name     = name;
        this.email    = email;
        this.password = password;
    }

    /**
     * Gibt den Namen des Benutzers zurück.
     *
     * @return der vollständige Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die E-Mail-Adresse des Benutzers zurück.
     *
     * @return die E-Mail-Adresse
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gibt das Passwort des Benutzers zurück.
     *
     * @return das Passwort (Klartext – nur zur Initialverwendung gedacht)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt den Namen des Benutzers.
     *
     * @param name der neue Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setzt die E-Mail-Adresse des Benutzers.
     *
     * @param email die neue E-Mail-Adresse
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setzt das Passwort des Benutzers.
     *
     * @param password das neue Passwort
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
