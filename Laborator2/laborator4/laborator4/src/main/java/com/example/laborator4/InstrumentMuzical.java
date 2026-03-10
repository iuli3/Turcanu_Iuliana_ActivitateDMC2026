package com.example.laborator4;

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

        public InstrumentTehnic(String denumire, int serie, boolean esteValid,
                                double pret, StareInstrument stare) {
            this.denumire = denumire;
            this.serie = serie;
            this.esteValid = esteValid;
            this.pret = pret;
            this.stare = stare;
        }
    }
}