/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author DUMAN
 */
public class Appointment {
    private int id;
    private int bolumId;
    private int doktorId;
    private int hastaId;
    private String tarih;
    private int saatId;
    private char durum;

    public Appointment(int id, int bolumId, int doktorId, int hastaId, String tarih, int saatId, char durum) {
        this.id = id;
        this.bolumId = bolumId;
        this.doktorId = doktorId;
        this.hastaId = hastaId;
        this.tarih = tarih;
        this.saatId = saatId;
        this.durum = durum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBolumId() {
        return bolumId;
    }

    public void setBolumId(int bolumId) {
        this.bolumId = bolumId;
    }

    public int getDoktorId() {
        return doktorId;
    }

    public void setDoktorId(int doktorId) {
        this.doktorId = doktorId;
    }

    public int getHastaId() {
        return hastaId;
    }

    public void setHastaId(int hastaId) {
        this.hastaId = hastaId;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public int getSaatId() {
        return saatId;
    }

    public void setSaatId(int saatId) {
        this.saatId = saatId;
    }

    public char getDurum() {
        return durum;
    }

    public void setDurum(char durum) {
        this.durum = durum;
    }
    
}
