package com.example.disciteomnes.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für eine Registrierungsanfrage dar.
 *
 * Sie wird verwendet, um die Registrierungsdaten eines neuen Benutzers
 * (E-Mail und Passwort) an den Authentifizierungs-Endpunkt zu senden.
 */
public class RegisterRequest {

    // Die E-Mail-Adresse des neuen Benutzers (wird als "email" im JSON gesendet)
    @SerializedName("email")
    private String email;

    // Das gewünschte Passwort des neuen Benutzers (wird als "password" im JSON gesendet)
    @SerializedName("password")
    private String password;

    /**
     * Konstruktor zum Erstellen eines neuen RegisterRequest-Objekts.
     *
     * @param email    die E-Mail-Adresse des neuen Benutzers
     * @param password das Passwort für das Benutzerkonto
     */
    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
