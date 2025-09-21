package com.example.disciteomnes.network;

import com.example.disciteomnes.data.dto.*;
import com.example.disciteomnes.data.models.*;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Dieses Interface definiert alle API-Endpunkte für die Kommunikation mit Supabase
 * über Retrofit. Es deckt Authentifizierung, Benutzerprofile, Aufgaben (Tasks),
 * Gruppen, Lernpläne (Study Plans) und Lernschritte (Study Steps) ab.
 */
public interface DisciteOmnesApi {

    // ───────────── [AUTHENTIFIZIERUNG] ─────────────

    /**
     * Registrierung eines neuen Benutzers bei Supabase Auth.
     */
    @POST("auth/v1/signup")
    Call<Void> register(@Body RegisterRequest request);

    /**
     * Login eines Benutzers über E-Mail und Passwort.
     * Gibt bei Erfolg ein Access-Token im LoginResponse zurück.
     */
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> login(@Body LoginRequest request);

    /**
     * Abrufen der aktuellen Benutzerinformationen basierend auf dem Access-Token.
     */
    @GET("auth/v1/user")
    Call<ResponseBody> getUser();


    // ───────────── [BENUTZERPROFIL / PROFILES] ─────────────

    /**
     * Erstellt ein neues Profil in der `profiles`-Tabelle (z. B. nach Registrierung).
     * Gibt das gespeicherte Profile-Objekt zurück.
     */
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/profiles")
    Call<Profile> createProfile(@Body Profile profile);


    // ───────────── [AUFGABEN / TASKS] ─────────────

    /**
     * Holt alle Aufgaben eines bestimmten Benutzers.
     * @param userIdEq z. B. "eq.<user-id>"
     * @param select z. B. "*"
     */
    @GET("rest/v1/tasks")
    Call<List<Task>> getTasksByUserId(
            @Query(value = "user_id", encoded = true) String userIdEq,
            @Query(value = "select", encoded = true) String select
    );

    /**
     * Fügt eine neue Aufgabe für einen Benutzer hinzu.
     */
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/tasks")
    Call<List<Task>> addTask(@Body TaskRequest request);

    /**
     * Aktualisiert den Status (z. B. "completed") einer bestimmten Aufgabe.
     * @param idFilter z. B. "eq.3"
     */
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @PATCH("rest/v1/tasks")
    Call<List<Task>> updateTask(
            @Query(value = "id", encoded = true) String idFilter,
            @Body TaskUpdateRequest request
    );

    /**
     * Löscht eine Aufgabe anhand ihrer ID.
     * @param idFilter z. B. "eq.5"
     */
    @DELETE("rest/v1/tasks")
    Call<Void> deleteTask(@Query(value = "id", encoded = true) String idFilter);


    // ───────────── [GRUPPEN / GROUPS] ─────────────

    /**
     * Erstellt eine neue Lerngruppe.
     */
    @Headers("Prefer: return=representation")
    @POST("rest/v1/groups")
    Call<List<Group>> addGroup(@Body GroupRequest request);

    /**
     * Fügt einen Benutzer einer Gruppe hinzu.
     */
    @Headers({
            "Content-Type: " + "application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/group_members")
    Call<Void> joinGroup(@Body GroupMemberRequest request);

    /**
     * Holt alle Gruppen, denen ein Benutzer zugeordnet ist (über die group_members-Tabelle).
     * @param fullUrl sollte z. B. sein:
     * "rest/v1/group_members?select=group:groups(id,name)&user_id=eq.<userId>"
     */
    @GET
    Call<List<GroupMemberResponse>> getGroupsByUser(@Url String fullUrl);


    // ───────────── [STUDIENPLÄNE / STUDY PLANS] ─────────────

    /**
     * Holt alle Lernpläne für eine bestimmte Gruppe.
     * @param groupFilter z. B. "eq.<group-id>"
     */
    @GET("rest/v1/study_plans")
    Call<List<StudyPlan>> getStudyPlans(
            @Query(value = "group_id", encoded = true) String groupFilter
    );

    /**
     * Fügt einen neuen Lernplan zu einer Gruppe hinzu.
     */
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/study_plans")
    Call<Void> addStudyPlan(@Body StudyPlanRequest request);


    // ───────────── [LERNABSCHNITTE / STUDY STEPS] ─────────────

    /**
     * Holt alle Lernschritte, die zu einem bestimmten Lernplan gehören.
     * @param planFilter z. B. "eq.<plan-id>"
     */
    @GET("rest/v1/study_steps")
    Call<List<StudyStep>> getStudyStepsByPlanId(
            @Query(value = "plan_id", encoded = true) String planFilter
    );

    /**
     * Fügt einen neuen Lernschritt zu einem bestehenden Lernplan hinzu.
     */
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/study_steps")
    Call<Void> addStudyStep(@Body StudyStepRequest request);

}
