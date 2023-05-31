package com.example.esercitazione;
import com.example.esercitazione.Utente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Registrazione extends AppCompatActivity {

    private EditText dataNascita;
    private ImageView image;
    private List<Utente> utenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        // Inizializzo la lista degli utenti
        utenti = new ArrayList<>();

        // Ottieni riferimenti agli elementi del layout
        dataNascita = findViewById(R.id.dataNascita);
        image = findViewById(R.id.immagine);

        // Imposta il DatePicker per la data di nascita
        setupDatePicker();

        // Ottieni riferimenti agli elementi del layout
        EditText usernameEditText = findViewById(R.id.username);
        EditText cittaEditText = findViewById(R.id.citta);
        EditText passwordEditText = findViewById(R.id.password);
        EditText passwordEditText2 = findViewById(R.id.password2);

        Button registratiBtn = findViewById(R.id.Registratibtn);
        registratiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ottieni i valori inseriti dall'utente
                String username = usernameEditText.getText().toString().trim();
                String citta = cittaEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String password2 = passwordEditText2.getText().toString().trim();

                // Verifica se gli EditText sono vuoti
                if (checkEmptyEditText(usernameEditText) || checkEmptyEditText(cittaEditText) ||
                        checkEmptyEditText(passwordEditText) || checkEmptyEditText(passwordEditText2)) {
                    return;
                }

                // Verifica la validità delle password
                if (!isValidPassword(password)) {
                    passwordEditText.setError("La password deve contenere almeno 8 caratteri, una lettera minuscola, una lettera maiuscola, un numero e un carattere speciale.");
                    passwordEditText.requestFocus();
                    return;
                }

                // Verifica se le password corrispondono
                if (!validatePasswords(password, password2)) {
                    passwordEditText2.setError("Le password non corrispondono.");
                    passwordEditText2.requestFocus();
                    return;
                }

                // Verifica se l'utente è maggiorenne
                if (!isAdult(dataNascita.getText().toString())) {
                    dataNascita.setError("Devi essere maggiorenne");
                    dataNascita.requestFocus();
                    return;
                }
                // Creazione di un nuovo utente
                Utente nuovoUtente = new Utente(username, citta, password, dataNascita.getText().toString(), "res/drawable-v24/placeholder.png", false);
                // Ottieni l'istanza di MyApplication
                MyApplication myApp = (MyApplication) getApplicationContext();
                // Aggiungi il nuovo utente alla lista degli utenti registrati
                myApp.getListaUtenti().add(nuovoUtente);
                // Mostra un messaggio di registrazione avvenuta con successo
                Toast.makeText(Registrazione.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();

                // Torna alla schermata di accesso
                onBackPressed();
            }
        });
    }

    // Funzione per controllare se un EditText è vuoto
    private boolean checkEmptyEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            editText.setError("Il campo non può essere vuoto");
            return true;
        } else {
            editText.setError(null); // Rimuovi l'eventuale errore precedentemente impostato
            return false;
        }
    }

    // Funzione per impostare il DatePicker per la data di nascita
    private void setupDatePicker() {
        dataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendario = Calendar.getInstance();
                int anno = calendario.get(Calendar.YEAR);
                int mese = calendario.get(Calendar.MONTH);
                int giorno = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(Registrazione.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dataSelezionata = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        dataNascita.setText(dataSelezionata);
                    }
                }, anno, mese, giorno);

                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

                // Listener per controllare se la data selezionata corrisponde a un adulto
                datePicker.getDatePicker().init(anno, mese, giorno, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataSelezionata = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                        dataNascita.setText(dataSelezionata);

                        if (!isAdult(dataSelezionata)) {
                            dataNascita.setError("Devi essere maggiorenne");
                        } else {
                            dataNascita.setError(null);
                        }
                    }
                });

                datePicker.show();
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

    // Funzione per verificare se due password corrispondono
    private boolean validatePasswords(String password1, String password2) {
        return password1.equals(password2);
    }

    // Funzione per verificare se una data corrisponde a un adulto
    private boolean isAdult(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dateNascita = sdf.parse(dateString);
            Calendar calendarNascita = Calendar.getInstance();
            calendarNascita.setTime(dateNascita);

            Calendar calendarMaggiorenne = Calendar.getInstance();
            calendarMaggiorenne.add(Calendar.YEAR, -18);

            return calendarNascita.before(calendarMaggiorenne);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Accedi.class));
        finish();
    }
}

