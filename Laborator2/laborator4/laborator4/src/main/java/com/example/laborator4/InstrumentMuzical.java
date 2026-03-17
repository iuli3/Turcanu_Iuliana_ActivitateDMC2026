package com.example.laborator4;

import java.time.LocalDate;

public class InstrumentMuzical {

    public enum StareInstrument {
        NOU, UZAT, DEFECT
    }

    public static class InstrumentTehnic {
        public String denumire;
        public int serie;
        public boolean esteValid;
        public double pret;
        public StareInstrument stare;
        public LocalDate dataAchizitie;

        public InstrumentTehnic(String denumire, int serie, boolean esteValid,
                                double pret, StareInstrument stare, LocalDate dataAchizitie) {
            this.denumire = denumire;
            this.serie = serie;
            this.esteValid = esteValid;
            this.pret = pret;
            this.stare = stare;
            this.dataAchizitie = dataAchizitie;
        }
        @Override
        public String toString() {
            return "denumire: "+ denumire + " | " + pret + " RON" +
                    "\nSerie: " + serie + " | " +"stare: "+ stare +
                    "\n" + (esteValid ? "Disponibil" : "Indisponibil") +
                    " | " + dataAchizitie;
        }
    }
}