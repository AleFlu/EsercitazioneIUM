package com.example.esercitazione;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private List<Utente> listaUtenti;

    @Override
    public void onCreate() {
        super.onCreate();

        listaUtenti = new ArrayList<>();
    }

    public List<Utente> getListaUtenti() {
        return listaUtenti;
    }

    public void setListaUtenti(List<Utente> listaUtenti) {
        this.listaUtenti = listaUtenti;
    }

    public void removeUtente(Utente utente) {
        listaUtenti.remove(utente);
    }
}