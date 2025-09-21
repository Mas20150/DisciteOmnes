package com.example.disciteomnes.data.dto;

/**
 * Diese Klasse stellt das Datenübertragungsobjekt (DTO) für die
 * Mitgliedschaft eines Benutzers in einer Gruppe dar.
 *
 * Sie wird verwendet, um eine Anfrage an den Server zu senden,
 * wenn ein Benutzer einer bestimmten Gruppe beitreten möchte.
 */
public class GroupMemberRequest {

    // Die eindeutige ID der Gruppe, der beigetreten werden soll
    private String group_id;

    // Die eindeutige ID des Benutzers, der der Gruppe beitritt
    private String user_id;

    /**
     * Konstruktor zum Erstellen eines neuen GroupMemberRequest-Objekts.
     *
     * @param userId  die ID des Benutzers
     * @param groupId die ID der Gruppe
     */
    public GroupMemberRequest(String userId, String groupId) {
        this.user_id = userId;
        this.group_id = groupId;
    }

    /**
     * Gibt die Benutzer-ID zurück.
     *
     * @return die ID des Benutzers
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * Gibt die Gruppen-ID zurück.
     *
     * @return die ID der Gruppe
     */
    public String getGroup_id() {
        return group_id;
    }
}
