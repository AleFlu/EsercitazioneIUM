package com.example.esercitazione;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Accedi extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button accediBtn;
    private TextView registrazioneTextView;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accedi);

        // Ottieni riferimenti agli elementi del layout
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        accediBtn = findViewById(R.id.accedibtn);
        registrazioneTextView = findViewById(R.id.registerbtn);

        String text = "Non sei ancora registrato?";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        registrazioneTextView.setText(content);

        // Ottieni l'istanza di MyApplication
        myApp = (MyApplication) getApplicationContext();

        // Imposta il listener del pulsante "Accedi"
        accediBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ottieni l'username inserito
                String username = usernameEditText.getText().toString().trim();

                // Verifica se l'username è stato inserito correttamente
                if (username.isEmpty()) {
                    usernameEditText.setError("Inserisci l'username");
                    return;
                }

                // Verifica se il campo password è visibile
                if (passwordEditText.getVisibility() != View.VISIBLE) {
                    // Il campo password non è ancora visibile, mostra il campo password
                    passwordEditText.setVisibility(View.VISIBLE);
                    passwordEditText.requestFocus();
                    return;
                }

                // Ottieni la password inserita
                String password = passwordEditText.getText().toString().trim();

                // Verifica se la password è stata inserita correttamente
                if (password.isEmpty()) {
                    passwordEditText.setError("Inserisci la password");
                    return;
                }

                // Verifica le credenziali
                if (verificaCredenziali(username, password)) {
                    // Credenziali corrette
                    Toast.makeText(Accedi.this, "Accesso effettuato con successo.", Toast.LENGTH_SHORT).show();
                    // Trova l'utente corrispondente alle credenziali
                    Utente utente1 = trovaUtente(username, password);

                    // Passa all'activity Home e passa l'oggetto Utente come parametro
                    Intent intent = new Intent(Accedi.this, Home.class);
                    intent.putExtra("utente", utente1);
                    startActivity(intent);
                } else {
                    // Credenziali non corrette
                    Toast.makeText(Accedi.this, "Credenziali non valide.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Imposta il listener del pulsante "Registrazione"
        registrazioneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reindirizza all'activity di registrazione
                Intent intent = new Intent(Accedi.this, Registrazione.class);
                startActivity(intent);
            }
        });
    }

    private boolean verificaCredenziali(String username, String password) {
        // Controlla se le credenziali corrispondono ad un utente nella lista condivisa
        for (Utente utente : myApp.getListaUtenti()) {
            if (utente.getUsername().equals(username) && utente.getPassword().equals(password)) {
                return true; // Credenziali corrette
            }
        }
        return false; // Credenziali non corrette
    }

    private Utente trovaUtente(String username, String password) {
        // Scorri la lista degli utenti
        for (Utente utente : myApp.getListaUtenti()) {
            if (utente.getUsername().equals(username) && utente.getPassword().equals(password)) {
                return utente; // Restituisci l'utente corrispondente alle credenziali
            }
        }
        return null; // Se l'utente non viene trovato, restituisci null
    }
}