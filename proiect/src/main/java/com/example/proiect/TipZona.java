package com.example.proiect;

public class TipZona {
    public String tip;
    public String risc;
    public String descriere;

    public TipZona(String tip, String risc, String descriere) {
        this.tip = tip;
        this.risc = risc;
        this.descriere = descriere;
    }

    @Override
    public String toString() { return tip; }
}