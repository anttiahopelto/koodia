package kuntosali;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author antti
 * @version 3.8.2020
 *
 */
public class Ryhmaliikunta implements Cloneable {

    private int tunnusNro;
    private int asiakasNro;
    private String nimi = "";
    private String viikonpv = "";
    private String klo = "";

    private static int seuraavaNro = 1;

    /**
     * @return ryhmaliikunnan kenttien lkm
     */
    public int getKenttia() {
        return 5;
    }


    /**
     * @return ekan täytettävän kentan indeksi
     */
    public int ekaKentta() {
        return 2;
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
            return "" + asiakasNro;
        case 2:
            return "" + nimi;
        case 3:
            return "" + viikonpv;
        case 4:
            return "" + klo;
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
     *   Ryhmaliikunta jasen = new Ryhmaliikunta();
     *   jasen.aseta(2,"Ankka") === null;
     *   jasen.aseta(3,"maanantai") === null; 
     *   jasen.aseta(4,"63.00-17.00") === "Anna muodossa 15.30-16.00";
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
            setAsiakasNro(Mjonot.erota(sb, '§', getAsiakasNro()));
            return null;
        case 2:
            nimi = tjono;
            return null;

        case 3:
            if (tjono.matches(
                    "maanantai|tiistai|keskiviikko|torstai|perjantai|lauantai|sunnuntai")) {
                viikonpv = tjono;
                return null;
            }
            return "Anna viikonpäivä";

        case 4:
            if (tjono.matches(
                    "^(0[0-9]|1[0-9]|2[0-3])\\.[0-5][0-9]-(0[0-9]|1[0-9]|2[0-3])\\.[0-5][0-9]$")) {
                klo = tjono;
                return null;
            }
            return "Anna muodossa 15.30-16.00";

        default:
            return "JeJee";
        }
    }


    /**
     * Alustetaan ryhmaliikunta
     */
    public Ryhmaliikunta() {
        // ei tehdä vielä mitään
    }


    /**
     * Alustetaan tietyn asiakkaan ryhmaliikunta
     * @param asiakasNro asikkaan yksilöivä nro
     */
    public Ryhmaliikunta(int asiakasNro) {
        this.asiakasNro = asiakasNro;
    }


    /**
     * getteri ryhmaliikunnan nimelle
     * @return palauttaa nimi attribuutin
     */
    public String getNimi() {
        return nimi;
    }


    /**
     * testailua varten aliohjelma, joka täyttää olion tiedot. Nimen perään tulee yksilöivä random arvo
     * @param numero mille asiakkaalle ryhmaliikunta sijoitetaan
     */
    public void taytaRyhmaliikunta(int numero) {
        asiakasNro = numero;
        this.nimi = "kuntopiiri" + Math.random();
        this.viikonpv = "torstai";
        this.klo = "18.00";
    }


    /**
     * Tulostetaan ryhmäliikunnan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", this.tunnusNro) + "  " + this.nimi);
        out.println("  Viikonpäivä: " + this.viikonpv);
        out.println("  Kellonaika: " + this.klo);
    }


    /**
     * Tulostetaan ryhmäliikunnan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa ryhmäliikunnalle seuraavan rekisterinumeron.
     * @return ryhmäliikunnan uusi tunnusNro
     * @example
     * <pre name="test">
     *   Ryhmaliikunta testi1 = new Ryhmaliikunta();
     *   testi1.getTunnusNro() === 0;
     *   testi1.rekisteroi();
     *   Ryhmaliikunta testi2 = new Ryhmaliikunta();
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
     * Palauttaa ryhmaliikunnan tunnusnumeron.
     * @return ryhmaliikunnan tunnusnumero
     */
    public int getTunnusNro() {
        return this.tunnusNro;
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
     * @param nro numero, joka asetetaan asiakasNro:ksi
     */
    public void setAsiakasNro(int nro) {
        asiakasNro = nro;

    }


    /**
     * Palautetaan mille asiakkaalle ryhmaliikunta kuuluu
     * @return jäsenen id
     */
    public int getAsiakasNro() {
        return asiakasNro;
    }


    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + asiakasNro + "|" + nimi + "|"
                + viikonpv + "|" + klo;
    }


    /**
     * Selvitää ryhmäliikunnan tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta ryhmäliikunnan tiedot otetaan
     * @example
     * <pre name="test">
     *   Ryhmaliikunta rl = new Ryhmaliikunta();
     *   rl.parse("   1   |  4  |   kuntopiiri  | torstai | 18.00 ");
     *   rl.getAsiakasNro() === 4;
     *   rl.toString()    === "1|4|kuntopiiri|torstai|18.00";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        asiakasNro = Mjonot.erota(sb, '|', asiakasNro);
        nimi = Mjonot.erota(sb, '|', nimi);
        viikonpv = Mjonot.erota(sb, '|', viikonpv);
        klo = Mjonot.erota(sb, '|', klo);
    }


    /**
     * testi pääohjelma yhdelle ryhmäliikunnalle
     * @param args ei käytössä
     */
    public static void main(String[] args) {

        Ryhmaliikunta ryhma1 = new Ryhmaliikunta(),
                ryhma2 = new Ryhmaliikunta();
        ryhma1.rekisteroi();
        ryhma2.rekisteroi();

        ryhma1.taytaRyhmaliikunta(1);
        ryhma2.taytaRyhmaliikunta(2);

        ryhma1.tulosta(System.out);
        ryhma2.tulosta(System.out);

    }


    /**
     * Tehdään identtinen klooni ryhmäliikunnasta
     * @return Object kloonattu rl
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Ryhmaliikunta har = new Ryhmaliikunta();
     *   har.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   Ryhmaliikunta kopio = har.clone();
     *   kopio.toString() === har.toString();
     *   har.parse("   1   |  11  |   Uinti  | 1949 | 22 t ");
     *   kopio.toString().equals(har.toString()) === false;
     * </pre>
     */
    @Override
    public Ryhmaliikunta clone() throws CloneNotSupportedException {
        return (Ryhmaliikunta) super.clone();
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
            return "AsiakasNro";
        case 2:
            return "Nimi";
        case 3:
            return "ViikonPv";
        case 4:
            return "Klo";
        default:
            return "jeeje";
        }
    }

}
