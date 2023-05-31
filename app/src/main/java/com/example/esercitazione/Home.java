package com.example.esercitazione;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private TextView benvenutoTextView;
    private Button logoutBtn;
    private Button modificaPasswordBtn;
    private Utente utente;
    private List<Utente> listaUtenti;
    private ListView utentiListView;
    private UtentiAdapter adapter;
    private EditText ricercaEditText;
    private Button showListButton;
    private LinearLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ricevi l'oggetto Utente dall'intent
        Intent intent = getIntent();
        utente = (Utente) intent.getSerializableExtra("utente");

        // Recupera la lista degli utenti dall'applicazione globale
        MyApplication myApplication = (MyApplication) getApplicationContext();
        listaUtenti = myApplication.getListaUtenti();

        // Visualizza il benvenuto con l'username dell'utente
        benvenutoTextView = findViewById(R.id.BenvenutoUsername);
        benvenutoTextView.setText(getString(R.string.benvenuto_testo, utente.getUsername()));

        // Visualizza la città dell'utente
        TextView textViewCitta = findViewById(R.id.TextViewCitta);
        textViewCitta.setText(getString(R.string.citta_testo, utente.getCitta()));

        // Visualizza la data di nascita dell'utente
        TextView textViewDataNascita = findViewById(R.id.TextViewDataNascita);
        textViewDataNascita.setText(getString(R.string.data_nascita, utente.getDataNascita()));

        // Aggiungi il listener per il pulsante Logout
        logoutBtn = findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Torna alla pagina di login
                Intent loginIntent = new Intent(Home.this, Accedi.class);
                startActivity(loginIntent);
                finish(); // Chiudi l'Activity corrente
            }
        });

        // Aggiungi il listener per il pulsante Modifica Password
        modificaPasswordBtn = findViewById(R.id.modifica_password_btn);
        modificaPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se l'username e la password sono uguali ad "Admin"
                if (utente.getUsername().equals("admin") && utente.getPassword().equals("admin")) {
                    // Visualizza un messaggio di errore
                    Toast.makeText(Home.this, "La password dell'Admin non può essere modificata", Toast.LENGTH_SHORT).show();
                } else {
                    // Apri l'Activity per la modifica della password
                    Intent modificaPasswordIntent = new Intent(Home.this, ModificaPassword.class);
                    modificaPasswordIntent.putExtra("utente", utente);
                    startActivity(modificaPasswordIntent);
                }
            }
        });

        // Popola la lista degli utenti
        utentiListView = findViewById(R.id.utenti_list_view);
        adapter = new UtentiAdapter(this, listaUtenti);
        utentiListView.setAdapter(adapter);

        // Inizializza la visibilità della barra di ricerca
        searchLayout = findViewById(R.id.search_layout);
        searchLayout.setVisibility(View.GONE);

        // Inizializza la visibilità della barra di ricerca
        showListButton = findViewById(R.id.show_list_button);
        if (utente.getIsAdmin()) {
            showListButton.setVisibility(View.VISIBLE);
        } else {
            showListButton.setVisibility(View.GONE);
        }

        // Listener per mostrare/nascondere la lista degli utenti
        showListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchLayout.getVisibility() == View.GONE) {
                    searchLayout.setVisibility(View.VISIBLE);
                    utentiListView.setVisibility(View.VISIBLE);
                    showListButton.setText("Nascondi lista");
                } else {
                    searchLayout.setVisibility(View.GONE);
                    utentiListView.setVisibility(View.GONE);
                    showListButton.setText("Mostra lista");
                }
            }
        });

        // Inizializza l'oggetto ricercaEditText
        ricercaEditText = findViewById(R.id.ricerca_edit_text);

        // Aggiungi il listener per il pulsante "Cerca"
        Button cercaButton = findViewById(R.id.cerca_button);
        cercaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = ricercaEditText.getText().toString();
                adapter.getFilter().filter(searchText);
                adapter.notifyDataSetChanged(); // Aggiorna la lista degli utenti filtrati
            }
        });

        // Aggiungi il listener per la ricerca degli utenti
        ricercaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Non viene utilizzato in questo caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtra la lista degli utenti in base alla stringa di ricerca
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Non viene utilizzato in questo caso
            }
        });

        // Imposta l'altezza corretta della ListView per visualizzare tutti gli elementi
        setListViewHeight(utentiListView);
    }

    // Metodo per impostare l'altezza corretta della ListView
    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
