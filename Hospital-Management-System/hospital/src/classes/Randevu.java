/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author DUMAN
 */
public class Randevu {
    private String sira;
    private String bolum;
    private String doktor;
    private String hasta;
    private String randevu_saati;
    private String randevu_durumu;

    public Randevu(String sira, String bolum, String doktor, String hasta, String randevu_saati, String randevu_durumu) {
        this.sira = sira;
        this.bolum = bolum;
        this.doktor = doktor;
        this.hasta = hasta;
        this.randevu_saati = randevu_saati;
        this.randevu_durumu = randevu_durumu;
    }

    public String getSira() {
        return sira;
    }

    public void setSira(String sira) {
        this.sira = sira;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getDoktor() {
        return doktor;
    }

    public void setDoktor(String doktor) {
        this.doktor = doktor;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public String getRandevu_saati() {
        return randevu_saati;
    }

    public void setRandevu_saati(String randevu_saati) {
        this.randevu_saati = randevu_saati;
    }

    public String getRandevu_durumu() {
        return randevu_durumu;
    }

    public void setRandevu_durumu(String randevu_durumu) {
        this.randevu_durumu = randevu_durumu;
    }
    
    
}
