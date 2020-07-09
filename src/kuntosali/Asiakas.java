/**
 * 
 */
package kuntosali;

import java.io.*;

/**
 * @author antti
 * @version 9.7.2020
 *
 */
public class Asiakas {
    
    
    
    private int        tunnusNro;
    private String     nimi           = "";
    
    private String     katuosoite     = "";
    private String     postinumero    = "";
    private String     postiosoite    = "";
    private String     puhelinnumero  = "";
    private String     email          = "";
    private String     alkamispv      = "";
    private String     loppumispv     = "";
    
    private static int seuraavaNro    = 1;

    
    

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
        out.println("  Päättymispvm: "  + loppumispv);
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
