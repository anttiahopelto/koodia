package kuntosali;

import java.util.*;


/**
 * Kuntosalin Ryhmaliikunnat, osaa mm. lisätä uuden ryhmaliikunnan
 * @author antti
 * @version 5.7.2020
 *
 */
public class Ryhmaliikunnat implements Iterable<Ryhmaliikunta> {

    private String                      tiedostonNimi = "";

    /** Taulukko ryhmaliikunnoista */
    private final Collection<Ryhmaliikunta> alkiot        = new ArrayList<Ryhmaliikunta>();


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
    }


    /**
     * Lukee asiakkaat tiedostosta.  
     * TODO Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + ".har";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa ryhmaliikunnat tiedostoon.  
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
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
     *  Harrastukset harrasteet = new Harrastukset();
     *  Harrastus pitsi21 = new Harrastus(2); harrasteet.lisaa(pitsi21);
     *  Harrastus pitsi11 = new Harrastus(1); harrasteet.lisaa(pitsi11);
     *  Harrastus pitsi22 = new Harrastus(2); harrasteet.lisaa(pitsi22);
     *  Harrastus pitsi12 = new Harrastus(1); harrasteet.lisaa(pitsi12);
     *  Harrastus pitsi23 = new Harrastus(2); harrasteet.lisaa(pitsi23);
     * 
     *  Iterator<Harrastus> i2=harrasteet.iterator();
     *  i2.next() === pitsi21;
     *  i2.next() === pitsi11;
     *  i2.next() === pitsi22;
     *  i2.next() === pitsi12;
     *  i2.next() === pitsi23;
     *  i2.next() === pitsi12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Harrastus har:harrasteet ) { 
     *    har.getJasenNro() === jnrot[n]; n++;  
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
            if (ryhma.getAsiakasNro() == tunnusnro) loydetyt.add(ryhma);
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

        System.out.println("============= Ryhmaliikunnat testi =================");

        List<Ryhmaliikunta> ryhmaliikunnat2 = ryhmat.annaRyhmaliikunnat(2);

        for (Ryhmaliikunta ryhma : ryhmaliikunnat2) {
            System.out.print(ryhma.getTunnusNro() + " ");
            ryhma.tulosta(System.out);
        }

    }

}