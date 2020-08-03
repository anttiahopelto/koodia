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
 * @version 3.8.2020
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
     * @param nimi tiedoston nimi
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * #PACKAGEIMPORT
     *  Ryhmaliikunnat harrasteet = new Ryhmaliikunnat();
     *  Ryhmaliikunta pitsi21 = new Ryhmaliikunta(); pitsi21.taytaRyhmaliikunta(1);
     *  Ryhmaliikunta pitsi11 = new Ryhmaliikunta(); pitsi11.taytaRyhmaliikunta(1);
     *  Ryhmaliikunta pitsi22 = new Ryhmaliikunta(); pitsi22.taytaRyhmaliikunta(1);
     *  Ryhmaliikunta pitsi12 = new Ryhmaliikunta(); pitsi12.taytaRyhmaliikunta(1); 
     *  Ryhmaliikunta pitsi23 = new Ryhmaliikunta(); pitsi23.taytaRyhmaliikunta(1); 
     *  String tiedNimi = "testiryhmat.dat";
     *  File ftied = new File(tiedNimi);
     *  ftied.delete();
     *  harrasteet.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  harrasteet.lisaa(pitsi21);
     *  harrasteet.lisaa(pitsi11);
     *  harrasteet.lisaa(pitsi22);
     *  harrasteet.lisaa(pitsi12);
     *  harrasteet.lisaa(pitsi23);
     *  harrasteet.tallenna(tiedNimi);
     *  harrasteet = new Ryhmaliikunnat();
     *  harrasteet.lueTiedostosta(tiedNimi);
     *  Iterator<Ryhmaliikunta> i = harrasteet.iterator();
     *  i.next().toString() === pitsi21.toString();
     *  i.next().toString() === pitsi11.toString();
     *  i.next().toString() === pitsi22.toString();
     *  i.next().toString() === pitsi12.toString();
     *  i.next().toString() === pitsi23.toString();
     *  i.hasNext() === false;
     *  harrasteet.lisaa(pitsi23);
     *  harrasteet.tallenna(tiedNimi);
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        try (BufferedReader fi = new BufferedReader(
                new FileReader(nimi))) {

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
     * @param nimi tiedoston nimi
     * @throws SailoException jos talletus epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * #PACKAGEIMPORT
     *  Ryhmaliikunnat harrasteet = new Ryhmaliikunnat();
     *  Ryhmaliikunta pitsi21 = new Ryhmaliikunta(); pitsi21.taytaRyhmaliikunta(1);
     *  Ryhmaliikunta pitsi11 = new Ryhmaliikunta(); pitsi11.taytaRyhmaliikunta(1);
     *  Ryhmaliikunta pitsi22 = new Ryhmaliikunta(); pitsi22.taytaRyhmaliikunta(1);
     *  Ryhmaliikunta pitsi12 = new Ryhmaliikunta(); pitsi12.taytaRyhmaliikunta(1); 
     *  Ryhmaliikunta pitsi23 = new Ryhmaliikunta(); pitsi23.taytaRyhmaliikunta(1); 
     *  String tiedNimi = "testiryhmat.dat";
     *  File ftied = new File(tiedNimi);
     *  ftied.delete();
     *  harrasteet.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  harrasteet.lisaa(pitsi21);
     *  harrasteet.lisaa(pitsi11);
     *  harrasteet.lisaa(pitsi22);
     *  harrasteet.lisaa(pitsi12);
     *  harrasteet.lisaa(pitsi23);
     *  harrasteet.tallenna(tiedNimi);
     *  harrasteet = new Ryhmaliikunnat();
     *  harrasteet.lueTiedostosta(tiedNimi);
     *  Iterator<Ryhmaliikunta> i = harrasteet.iterator();
     *  i.next().toString() === pitsi21.toString();
     *  i.next().toString() === pitsi11.toString();
     *  i.next().toString() === pitsi22.toString();
     *  i.next().toString() === pitsi12.toString();
     *  i.next().toString() === pitsi23.toString();
     *  i.hasNext() === false;
     *  harrasteet.lisaa(pitsi23);
     *  harrasteet.tallenna(tiedNimi);
     * </pre>
     */
    public void tallenna(String nimi) throws SailoException {
        if (!muutettu)
            return;

        File fbak = new File(getBakNimi());
        File ftied = new File(nimi);
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
     * Poistaa kaikki tietyn tietyn asiakkaan ryhmäliikunnat
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     */
    public int poistaAsiakkaanRL(int tunnusNro) {
        int n = 0;
        for (Iterator<Ryhmaliikunta> it = alkiot.iterator(); it.hasNext();) {
            Ryhmaliikunta rl = it.next();
            if (rl.getAsiakasNro() == tunnusNro) {
                it.remove();
                n++;
            }
        }
        if (n > 0)
            muutettu = true;
        return n;
    }


    /**
     * Poistaa valitun ryhmaliikunnan
     * @param ryhmaliikunta poistettava rl
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Ryhmaliikunnat harrasteet = new Ryhmaliikunnat();
     *  Ryhmaliikunta pitsi21 = new Ryhmaliikunta(); pitsi21.taytaRyhmaliikunta(2);
     *  Ryhmaliikunta pitsi11 = new Ryhmaliikunta(); pitsi11.taytaRyhmaliikunta(1);
     *  Ryhmaliikunta pitsi22 = new Ryhmaliikunta(); pitsi22.taytaRyhmaliikunta(2); 
     *  Ryhmaliikunta pitsi12 = new Ryhmaliikunta(); pitsi12.taytaRyhmaliikunta(1); 
     *  Ryhmaliikunta pitsi23 = new Ryhmaliikunta(); pitsi23.taytaRyhmaliikunta(2); 
     *  harrasteet.lisaa(pitsi21);
     *  harrasteet.lisaa(pitsi11);
     *  harrasteet.lisaa(pitsi22);
     *  harrasteet.lisaa(pitsi12);
     *  harrasteet.poista(pitsi23) === false ; harrasteet.getLkm() === 4;
     *  harrasteet.poista(pitsi11) === true;   harrasteet.getLkm() === 3;
     *  List<Ryhmaliikunta> h = harrasteet.annaRyhmaliikunnat(1);
     *  h.size() === 1; 
     *  h.get(0) === pitsi12;
     * </pre>
     */
    public boolean poista(Ryhmaliikunta ryhmaliikunta) {
        boolean ret = alkiot.remove(ryhmaliikunta);
        if (ret)
            muutettu = true;
        return ret;
    }


    /**
     * Korvaa ryhmäliikunnan tietorakenteessa.  Ottaa ryhmäliikunnan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva ryhmaliikunta.  Jos ei löydy,
     * niin lisätään uutena ryhmäliikuntana.
     * @param ryhma lisättävän ryhmaliikunnan viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Ryhmaliikunnat harrastukset = new Ryhmaliikunnat();
     * Ryhmaliikunta har1 = new Ryhmaliikunta(), har2 = new Ryhmaliikunta();
     * har1.rekisteroi(); har2.rekisteroi();
     * harrastukset.getLkm() === 0;
     * harrastukset.korvaaTaiLisaa(har1); harrastukset.getLkm() === 1;
     * harrastukset.korvaaTaiLisaa(har2); harrastukset.getLkm() === 2;
     * Ryhmaliikunta har3 = har1.clone();
     * har3.aseta(2,"kkk");
     * Iterator<Ryhmaliikunta> i2=harrastukset.iterator();
     * i2.next() === har1;
     * harrastukset.korvaaTaiLisaa(har3); harrastukset.getLkm() === 2;
     * i2=harrastukset.iterator();
     * Ryhmaliikunta h = i2.next();
     * h === har3;
     * h == har3 === true;
     * h == har1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Ryhmaliikunta ryhma) throws SailoException {
        int id = ryhma.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (((ArrayList<Ryhmaliikunta>) alkiot).get(i)
                    .getTunnusNro() == id) {
                ((ArrayList<Ryhmaliikunta>) alkiot).set(i, ryhma);
                muutettu = true;
                return;
            }
        }
        lisaa(ryhma);
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