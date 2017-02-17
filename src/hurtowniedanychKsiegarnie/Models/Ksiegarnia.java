/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hurtowniedanychKsiegarnie.Models;

/**
 *
 * @author daniel
 */
public class Ksiegarnia {
    
   String nazwa, region,miasto;
   int liczba;

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public void setLiczba(int liczba) {
        this.liczba = liczba;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getRegion() {
        return region;
    }

    public String getMiasto() {
        return miasto;
    }

    public int getLiczba() {
        return liczba;
    }
   
    public Ksiegarnia(String nazwa, String region, String miasto, int liczba) {
        this.nazwa = nazwa;
        this.region = region;
        this.miasto = miasto;
        this.liczba = liczba;
    }

  
   
    
    
    
}
