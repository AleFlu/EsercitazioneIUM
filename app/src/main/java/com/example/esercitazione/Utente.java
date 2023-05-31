package com.example.esercitazione;

import java.io.Serializable;
import java.util.Objects;

// Classe Utente
public class Utente implements Serializable {
    // Resto del codice della classe Utente

    private String nomeUtente;
    private String citta;
    private String password;
    private String dataNascita;
    private String immagineProfilo;
    private Boolean isAdmin;

    public Utente(String nomeUtente, String citta, String password, String dataNascita) {
        this.nomeUtente = nomeUtente;
        this.citta = citta;
        this.password = password;
        this.dataNascita = dataNascita;

    }

    public Utente(String nomeUtente, String citta, String password, String dataNascita, String immagineProfilo) {
        this.nomeUtente = nomeUtente;
        this.citta = citta;
        this.password = password;
        this.dataNascita = dataNascita;
        this.immagineProfilo = immagineProfilo;
    }

    public Utente(String nomeUtente, String citta, String password, String dataNascita, String immagineProfilo, Boolean isAdmin) {
        this.nomeUtente = nomeUtente;
        this.citta = citta;
        this.password = password;
        this.dataNascita = dataNascita;
        this.immagineProfilo = immagineProfilo;
        this.isAdmin = isAdmin;
    }



    // Metodi getter e setter
    public String getUsername() {
        return nomeUtente;
    }

    public void setUsername(String username) {
        this.nomeUtente = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getImmagineProfilo() {
        return immagineProfilo;
    }

    public void setImmagineProfilo(String immagineProfilo) {
        this.immagineProfilo = immagineProfilo;
    }
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Utente other = (Utente) o;
        return Objects.equals(nomeUtente, other.nomeUtente) &&
                Objects.equals(password, other.password);
    }
}
