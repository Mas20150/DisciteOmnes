package com.example.disciteomnes.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Diese Klasse erstellt eine Retrofit-Instanz für Datenbankzugriffe auf Supabase (`/rest/v1/...`).
 *
 * Im Gegensatz zur AuthClient-Klasse verwendet diese Instanz optional ein JWT-Token
 * zur Authentifizierung des aktuellen Benutzers.
 */
public class DatabaseClient {

    // Basis-URL deiner Supabase-Instanz (z. B. für REST-Endpunkte wie /rest/v1/tasks)
    private static final String BASE_URL = "https://pruclodpzbyfoaeuefvf.supabase.co/";

    // Öffentlicher Supabase-API-Key (wird für jede Anfrage benötigt)
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBydWNsb2RwemJ5Zm9hZXVlZnZmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTEyMTk1MDUsImV4cCI6MjA2Njc5NTUwNX0.HRIXlqoGYR492mZ5QFzurqDN0-mZSIeDzh0Bnj1WHgM";

    /**
     * Gibt eine konfigurierte Retrofit-Instanz für den Zugriff auf die Supabase-Datenbank zurück.
     *
     * - Fügt in jedem Fall den Header `apikey` hinzu.
     * - Falls `jwtToken` angegeben ist, wird zusätzlich der Header
     *   `Authorization: Bearer <jwtToken>` hinzugefügt.
     *
     * @param jwtToken das optionale JSON Web Token des eingeloggten Benutzers (kann null sein)
     * @return eine Retrofit-Instanz zur Kommunikation mit Supabase (REST-API)
     */
    public static Retrofit getInstance(String jwtToken) {
        // Interceptor zur Protokollierung aller HTTP-Anfragen und -Antworten im Logcat
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        // HTTP-Client mit Header-Interceptor für Authentifizierung und Logging
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    // Grundlegende Header (apikey + JSON)
                    Request.Builder reqB = chain.request().newBuilder()
                            .header("apikey", SUPABASE_ANON_KEY)
                            .header("Content-Type", "application/json");

                    // Falls vorhanden: JWT als Bearer-Token hinzufügen
                    if (jwtToken != null && !jwtToken.isEmpty()) {
                        reqB.header("Authorization", "Bearer " + jwtToken);
                    }

                    return chain.proceed(reqB.build());
                })
                .addInterceptor(logger)
                .build();

        // Retrofit-Builder mit Gson-Konverter
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
