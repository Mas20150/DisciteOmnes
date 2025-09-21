package com.example.disciteomnes.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Diese Klasse repräsentiert die Serverantwort nach einer erfolgreichen Login-Anfrage.
 *
 * Sie enthält das Access-Token, das für authentifizierte Folgeanfragen verwendet wird.
 */
public class LoginResponse {

    // Das Access-Token, das vom Server zurückgegeben wird (JSON-Feld: "access_token")
    @SerializedName("access_token")
    private String accessToken;

    /**
     * Gibt das vom Server erhaltene Access-Token zurück.
     *
     * @return das JWT Access-Token zur Authentifizierung
     */
    public String getAccessToken() {
        return accessToken;
    }
}
