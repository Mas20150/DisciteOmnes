package com.example.disciteomnes.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Diese Klasse repräsentiert einen Benutzer, wie er vom Supabase-Auth-Endpoint zurückgegeben wird.
 *
 * Sie wird typischerweise verwendet, um nach dem Login oder bei Auth-Validierung
 * Benutzerinformationen wie ID und E-Mail-Adresse auszulesen.
 */
public class SupabaseUser {

    // Die eindeutige Benutzer-ID (UUID), wie sie von Supabase bereitgestellt wird
    @SerializedName("id")
    private String id;

    // Die registrierte E-Mail-Adresse des Benutzers
    @SerializedName("email")
    private String email;

    /**
     * Gibt die Benutzer-ID zurück.
     *
     * @return die ID des Benutzers (UUID)
     */
    public String getId() {
        return id;
    }

    /**
     * Gibt die E-Mail-Adresse des Benutzers zurück.
     *
     * @return die registrierte E-Mail-Adresse
     */
    public String getEmail() {
        return email;
    }
}
