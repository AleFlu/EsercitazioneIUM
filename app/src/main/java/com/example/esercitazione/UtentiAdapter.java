package com.example.esercitazione;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UtentiAdapter extends ArrayAdapter<Utente> {
    private List<Utente> utenti;
    private List<Utente> utentiFiltered;

    public UtentiAdapter(Context context, List<Utente> utenti) {
        super(context, 0, utenti);
        this.utenti = utenti;
        this.utentiFiltered = utenti;
    }

    @Override
    public int getCount() {
        return utentiFiltered.size();
    }

    @Override
    public Utente getItem(int position) {
        return utentiFiltered.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_utente, parent, false);
        }

        final Utente utente = utentiFiltered.get(position);

        TextView nomeUtenteTextView = listItemView.findViewById(R.id.nome_utente_text_view);
        nomeUtenteTextView.setText(utente.getUsername());

        Button azioneButton = listItemView.findViewById(R.id.azione_button);
        if (utente.getIsAdmin()) {
            azioneButton.setText("Rimuovi");
        } else {
            azioneButton.setText("Aggiungi");
        }

        azioneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utente.getUsername().equals("admin")) {
                    // L'utente "admin" non può essere rimosso
                    Toast.makeText(getContext(), "L'admin non può essere rimosso", Toast.LENGTH_SHORT).show();
                } else {
                    if (utente.getIsAdmin()) {
                        // Azione di rimozione
                        // Rimuovi l'utente o imposta isAdmin a false
                        utente.setIsAdmin(false);
                    } else {
                        // Azione di aggiunta
                        // Aggiungi l'utente o imposta isAdmin a true
                        utente.setIsAdmin(true);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        return listItemView;
    }

    public void setUtenti(List<Utente> utenti) {
        this.utenti = utenti;
        this.utentiFiltered = utenti;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Utente> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(utenti);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Utente utente : utenti) {
                        if (utente.getUsername().toLowerCase().contains(filterPattern)) {
                            filteredList.add(utente);
                        }
                    }
                }

                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                utentiFiltered = (List<Utente>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
