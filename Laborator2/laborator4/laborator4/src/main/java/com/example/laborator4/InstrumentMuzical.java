package com.example.laborator4;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;

public class InstrumentMuzical {

    public enum StareInstrument {
        NOU, UZAT, DEFECT
    }

    public static class InstrumentTehnic implements Parcelable {
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

        protected InstrumentTehnic(Parcel in)
        {
            denumire = in.readString();
            serie = in.readInt();
            esteValid=in.readByte()!=0;
            pret=in.readDouble();
            stare=StareInstrument.valueOf(in.readString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dataAchizitie=LocalDate.parse(in.readString());
            }

        }

        public static final Creator<InstrumentTehnic> CREATOR= new Creator<InstrumentTehnic>() {
            @Override
            public InstrumentTehnic createFromParcel(Parcel in) {
                return new InstrumentTehnic(in);
            }

            @Override
            public InstrumentTehnic[] newArray(int size) {
                return new InstrumentTehnic[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(denumire);
            dest.writeInt(serie);
            dest.writeByte((byte) (esteValid ? 1 : 0));
            dest.writeDouble(pret);
            dest.writeString(stare.name());
            dest.writeString(dataAchizitie != null ? dataAchizitie.toString() : "");
        }

        @Override
        public int describeContents() {
            return 0;
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