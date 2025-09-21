package com.example.disciteomnes.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Diese Klasse erstellt einen Retrofit-Client für Authentifizierungsanfragen an Supabase.
 *
 * Sie wird für alle Anfragen an die Supabase Auth API verwendet, z. B. zum Registrieren,
 * Einloggen oder Abrufen von Benutzerinformationen.
 */
public class AuthClient {

    // Basis-URL für alle Supabase-Anfragen (inkl. Auth)
    private static final String BASE_URL = "https://pruclodpzbyfoaeuefvf.supabase.co/";

    // Supabase-Anonym-Schlüssel (Public API Key)
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBydWNsb2RwemJ5Zm9hZXVlZnZmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTEyMTk1MDUsImV4cCI6MjA2Njc5NTUwNX0.HRIXlqoGYR492mZ5QFzurqDN0-mZSIeDzh0Bnj1WHgM";

    /**
     * Erstellt und gibt eine Singleton-Retrofit-Instanz zurück, die für Auth-Anfragen konfiguriert ist.
     *
     * Es werden automatisch folgende Header gesetzt:
     * - apikey: zur Autorisierung mit Supabase
     * - Content-Type: application/json
     *
     * Zusätzlich werden alle HTTP-Anfragen und -Antworten geloggt (BODY-Level).
     *
     * @return eine konfigurierte Retrofit-Instanz
     */
    public static Retrofit getInstance() {
        // Interceptor zur Ausgabe aller HTTP-Anfragen und -Antworten im Logcat
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient mit Header-Interceptor und Logger
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request req = chain.request().newBuilder()
                            .header("apikey", SUPABASE_ANON_KEY)
                            .header("Content-Type", "application/json")
                            .build();
                    return chain.proceed(req);
                })
                .addInterceptor(logger)
                .build();

        // Retrofit-Instanz mit Gson-Konverter
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
