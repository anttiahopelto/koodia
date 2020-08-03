/**
 * 
 */
package kuntosali;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author antti
 * @version 3.8.2020
 *
 */
public class Asiakas implements Cloneable {

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
     * Palauttaa jäsenen kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    public int getKenttia() {
        return 9;
    }


    /**
     * Eka kenttä joka on mielekäs kysyttäväksi
     * @return eknn kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }


    /**
     * Alustetaan asiakkaan merkkijono-attribuuti tyhjiksi jonoiksi
     * ja tunnusnro = 0.
     */
    public Asiakas() {
        // TODO oikea alustus
    }


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

        this.nimi = "Ismo Laitela ";
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
     */

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
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
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));

    }


    /**
     * Tutkii onko jäsenen tiedot samat kuin parametrina tuodun jäsenen tiedot
     * @param asiakas johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Asiakas jasen1 = new Asiakas();
     *   jasen1.parse("   3  |  Ankka Aku   | 030201-111C");
     *   Asiakas jasen2 = new Asiakas();
     *   jasen2.parse("   3  |  Ankka Aku   | 030201-111C");
     *   Asiakas jasen3 = new Asiakas();
     *   jasen3.parse("   3  |  Ankka Aku   | 030201-115H");
     *   
     *   jasen1.equals(jasen2) === true;
     *   jasen2.equals(jasen1) === true;
     *   jasen1.equals(jasen3) === false;
     *   jasen3.equals(jasen2) === false;
     * </pre>
     */
    public boolean equals(Asiakas asiakas) {
        if (asiakas == null)
            return false;
        for (int k = 0; k < getKenttia(); k++)
            if (!anna(k).equals(asiakas.anna(k)))
                return false;
        return true;
    }


    @Override
    public boolean equals(Object asiakas) {
        if (asiakas instanceof Asiakas)
            return equals((Asiakas) asiakas);
        return false;
    }


    /**
     * Tehdään identtinen klooni asiakkaasta
     * @return Object kloonattu asiakas
     * @example
     * <pre name="test">
     *  #THROWS CloneNotSupportedException 
     *   Asiakas asiakas = new Asiakas();
     *   asiakas.parse("   3  |  Ankka Aku   | 123");
     *   Asiakas kopio = asiakas.clone();
     *   kopio.toString() === asiakas.toString();
     *   asiakas.parse("   4  |  Ankka Tupu   | 123");
     *   kopio.toString().equals(asiakas.toString()) === false;
     * 
     * </pre>
     */

    @Override
    public Asiakas clone() throws CloneNotSupportedException {
        Asiakas uusi;
        uusi = (Asiakas) super.clone();
        return uusi;
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


    /**
     * @param k mones tietokenttä
     * @return k tietokentän sisältö merkkijonona
     */
    public String anna(int k) {
        switch (k) {
        case 0:
            return "" + tunnusNro;
        case 1:
            return "" + nimi;
        case 2:
            return "" + puhelinnumero;
        case 3:
            return "" + katuosoite;
        case 4:
            return "" + postinumero;
        case 5:
            return "" + postiosoite;
        case 6:
            return "" + email;
        case 7:
            return "" + alkamispv;
        case 8:
            return "" + loppumispv;
        default:
            return "Jees Jees";
        }
    }


    /**
     *  * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     *   Asiakas jasen = new Asiakas();
     *   jasen.aseta(1,"Ankka Aku") === null;
     *   jasen.aseta(2,"030201-111C") === null; 
     *   jasen.aseta(9,"1940") === null;
     * </pre>
    
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch (k) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2:
            if (jono.matches("^[0-9]{10}")) {
                puhelinnumero = tjono;
                return null;
            }
            return "puhelinnumero voi sisältää vain numeroita";

        case 3:
            katuosoite = tjono;
            return null;
        case 4:
            if (jono.matches("^[0-9]{5}")) {
                postinumero = tjono;
                return null;
            }
            return "Postinumeron täytyy olla 5 numeroa pitkä";
        case 5:
            if (jono.matches("^[a-zA-Z]*$")) {
                postiosoite = tjono;
                return null;
            }
            return "Postiosoite voi sisältää vain kirjaimia";

        case 6:
            if (jono.matches("^(.+)@(.+)$")) {
                email = tjono;
                return null;
            }
            return "anna sähköposti muodossa sahkoposti@email.com";

        case 7:
            if (jono.matches("\\d{4}-\\d{2}-\\d{2}")) {
                alkamispv = tjono;
                return null;
            }
            return "Anna pvm muodossa VVVV-KK-PV";
        case 8:
            if (jono.matches("\\d{4}-\\d{2}-\\d{2}")) {
                loppumispv = tjono;
                return null;
            }
            return "Anna pvm muodossa VVVV-KK-PV";
        default:
            return "JeJee";
        }
    }


    /**
     * @param k Palauttaa asiakkaan K kentän kysymyksen
     * @return k kenttää vastaava kysymys
     */
    public String getKysymys(int k) {
        switch (k) {
        case 0:
            return "Tunnus nro";
        case 1:
            return "nimi";
        case 2:
            return "puhelinnumero";
        case 3:
            return "katuosoite";
        case 4:
            return "postinumero";
        case 5:
            return "postiosoite";
        case 6:
            return "sähköposti";
        case 7:
            return "alkamispv";
        case 8:
            return "loppumispv";
        default:
            return "jeeje";
        }
    }


    @Override
    public int hashCode() {
        return tunnusNro;
    }


    /**getteri loppumispv:lle
     * @return loppumisPv 
     */
    public String getLoppumisPv() {
        return loppumispv;

    }

}
