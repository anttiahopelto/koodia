/**
 * 
 */
package kuntosali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * @author antti
 * @version 3.8.2020
 *
 */
public class Asiakkaat implements Iterable<Asiakas> {

    private static final int MAX_JASENIA = 8;
    private int lkm = 0;
    private Asiakas[] alkiot = new Asiakas[MAX_JASENIA];
    private boolean muutettu = false;
    private String kokoNimi = "";

    /**
     * muodostaja
     */
    public Asiakkaat() {
        // attribuuttien oma alustus on riittävä
    }


    /**
     * Lisää uuden asiakkaan tietorakenteeseen.  Ottaa asiakkaan omistukseensa.
     * @param asiakas lisätäävän asiakkaan viite.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Asiakas asiakas) {
        if (lkm >= alkiot.length) {
            alkiot = Arrays.copyOf(alkiot, alkiot.length + 10);
        }
        alkiot[lkm] = asiakas;
        lkm++;
        muutettu = true;
    }


    /**
     * Palauttaa viitteen i:teen asiakkaaseen.
     * @param i monennenko jäsenen viite halutaan
     * @return viite asiakkaahan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    protected Asiakas anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }


    /**
     * Lukee asiakkaat tiedostosta.
     * @param tiedosto tiedoston nimi
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Asiakkaat jasenet = new Asiakkaat();
     *  Asiakas aku1 = new Asiakas(), aku2 = new Asiakas(),aku3 = new Asiakas();
     *  aku1.taytaAsiakas();
     *  aku2.taytaAsiakas();
     *  aku3.taytaAsiakas();
     *  jasenet.lueTiedostosta("kuntosali/testi.dat"); 
     *  jasenet.lisaa(aku1);
     *  jasenet.lisaa(aku2);
     *  jasenet.lisaa(aku3);
     *  jasenet.tallenna("kuntosali/testi.dat");
     *  Iterator<Asiakas> i = jasenet.iterator();
     *  i.hasNext() === true;
     *  i.next() === aku2;
     * </pre>
     */
    public void lueTiedostosta(String tiedosto) throws SailoException {
        File ftied = new File(tiedosto);
        try (BufferedReader fi = new BufferedReader(
                new FileReader(ftied.getCanonicalPath()))) {
            kokoNimi = fi.readLine();
            if (kokoNimi == null)
                throw new SailoException("Kuntosalin nimi puutuu");
            String rivi = "";
            while (true) {
                rivi = fi.readLine();
                if (rivi == null)
                    break;
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Asiakas asiakas = new Asiakas();
                asiakas.parse(rivi);
                lisaa(asiakas);
            }
            muutettu = false;

        } catch (FileNotFoundException e) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea :(");
        } catch (IOException e) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea :(");
        }
    }


    /**
     * Tallentaa asiakkaat tiedostoon.
     * @param tiedosto tiedoston nimi
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * #PACKAGEIMPORT
     *  Asiakkaat asiakkaat = new Asiakkaat();
     *  Asiakas pitsi21 = new Asiakas(); pitsi21.taytaAsiakas();
     *  Asiakas pitsi11 = new Asiakas(); pitsi11.taytaAsiakas();
     *  Asiakas pitsi22 = new Asiakas(); pitsi22.taytaAsiakas();
     *  Asiakas pitsi12 = new Asiakas(); pitsi12.taytaAsiakas(); 
     *  Asiakas pitsi23 = new Asiakas(); pitsi23.taytaAsiakas(); 
     *  String tiedNimi = "testi.dat";
     *  File ftied = new File(tiedNimi);
     *  ftied.delete();
     *  asiakkaat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  asiakkaat.lisaa(pitsi21);
     *  asiakkaat.lisaa(pitsi11);
     *  asiakkaat.lisaa(pitsi22);
     *  asiakkaat.lisaa(pitsi12);
     *  asiakkaat.lisaa(pitsi23);
     *  asiakkaat.tallenna(tiedNimi);
     *  asiakkaat = new Asiakkaat();
     *  asiakkaat.lueTiedostosta(tiedNimi); 
     *  Iterator<Asiakas> i = asiakkaat.iterator();
     *  i.next().toString() === pitsi21.toString();
     *  i.next().toString() === pitsi11.toString();
     *  i.next().toString() === pitsi22.toString();
     *  i.next().toString() === pitsi12.toString();
     *  i.next().toString() === pitsi23.toString();
     *  i.hasNext() === false;
     *  asiakkaat.lisaa(pitsi23);
     *  asiakkaat.tallenna(tiedNimi);
     * </pre>
     * @throws SailoException jos tallennus epäonnistuu
     * 
     */
    public void tallenna(String tiedosto) throws SailoException {
        if (!muutettu)
            return;
        File ftied = new File(tiedosto);
        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            fo.println(getKokoNimi());
            for (int i = 0; i < getLkm(); i++) {
                Asiakas asiakas = anna(i);
                fo.println(asiakas.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName()
                    + " kirjoittamisessa ongelma");
        }

        muutettu = false;
    }


    /**Palauttaa kuntosalin kokonimen
     * @return kuntosalin kokonimi merkkijonona
     */
    public String getKokoNimi() {
        return kokoNimi;
    }


