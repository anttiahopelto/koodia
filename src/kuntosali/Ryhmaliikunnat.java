package kuntosali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Kuntosalin Ryhmaliikunnat, osaa mm. lisätä uuden ryhmaliikunnan
 * @author antti
 * @version 5.7.2020
 *
 */
public class Ryhmaliikunnat implements Iterable<Ryhmaliikunta> {

    private boolean muutettu = false;
    private String tiedostonNimi = "kuntosali/ryhmaliikunnat";

    /** Taulukko ryhmaliikunnoista */
    private final Collection<Ryhmaliikunta> alkiot = new ArrayList<Ryhmaliikunta>();

    /**
     * Ryhmaliikuntojen alustaminen
     */
    public Ryhmaliikunnat() {
        //
    }


    /**
     * Lisää uuden ryhmaliikunnan tietorakenteeseen.  Ottaa ryhmaliikunnan omistukseensa.
     * @param ryhma lisättävä ryhmaliikunta.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Ryhmaliikunta ryhma) {
        alkiot.add(ryhma);
        muutettu = true;
    }


    /**
     * @throws SailoException jos lukeminen epäonnistuu
     */
    // TODO Testit
    public void lueTiedostosta() throws SailoException {
        try (BufferedReader fi = new BufferedReader(
                new FileReader(getTiedostonNimi()))) {

            String rivi;
            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Ryhmaliikunta ryhmaliikunta = new Ryhmaliikunta();
                ryhmaliikunta.parse(rivi); // voisi olla virhekäsittely
                lisaa(ryhmaliikunta);
            }
            muutettu = false;

        } catch (FileNotFoundException e) {
            throw new SailoException(
                    "Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch (IOException e) {
            throw new SailoException(
                    "Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }


    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonNimi = tied;
    }


    /**
     * Tallentaa harrastukset tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu)
            return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if ... System.err.println("Ei voi nimetä");

        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            for (Ryhmaliikunta rl : this) {
                fo.println(rl.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName()
                    + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonNimi + ".bak";
    }


    /**
     * @return tallennustiedoston nimi, jota käytetään tallennukseen
     */
    public String getTiedostonNimi() {
        return tiedostonNimi + ".dat";
    }


    /**
     * Palauttaa kuntosalin ryhmaliikuntojen lukumäärän
     * @return ryhmaliikuntojen lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien ryhmaliikuntojen läpikäymiseen
     * @return ryhmaliikuntaiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Ryhmaliikunnat ryhmat = new Ryhmaliikunnat();
     *  Ryhmaliikunta pitsi21 = new Ryhmaliikunta(2); ryhmat.lisaa(pitsi21);
     *  Ryhmaliikunta pitsi11 = new Ryhmaliikunta(1); ryhmat.lisaa(pitsi11);
     *  Ryhmaliikunta pitsi22 = new Ryhmaliikunta(2); ryhmat.lisaa(pitsi22);
     *  Ryhmaliikunta pitsi12 = new Ryhmaliikunta(1); ryhmat.lisaa(pitsi12);
     *  Ryhmaliikunta pitsi23 = new Ryhmaliikunta(2); ryhmat.lisaa(pitsi23);
     * 
     *  Iterator<Ryhmaliikunta> i2=ryhmat.iterator();
     *  i2.next() === pitsi21;
     *  i2.next() === pitsi11;
     *  i2.next() === pitsi22;
     *  i2.next() === pitsi12;
     *  i2.next() === pitsi23;
     *  i2.next() === pitsi12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int anrot[] = {2,1,2,1,2};
     *  
     *  for ( Ryhmaliikunta ryhma:ryhmat ) { 
     *    ryhma.getAsiakasNro() === anrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Ryhmaliikunta> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki asiakkaan ryhmäliikunnat
     * @param tunnusnro asiakkaan tunnusnumero jolle ryhmäliikuntoja haetaan
     * @return tietorakenne jossa viiteet löydetteyihin harrastuksiin
     */
    public List<Ryhmaliikunta> annaRyhmaliikunnat(int tunnusnro) {
        List<Ryhmaliikunta> loydetyt = new ArrayList<Ryhmaliikunta>();
        for (Ryhmaliikunta ryhma : alkiot)
            if (ryhma.getAsiakasNro() == tunnusnro)
                loydetyt.add(ryhma);
        return loydetyt;
    }


    /**
     * Testiohjelma harrastuksille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ryhmaliikunnat ryhmat = new Ryhmaliikunnat();
        Ryhmaliikunta kuntopiiri1 = new Ryhmaliikunta();
        kuntopiiri1.taytaRyhmaliikunta(2);
        Ryhmaliikunta kuntopiiri2 = new Ryhmaliikunta();
        kuntopiiri2.taytaRyhmaliikunta(1);
        Ryhmaliikunta kuntopiiri3 = new Ryhmaliikunta();
        kuntopiiri3.taytaRyhmaliikunta(2);
        Ryhmaliikunta kuntopiiri4 = new Ryhmaliikunta();
        kuntopiiri4.taytaRyhmaliikunta(2);

        ryhmat.lisaa(kuntopiiri1);
        ryhmat.lisaa(kuntopiiri2);
        ryhmat.lisaa(kuntopiiri3);
        ryhmat.lisaa(kuntopiiri2);
        ryhmat.lisaa(kuntopiiri4);

        System.out.println(
                "============= Ryhmaliikunnat testi =================");

        List<Ryhmaliikunta> ryhmaliikunnat2 = ryhmat.annaRyhmaliikunnat(2);

        for (Ryhmaliikunta ryhma : ryhmaliikunnat2) {
            System.out.print(ryhma.getTunnusNro() + " ");
            ryhma.tulosta(System.out);
        }

    }

}