package com.example.esercitazione;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ModificaPassword extends AppCompatActivity {

    private EditText vecchiaPasswordEditText;
    private EditText nuovaPasswordEditText;
    private EditText confermaPasswordEditText;
    private Button confermaButton;
    private Button tornaHomeButton;
    private MyApplication myApp;
    private Utente utente;

    private List<Utente> utenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        // Ottieni l'istanza di MyApplication
        myApp = (MyApplication) getApplicationContext();
        // Ottenere la lista degli utenti dalla variabile globale
        utenti = myApp.getListaUtenti();

        // Ricevi l'oggetto Utente dall'intent
        Intent intent = getIntent();
        utente = (Utente) intent.getSerializableExtra("utente");

        TextView textViewUsername = findViewById(R.id.TextViewUsername);
        textViewUsername.setText(getString(R.string.Nome, utente.getUsername()));

        // Visualizza la città dell'utente
        TextView textViewCitta = findViewById(R.id.TextViewCitta);
        textViewCitta.setText(getString(R.string.citta_testo, utente.getCitta()));

        // Visualizza la data di nascita dell'utente
        TextView textViewDataNascita = findViewById(R.id.TextViewDataNascita);
        textViewDataNascita.setText(getString(R.string.data_nascita, utente.getDataNascita()));

        vecchiaPasswordEditText = findViewById(R.id.editTextVecchiaPassword);
        nuovaPasswordEditText = findViewById(R.id.editTextNuovaPassword);
        confermaPasswordEditText = findViewById(R.id.editTextConfermaPassword);
        confermaButton = findViewById(R.id.buttonConferma);
        tornaHomeButton = findViewById(R.id.buttonTornaHome);

        confermaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vecchiaPassword = vecchiaPasswordEditText.getText().toString().trim();
                String nuovaPassword = nuovaPasswordEditText.getText().toString().trim();
                String confermaPassword = confermaPasswordEditText.getText().toString().trim();

                // Verifica se i campi sono vuoti
                if (TextUtils.isEmpty(vecchiaPassword) || TextUtils.isEmpty(nuovaPassword) || TextUtils.isEmpty(confermaPassword)) {
                    Toast.makeText(ModificaPassword.this, "Riempi tutti i campi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica se la vecchia password corrisponde a quella dell'utente
                if (!vecchiaPassword.equals(utente.getPassword())) {
                    vecchiaPasswordEditText.setError("Vecchia password errata");
                    vecchiaPasswordEditText.requestFocus();
                    return;
                }

                // Verifica se la nuova password corrisponde alla conferma password
                if (!nuovaPassword.equals(confermaPassword)) {
                    confermaPasswordEditText.setError("Le password non corrispondono");
                    confermaPasswordEditText.requestFocus();
                    return;
                }

                // Verifica se la nuova password è uguale alla vecchia password
                if (nuovaPassword.equals(utente.getPassword())) {
                    nuovaPasswordEditText.setError( "La nuova password deve essere diversa dalla vecchia password");
                    nuovaPasswordEditText.requestFocus();
                    return;
                }

                // Verifica se la nuova password soddisfa i requisiti
                if (!isValidPassword(nuovaPassword)) {
                    nuovaPasswordEditText.setError("La password deve contenere almeno 8 caratteri, una lettera minuscola, una lettera maiuscola, un numero e un carattere speciale.");
                    nuovaPasswordEditText.requestFocus();
                    return;
                }

                // Rimuovi l'utente dalla lista utenti
                myApp.removeUtente(utente);

                // Modifica la password dell'utente
                utente.setPassword(nuovaPassword);

                // Re-inserisci l'utente con la nuova password nella lista utenti
                utenti.add(utente);

                // Aggiorna la lista utenti nell'oggetto MyApplication
                myApp.setListaUtenti(utenti);

                Toast.makeText(ModificaPassword.this, "Password modificata con successo", Toast.LENGTH_SHORT).show();

            }
        });

        tornaHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Controllo degli errori al cambio di focus degli EditText
        vecchiaPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String vecchiaPassword = vecchiaPasswordEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(vecchiaPassword)) {
                        vecchiaPasswordEditText.setError("Inserisci la vecchia password");
                    }
                }
            }
        });

        nuovaPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String nuovaPassword = nuovaPasswordEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(nuovaPassword)) {
                        nuovaPasswordEditText.setError("Inserisci la nuova password");
                    }
                }
            }
        });

        confermaPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String confermaPassword = confermaPasswordEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(confermaPassword)) {
                        confermaPasswordEditText.setError("Inserisci la conferma password");
                    }
                }
            }
        });
    }

    // Funzione per verificare se una password è valida
    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                if (isSpecialCharacter(c)) {
                    hasSpecialChar = true;
                }
            }
        }

        return hasLowerCase && hasUpperCase && hasDigit && hasSpecialChar;
    }

    // Funzione per verificare se un carattere è un carattere speciale
    private boolean isSpecialCharacter(char c) {
        String specialCharacters = "@#$%^&+=!.";
        return specialCharacters.contains(String.valueOf(c));
    }
}
