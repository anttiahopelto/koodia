/**
 * 
 */
package kuntosali;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author antti
 * @version 9.7.2020
 *
 */
public class Asiakas {

    private int tunnusNro;
    private String nimi = "";

    private String katuosoite = "";
    private String postinumero = "";
    private String postiosoite = "";
    private String puhelinnumero = "";
    private String email = "";
    private String alkamispv = "";
    private String loppumispv = "";

    private static int seuraavaNro = 1;

    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro)
            seuraavaNro = tunnusNro + 1;
    }


    /**
     * @return asiakkaan nimi
     * @example
     * <pre name="test">
     *   Asiakas asiakas = new Asiakas();
     *   asiakas.taytaAsiakas();
     *   asiakas.getNimi() =R= "Ismo Laitela .*";
     * </pre>
     */
    public String getNimi() {
        return this.nimi;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä oletus tiedot asiakkaalle. 
     */
    public void taytaAsiakas() {

        this.nimi = "Ismo Laitela " + Math.random();
        this.katuosoite = "Pihlajakatu 23";
        this.postinumero = "00140";
        this.postiosoite = "Helsinki";
        this.puhelinnumero = "0123456789";
        this.email = "ismo.laitela@gmail.com";
        this.alkamispv = "01.01.2020";
        this.loppumispv = "01.03.2020";

    }


    /**
     * Tulostetaan asiakkaan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) + "  " + nimi);
        out.println("  " + katuosoite + "  " + postinumero + " " + postiosoite);
        out.println("  Puhelinnumero: " + puhelinnumero);
        out.println("  Sähköpostiosoite: " + email);
        out.println("  Liittynyt: " + alkamispv + ".");
        out.println("  Päättymispvm: " + loppumispv);
    }


    /**
     * Tulostetaan asiakkaan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa Asiakkaalle seuraavan rekisterinumeron.
     * @return asiakkaan uusi tunnusNro
     * @example
     * <pre name="test">
     *   Asiakas testi1 = new Asiakas();
     *   testi1.getTunnusNro() === 0;
     *   testi1.rekisteroi();
     *   Asiakas testi2 = new Asiakas();
     *   testi2.rekisteroi();
     *   int n1 = testi1.getTunnusNro();
     *   int n2 = testi2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palauttaa asiakkaan tunnusnumeron.
     * @return asiakkaan tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Palauttaa asiakkaan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return asiakas tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Asiakas asiakas = new Asiakas();
     *   asiakas.parse("   3  |  Ankka Aku   | 030201-111C");
     *   asiakas.toString().startsWith("3|Ankka Aku|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * </pre>  
     */

    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + nimi + "|" + katuosoite + "|"
                + postinumero + "|" + postiosoite + "|" + puhelinnumero + "|"
                + email + "|" + alkamispv + "|" + loppumispv;
    }


    /**
     * Selvitää asiakkaan tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta jäsenen tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Asiakas asiakas = new Asiakas();
     *   asiakas.parse("   3  |  Ankka Aku   | 030201-111C");
     *   asiakas.getTunnusNro() === 3;
     *   asiakas.toString().startsWith("3|Ankka Aku|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   asiakas.rekisteroi();
     *   int n = asiakas.getTunnusNro();
     *   asiakas.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   asiakas.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   asiakas.getTunnusNro() === n+20+1;
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        katuosoite = Mjonot.erota(sb, '|', katuosoite);
        postinumero = Mjonot.erota(sb, '|', postinumero);
        postiosoite = Mjonot.erota(sb, '|', postiosoite);
        puhelinnumero = Mjonot.erota(sb, '|', puhelinnumero);
        email = Mjonot.erota(sb, '|', email);
        alkamispv = Mjonot.erota(sb, '|', alkamispv);
        loppumispv = Mjonot.erota(sb, '|', loppumispv);

    }


    /**
     * Testi pää ohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {

        Asiakas asiakas1 = new Asiakas(), asiakas2 = new Asiakas();
        asiakas1.rekisteroi();
        asiakas2.rekisteroi();

        asiakas1.taytaAsiakas();
        asiakas2.taytaAsiakas();

        asiakas1.tulosta(System.out);
        asiakas2.tulosta(System.out);

    }

}