    /**
     * Palauttaa kuntosalin asiakkaiden lukumäärän
     * @return asiakkaiden lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Testiohjelma asiakkaille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Asiakkaat asiakkaat = new Asiakkaat();

        Asiakas ismo1 = new Asiakas(), ismo2 = new Asiakas();
        ismo1.rekisteroi();
        ismo1.taytaAsiakas();
        ismo2.rekisteroi();
        ismo2.taytaAsiakas();

        asiakkaat.lisaa(ismo1);
        asiakkaat.lisaa(ismo2);

        System.out.println("============= Asiakkaat testi =================");

        for (int i = 0; i < asiakkaat.getLkm(); i++) {
            Asiakas asiakas = asiakkaat.anna(i);
            System.out.println("Jäsen nro: " + i);
            asiakas.tulosta(System.out);
        }
    }


    /**
     * Korvaa asiakkaan tietorakenteessa.  Ottaa asiakkaan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva asiakas.  Jos ei löydy,
     * niin lisätään uutena asiakkaana.
     * @param asiakas lisätäävän asiakkaan viite.  Huom tietorakenne muuttuu omistajaksi
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Asiakkaat asiakkaat = new Asiakkaat();
     * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas();
     * aku1.rekisteroi(); aku2.rekisteroi();
     * asiakkaat.getLkm() === 0;
     * asiakkaat.korvaaTaiLisaa(aku1); asiakkaat.getLkm() === 1;
     * asiakkaat.korvaaTaiLisaa(aku2); asiakkaat.getLkm() === 2;
     * </pre>
     */
    public void korvaaTaiLisaa(Asiakas asiakas) {
        int id = asiakas.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getTunnusNro() == id) {
                alkiot[i] = asiakas;
                muutettu = true;
                return;
            }
        }
        lisaa(asiakas);

    }


    /** 
     * Poistaa asiakkaan jolla on valittu tunnusnumero  
     * @param id poistettavan asiakkaan tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Asiakkaat jasenet = new Asiakkaat(); 
     * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas(), aku3 = new Asiakas(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
     * jasenet.poista(id1+1) === 1; 
     * jasenet.annaId(id1+1) === null; jasenet.getLkm() === 2; 
     * jasenet.poista(id1) === 1; jasenet.getLkm() === 1; 
     * jasenet.poista(id1+3) === 0; jasenet.getLkm() === 1; 
     * </pre> 
     *  
     */
    public int poista(int id) {
        int ind = etsiId(id);
        if (ind < 0)
            return 0;
        lkm--;
        for (int i = ind; i < lkm; i++)
            alkiot[i] = alkiot[i + 1];
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }


    /** 
     * Etsii asiakkaan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen asiakkaan indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Asiakkaat jasenet = new Asiakkaat(); 
     * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas(), aku3 = new Asiakas(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
     * jasenet.etsiId(id1+1) === 1; 
     * jasenet.etsiId(id1+2) === 2; 
     * </pre> 
     */
    public int etsiId(int id) {
        for (int i = 0; i < lkm; i++)
            if (id == alkiot[i].getTunnusNro())
                return i;
        return -1;
    }


    /** 
     * Etsii Asiakkaan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return asiakas jolla etsittävä id tai null 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Asiakkaat jasenet = new Asiakkaat(); 
     * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas(), aku3 = new Asiakas(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
     * jasenet.annaId(id1  ) == aku1 === true; 
     * jasenet.annaId(id1+1) == aku2 === true; 
     * jasenet.annaId(id1+2) == aku3 === true; 
     * </pre> 
     */
    public Asiakas annaId(int id) {
        for (Asiakas asiakas : this) {
            if (id == asiakas.getTunnusNro())
                return asiakas;
        }
        return null;
    }


    /**
     * @param hakuehto Millä ehdolla haetaan
     * @param k mistä kentästä
     * @return tietorakenne löydetyistä asiakkaista
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Asiakkaat jasenet = new Asiakkaat(); 
     * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas(), aku3 = new Asiakas(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
     * jasenet.poista(id1+1) === 1; 
     * jasenet.annaId(id1+1) === null; jasenet.getLkm() === 2; 
     * jasenet.poista(id1) === 1; jasenet.getLkm() === 1; 
     * jasenet.poista(id1+3) === 0; jasenet.getLkm() === 1; 
     * </pre> 
     */
    public Collection<Asiakas> etsi(String hakuehto, int k) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0)
            ehto = hakuehto;
        int hakukentta = k;
        if (hakukentta < 0)
            hakukentta = 1;
        Collection<Asiakas> loytyneet = new ArrayList<Asiakas>();
        for (Asiakas asiakas : this) {
            if (WildChars.onkoSamat(asiakas.anna(hakukentta), ehto))
                loytyneet.add(asiakas);
        }
        return loytyneet;
    }


    /**
     * @return asiakas iteraattori
     */
    @Override
    public Iterator<Asiakas> iterator() {
        return new AsiakkaatIterator();
    }

    /**
     * Luokka asiakkaiden iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Asiakkaat jasenet = new Asiakkaat();
     * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas();
     * aku1.rekisteroi(); aku2.rekisteroi();
     *
     * jasenet.lisaa(aku1); 
     * jasenet.lisaa(aku2); 
     * jasenet.lisaa(aku1); 
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Asiakas jasen:jasenet)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+jasen.getTunnusNro());           
     * 
     * String tulos = " " + aku1.getTunnusNro() + " " + aku2.getTunnusNro() + " " + aku1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Asiakas>  i=jasenet.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Asiakas jasen = i.next();
     *   ids.append(" "+jasen.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Asiakas>  i=jasenet.iterator();
     * i.next() == aku1  === true;
     * i.next() == aku2  === true;
     * i.next() == aku1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class AsiakkaatIterator implements Iterator<Asiakas> {
        private int kohdalla = 0;

        /**
         * Onko olemassa vielä seuraavaa asiakasta
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä asiakkaita
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava asiakkaan
         * @return seuraava asiakas
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Asiakas next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("Ei oo seuraavaa");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }

}
