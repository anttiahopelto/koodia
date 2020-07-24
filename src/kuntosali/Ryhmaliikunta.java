package kuntosali;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author antti
 * @version 9.7.2020
 *
 */
public class Ryhmaliikunta {

    private int tunnusNro;
    private int asiakasNro;
    private String nimi = "";
    private String viikonpv = "";
    private String klo;

    private static int seuraavaNro = 1;

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

}
