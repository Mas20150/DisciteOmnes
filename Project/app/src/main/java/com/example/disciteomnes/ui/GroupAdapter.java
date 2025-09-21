package com.example.disciteomnes.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disciteomnes.R;
import com.example.disciteomnes.data.models.Group;

import java.util.List;

/**
 * RecyclerView-Adapter zur Anzeige einer Liste von Gruppen.
 *
 * Für jede Gruppe wird ein Eintrag mit dem Gruppennamen und der UUID angezeigt.
 * Die UUID kann durch Antippen in die Zwischenablage kopiert werden.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    // Liste aller Gruppen, die angezeigt werden sollen
    private final List<Group> groupList;

    /**
     * Konstruktor zum Initialisieren des Adapters mit einer Liste von Gruppen.
     *
     * @param groupList Liste von Group-Objekten
     */
    public GroupAdapter(List<Group> groupList) {
        this.groupList = groupList;
    }

    /**
     * Erstellt ein neues ViewHolder-Element für eine Gruppenanzeige.
     */
    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Einzelnes Gruppenlayout (item_group.xml) aufbauen
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    /**
     * Bindet die Daten einer bestimmten Gruppe an die entsprechende View.
     */
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.tvGroupName.setText(group.getName());
        holder.tvGroupUUID.setText("UUID: " + group.getId());

        // Beim Klick auf die UUID → in die Zwischenablage kopieren
        holder.tvGroupUUID.setOnClickListener(v -> {
            Context context = v.getContext();
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("UUID", group.getId());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "UUID kopiert", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Gibt die Anzahl der Gruppen zurück.
     */
    @Override
    public int getItemCount() {
        return groupList.size();
    }

    /**
     * ViewHolder-Klasse für die Darstellung einer einzelnen Gruppe.
     * Enthält:
     * - Gruppenname (tvGroupName)
     * - Gruppen-UUID (tvGroupUUID)
     */
    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvGroupUUID;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvGroupUUID = itemView.findViewById(R.id.tvGroupUUID);
        }
    }
}
