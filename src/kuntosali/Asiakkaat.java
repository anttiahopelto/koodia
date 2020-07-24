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
import java.util.Arrays;

/**
 * @author antti
 * @version 9.7.2020
 *
 */
public class Asiakkaat {

    private static final int MAX_JASENIA = 8;
    private int lkm = 0;
    private Asiakas[] alkiot = new Asiakas[MAX_JASENIA];
    private boolean muutettu = false;

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
    public Asiakas anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }


    /**
     * Lukee asiakkaat tiedostosta.
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        File ftied = new File("kuntosali/asiakkaat.dat");
        try (BufferedReader fi = new BufferedReader(
                new FileReader(ftied.getCanonicalPath()))) {
            String kokoNimi = fi.readLine();
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
     * @throws SailoException jos tallennus epäonnistuu
     * 
     */
    // TODO Kuntosalin nimi oikeasti haetaan jostain
    public void tallenna() throws SailoException {
        if (!muutettu)
            return;
        File ftied = new File("kuntosali/asiakkaat.dat");
        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            fo.println("Kuntosalin nimi");
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

}
