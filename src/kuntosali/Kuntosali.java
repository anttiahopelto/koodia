        package kuntosali;

import java.util.List;

/**
         * @author antti
         * @version 9.7.2020
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
         * @param nimi jota käyteään lukemisessa
         * @throws SailoException jos lukeminen epäonnistuu
         */
        public void lueTiedostosta(String nimi) throws SailoException {
            asiakkaat.lueTiedostosta(nimi);
            ryhmaliikunnat.lueTiedostosta(nimi);
        }
        
        /**
         * Tallettaa kuntosalin tiedot tiedostoon
         * @throws SailoException jos tallettamisessa ongelmia
         */
        public void talleta() throws SailoException {
            asiakkaat.talleta();
            ryhmaliikunnat.talleta();
            // TODO: yritä tallettaa toinen vaikka toinen epäonnistuisi
        }
        
        /**
         * 
         * @param args ei käytössä
         */
        public static void main(String args[]) {
            Kuntosali kuntosali = new Kuntosali();
        
            try {
                // kuntosali.lueTiedostosta("asiakastiedot");
        
                Asiakas ismo1 = new Asiakas(), ismo2 = new Asiakas();
                ismo1.rekisteroi();
                ismo1.taytaAsiakas();
                ismo2.rekisteroi();
                ismo2.taytaAsiakas();
        
                kuntosali.lisaa(ismo1);
                kuntosali.lisaa(ismo2);
        
                System.out.println("============= Kuntosalin testi =================");
        
                for (int i = 0; i < kuntosali.getAsiakkaat(); i++) {
                    Asiakas asiakas = kuntosali.annaAsiakas(i);
                    System.out.println("Asiakas paikassa: " + i);
                    asiakas.tulosta(System.out);
                }
        
            } catch (SailoException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        
        
        }