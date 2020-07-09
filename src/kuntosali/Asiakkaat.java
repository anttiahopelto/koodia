/**
 * 
 */
package kuntosali;

/**
 * @author antti
 * @version 9.7.2020
 *
 */
public class Asiakkaat {

        private static final int MAX_JASENIA   = 8;
        private int              lkm           = 0;
        private String           tiedostonNimi = "";
        private Asiakas[]        alkiot        = new Asiakas[MAX_JASENIA];
        
        
        /**
         * muodostaja
         */
        public Asiakkaat() {
            // attribuuttien oma alustus on riittävä
        }
        
        
        /**
         * Lisää uuden asiakkaan tietorakenteeseen.  Ottaa asiakkaan omistukseensa.
         * @param asiakas lisätäävän asiakkaan viite.  Huom tietorakenne muuttuu omistajaksi
         * @throws SailoException jos tietorakenne on jo täynnä
         */
        public void lisaa(Asiakas asiakas) throws SailoException {
            if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
            //TODO: Kasvata taulukon kokoa, eli uusi taulukko, kun vanha täynnä
            alkiot[lkm] = asiakas;
            lkm++;
        }
        
        
        /**
         * Palauttaa viitteen i:teen asiakkaaseen.
         * @param i monennenko jäsenen viite halutaan
         * @return viite asiakkaahan, jonka indeksi on i
         * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
         */
        public Asiakas anna(int i) throws IndexOutOfBoundsException {
            if (i < 0 || lkm <= i)
                throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
            return alkiot[i];
        }
        
        /**
         * Lukee asiakkaat tiedostosta.  Kesken.
         * @param hakemisto tiedoston hakemisto
         * @throws SailoException jos lukeminen epäonnistuu
         */
        public void lueTiedostosta(String hakemisto) throws SailoException {
            tiedostonNimi = hakemisto + "/nimet.dat";
            throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
        }


        /**
         * Tallentaa asiakkaat tiedostoon.  Kesken.
         * @throws SailoException jos talletus epäonnistuu
         */
        public void talleta() throws SailoException {
            throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
        }


        /**
         * Palauttaa kuntosalin asiakkaiden lukumäärän
         * @return asiakkaiden lukumäärä
         */
        public int getLkm() {
            return lkm;
        }
        
        
        /**
         * Testiohjelma asiakkaille
         * @param args ei käytössä
         */
        public static void main(String args[]) {
            Asiakkaat asiakkaat = new Asiakkaat();

            Asiakas ismo1 = new Asiakas(), ismo2 = new Asiakas();
            ismo1.rekisteroi();
            ismo1.taytaAsiakas();
            ismo2.rekisteroi();
            ismo2.taytaAsiakas();

            try {
                asiakkaat.lisaa(ismo1);
                asiakkaat.lisaa(ismo2);

                System.out.println("============= Asiakkaat testi =================");

                for (int i = 0; i < asiakkaat.getLkm(); i++) {
                    Asiakas asiakas = asiakkaat.anna(i);
                    System.out.println("Jäsen nro: " + i);
                    asiakas.tulosta(System.out);
                }

            } catch (SailoException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
       

}
