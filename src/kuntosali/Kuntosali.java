package kuntosali;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

/**
         * @author antti
         * @version 3.8.2020
         *
         */
public class Kuntosali {

    private final Asiakkaat asiakkaat = new Asiakkaat();
    private final Ryhmaliikunnat ryhmaliikunnat = new Ryhmaliikunnat();

    /**
     * Palautaa kuntosalin asiakkaiden määrän
     * @return asiakkaiden määrä
     */
    public int getAsiakkaat() {
        return asiakkaat.getLkm();
    }


    /**
     * Lisää kuntosaliin uuden asiakkaan
     * @param asiakas Lisättävä asiakas
     * @throws SailoException jos lisäystä ei voida tehdä
    
     */
    public void lisaa(Asiakas asiakas) throws SailoException {
        asiakkaat.lisaa(asiakas);
    }


    /**
     * lisätään uusi ryhmaliikunta kuntosaliin
     * @param ryhma ryhma joka lisataan
     */
    public void lisaa(Ryhmaliikunta ryhma) {
        ryhmaliikunnat.lisaa(ryhma);
    }


    /**
     * Palauttaa i:n asiakkaan
     * @param i monesko asiakas palautetaan
     * @return viite i:teen asiakkaahan
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Asiakas annaAsiakas(int i) throws IndexOutOfBoundsException {
        return asiakkaat.anna(i);
    }


    /**
     * @param asiakas asiakas kenen ryhmaliiikunnat haetaan
     * @return lista ryhmaliikunnoista
     */
    public List<Ryhmaliikunta> annaRyhmaliikunnat(Asiakas asiakas) {
        return ryhmaliikunnat.annaRyhmaliikunnat(asiakas.getTunnusNro());
    }


    /**
     * Lukee kuntosalin tiedot tiedostosta
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        asiakkaat.lueTiedostosta("kuntosali/asiakkaat.dat");
        ryhmaliikunnat.lueTiedostosta("kuntosali/ryhmaliikunnat.dat");
    }


    /**
     * tallentaa tiedostot
     * @throws SailoException jos jotai menee pielee
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            asiakkaat.tallenna("kuntosali/asiakkat.dat");
        } catch (SailoException e) {
            virhe += e.getMessage();
        }

        try {
            ryhmaliikunnat.tallenna("kuntosali/ryhmaliikunnat.dat");
        } catch (SailoException e) {
            virhe += e.getMessage();
        }

        if (virhe.length() > 0)
            throw new SailoException(virhe);
    }


    /**
     * @param hakuehto hakuehto millä haetaan
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä jäsenistä
     */
    public Collection<Asiakas> etsi(String hakuehto, int k) {
        return asiakkaat.etsi(hakuehto, k);
    }


    /**
     * Poistaa asiakkaista ja ryhmaliikunnoista asiakkaan tiedot 
     * @param asiakas asiakas jokapoistetaan
     * @return montako asiakasta poistettiin
     */
    public int poista(Asiakas asiakas) {
        if (asiakas == null)
            return 0;
        int ret = asiakkaat.poista(asiakas.getTunnusNro());
        ryhmaliikunnat.poistaAsiakkaanRL(asiakas.getTunnusNro());
        return ret;
    }


    /** 
     * Poistaa ryhmaliikunnan 
     * @param ryhmaliikunta poistettava rl 
     */
    public void poistaRyhmaliikunta(Ryhmaliikunta ryhmaliikunta) {
        ryhmaliikunnat.poista(ryhmaliikunta);
    }


    /**
     * 
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Kuntosali kuntosali = new Kuntosali();

        try {

            Asiakas ismo1 = new Asiakas(), ismo2 = new Asiakas();
            ismo1.rekisteroi();
            ismo1.taytaAsiakas();
            ismo2.rekisteroi();
            ismo2.taytaAsiakas();

            kuntosali.lisaa(ismo1);
            kuntosali.lisaa(ismo2);

            System.out.println(
                    "============= Kuntosalin testi =================");

            for (int i = 0; i < kuntosali.getAsiakkaat(); i++) {
                Asiakas asiakas = kuntosali.annaAsiakas(i);
                System.out.println("Asiakas paikassa: " + i);
                asiakas.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }


    /**
     * Tarkistaa onko asiakkuus loppumassa seuraavan kuukauden aikana
     * @param asiakas asiakas jonka asiakkuus tarkistetaan
     * @return palauttaa inttinä kuinka monta päivää asiakkuutta jäljellä
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import kuntosali.*;
     * Kuntosali kuntosali = new Kuntosali();
     * Asiakas asiakas = new Asiakas();
     * Asiakas asiakas1 = new Asiakas();
     * Asiakas asiakas2 = new Asiakas();
     * asiakas1.parse("5|Kari Taalasmaa|0402470506|Kahvakuulantie 7|56700|Kangasala|kari.taalasmaa@hotmail.com|2018-02-23|2020-11-24");
     * asiakas.parse("6|Seppo Taalasmaa|0405330207|Kalliokuja 21|56700|Helsinki|seppo.ukko@hotmail.com|2017-12-01|2020-06-01");
     * asiakas2.parse("2|Koira Ukko|0123456789|Koirakuja 7|63700|Koirakyla|koiruli@koiramail.com|1998-06-21|2020-08-05");
     * kuntosali.asiakkuusLoppumassa(asiakas) === -63;
     * kuntosali.asiakkuusLoppumassa(asiakas1) === 113;
     * kuntosali.asiakkuusLoppumassa(asiakas2) === 2;
     * </pre>
     */
    public int asiakkuusLoppumassa(Asiakas asiakas) {

        String loppumisPv = asiakas.getLoppumisPv();
        LocalDate loppuminen = LocalDate.parse(loppumisPv);
        LocalDate nyt = LocalDate.now();

        long montapv = ChronoUnit.DAYS.between(nyt, loppuminen);
        return (int) montapv;

    }


    /**
     * @param asiakas lisätäävän asiakkaan viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne täynnä
     */
    public void korvaaTaiLisaa(Asiakas asiakas) throws SailoException {
        asiakkaat.korvaaTaiLisaa(asiakas);
    }


    /**
     * @param ryhma lisätäävän ryhmaliikunnan viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne täynnä
     */
    public void korvaaTaiLisaa(Ryhmaliikunta ryhma) throws SailoException {
        ryhmaliikunnat.korvaaTaiLisaa(ryhma);
    }

}