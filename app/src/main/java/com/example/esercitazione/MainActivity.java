package com.example.esercitazione;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inizializza l'istanza di MyApplication
        MyApplication myApp = (MyApplication) getApplicationContext();

        // Aggiungi l'utente admin alla lista degli utenti
        Utente adminUser = new Utente("admin", "Cagliari", "admin", "0/00/0000","res/drawable-v24/placeholder.png", true );
        myApp.getListaUtenti().add(adminUser);

        


        // Avvia l'activity "Accedi"
        startAccediActivity();
    }

    public void startAccediActivity() {
        Intent intent = new Intent(this, Accedi.class);
        startActivity(intent);
        finish();
    }
}