package com.example.disciteomnes.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für eine Login-Anfrage dar.
 *
 * Sie wird verwendet, um die Anmeldedaten (E-Mail und Passwort)
 * an den Authentifizierungs-Endpunkt des Servers zu senden.
 */
public class LoginRequest {

    // Die E-Mail-Adresse des Benutzers (wird als "email" im JSON gesendet)
    @SerializedName("email")
    private String email;

    // Das Passwort des Benutzers (wird als "password" im JSON gesendet)
    @SerializedName("password")
    private String password;

    /**
     * Konstruktor zum Erstellen eines neuen LoginRequest-Objekts.
     *
     * @param email    die E-Mail-Adresse des Benutzers
     * @param password das Passwort des Benutzers
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
