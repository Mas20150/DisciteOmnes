package com.example.disciteomnes.data.dto;

import com.example.disciteomnes.data.models.Group;
import com.google.gson.annotations.SerializedName;

/**
 * Diese Klasse repräsentiert die Antwort vom Server,
 * wenn Gruppendaten über die Gruppenmitgliedschaft abgefragt werden.
 *
 * Typischerweise wird sie verwendet, um Informationen
 * über die Gruppe zu erhalten, der ein Benutzer angehört.
 */
public class GroupMemberResponse {

    // Enthält das Group-Objekt, das von der API zurückgegeben wird
    @SerializedName("group")
    private Group group;

    /**
     * Gibt das vollständige Group-Objekt zurück.
     *
     * @return die Gruppe, der das Mitglied zugeordnet ist
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Gibt die Gruppen-ID zurück, sofern das Gruppenobjekt nicht null ist.
     *
     * @return die ID der Gruppe oder null, wenn keine Gruppe vorhanden ist
     */
    public String getGroupId() {
        return group != null ? group.getId() : null;
    }
}
