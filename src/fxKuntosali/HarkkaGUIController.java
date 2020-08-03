package fxKuntosali;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kuntosali.Asiakas;
import kuntosali.Kuntosali;
import kuntosali.Ryhmaliikunta;
import kuntosali.SailoException;
import static fxKuntosali.AsiakasDialogiController.getFieldId;

/**
 * @author antti
 * @version 2.8.2020
 *
 */
public class HarkkaGUIController implements Initializable {

    @FXML
    private Label labelLoppumassa;

    @FXML
    private GridPane gridAsiakas;

    @FXML
    private StringGrid<Ryhmaliikunta> tableRyhmaliikunnat;

    @FXML
    private TextField hakuehto;

    @FXML
    ListChooser<Asiakas> chooserAsiakkaat;

    @FXML
    ScrollPane panelAsiakas;

    @FXML
    private ComboBoxChooser<String> cbKentat;
    private String kuntosalinnimi = "kuntosali";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();

    }


    /**
     * Kutsutaan aliohjelmia, kun käyttäjä tekee jotain
     */

    @FXML
    void handleApua() {
        apua();

    }


    @FXML
    void handleLisaaAsiakas() {
        lisaaAsiakas();

    }


    @FXML
    void handleTarkistaLoppuminen() {
        asiakkuusLoppumassa();
    }


    @FXML
    void handleLopeta() {
        lopeta();

    }


    @FXML
    void handleMuokkaa() {
        muokkaa(1);

    }


    @FXML
    void handleMuokkaaRyhma() {
        muokkaaRyhma();
    }


    @FXML
    void handleTallenna() {
        tallenna();

    }


    @FXML
    void handleHaku() {
        hae(0);

    }


    @FXML
    void handleHakukriteeri() {
        hae(0);

    }


    @FXML
    void handleAvaa() {
        avaa();
    }


    @FXML
    void handleLisaaRyhma() {
        lisaaRyhma();
    }


    @FXML
    void handlePoistaRyhmä() {
        poistaRyhma();
    }


    @FXML
    void handlePoistaAsiakas() {
        poistaAsiakas();
    }

    // --------------------------------------------------------------------------------

    /**
     * Kuntosali johon viitataan
     */
    private Kuntosali kuntosali;
    private Asiakas asiakasKohdalla;
    private TextField edits[];
    private int kentta = 0;
    private static Asiakas apuAsiakas = new Asiakas();

    /**
     *  Lisää uuden asiakkaan. Avaa dialogin tietojen täyttämistä varten
     */
    private void uusiAsiakas() {
        try {
            Asiakas uusi = new Asiakas();
            uusi = AsiakasDialogiController.kysyAsiakas(null, uusi, 1);
            if (uusi == null)
                return;
            uusi.rekisteroi();
            kuntosali.lisaa(uusi);
            hae(uusi.getTunnusNro());
        } catch (SailoException e) {
            Dialogs.showMessageDialog(
                    "ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
    }


    /**
     * Poistetaan listalta valittu jäsen
     */
    private void poistaAsiakas() {
        Asiakas asiakas = asiakasKohdalla;
        if (asiakas == null)
            return;
        if (!Dialogs.showQuestionDialog("Poisto",
                "Poistetaanko asiakas: " + asiakas.getNimi(), "Kyllä", "Ei"))
            return;
        kuntosali.poista(asiakas);
        int index = chooserAsiakkaat.getSelectedIndex();
        hae(0);
        chooserAsiakkaat.setSelectedIndex(index);
    }


    /**
     * Hakee jäsenten tiedot listaan
     * @param tNro asiakkaan tunnusnumero, joka aktivoidaan haun jälkeen  
     */
    protected void hae(int tNro) {
        int nro = tNro; // asiakkaan numero, joka aktivoidaan haun jälkeen
        if (nro <= 0) {
            Asiakas kohdalla = asiakasKohdalla;
            if (kohdalla != null)
                nro = kohdalla.getTunnusNro();
        }

        int k = cbKentat.getSelectionModel().getSelectedIndex()
                + apuAsiakas.ekaKentta();
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0)
            ehto = "*" + ehto + "*";

        chooserAsiakkaat.clear();

        int index = 0;
        Collection<Asiakas> asiakkaat;
        asiakkaat = kuntosali.etsi(ehto, k);
        int i = 0;
        for (Asiakas asiakas : asiakkaat) {
            if (asiakas.getTunnusNro() == nro)
                index = i;
            chooserAsiakkaat.add(asiakas.getNimi(), asiakas);
            i++;
        }
        chooserAsiakkaat.setSelectedIndex(index); // tästä tulee muutosviesti
                                                  // joka näyttää jäsenen
    }


    /**
     * Alustetaan listchooseri ja gridpane jossa näkyy asiakkaat
     */
    private void alusta() {
        chooserAsiakkaat.clear();
        chooserAsiakkaat.addSelectionListener(e -> naytaAsiakas());
        edits = AsiakasDialogiController.luoKentat(gridAsiakas);
        for (TextField edit : edits)
            if (edit != null) {
                edit.setEditable(false);
                edit.setOnMouseClicked(e -> {
                    if (e.getClickCount() > 1)
                        muokkaa(getFieldId(e.getSource(), 0));
                });
                edit.focusedProperty().addListener(
                        (a, o, n) -> kentta = getFieldId(edit, kentta));
            }
        gridAsiakas.add(labelLoppumassa, 1, 9);

    }


    /**
     * Kun on valittu asiakas listchooserista, niin tulostetaan se keskellä olevaan gridpaneen
     */
    private void naytaAsiakas() {
        asiakasKohdalla = chooserAsiakkaat.getSelectedObject();
        if (asiakasKohdalla == null)
            return;

        AsiakasDialogiController.naytaAsiakas(edits, asiakasKohdalla);
        naytaRyhmaliikunnat(asiakasKohdalla);
        asiakkuusLoppumassa();

    }


    /**
     * Laittaa asiakkaan ryhmäliikunnat näkyviin StringGridiin tableRyhmaliikunnat
     * @param asiakas minkä asiakkaan ryhmaliikunnat näytetän StringGridissä
     */
    private void naytaRyhmaliikunnat(Asiakas asiakas) {
        tableRyhmaliikunnat.clear();
        if (asiakas == null)
            return;

        List<Ryhmaliikunta> ryhmaliikunnat = kuntosali
                .annaRyhmaliikunnat(asiakas);
        if (ryhmaliikunnat.size() == 0)
            return;
        for (Ryhmaliikunta ryhma : ryhmaliikunnat)
            naytaRyhmaliikunta(ryhma);
    }


    /**
     * Aliohjelma joka lisää ryhmäliikunnan näkyviin StringGridiin, pilkkoo eka ryhmän attribuutit taulukkoon
     * @param ryhma ryhmaliikunta joka lisätään tableen
     */

    private void naytaRyhmaliikunta(Ryhmaliikunta ryhma) {
        String[] rivi = ryhma.toString().split("\\|"); // TODO tee parempi
        tableRyhmaliikunnat.add(ryhma, rivi[2], rivi[3], rivi[4]);
    }


    /**
     * asetetaan kontrollerin kuntosaliviite
     * @param kuntosali kuntosali mihin viitataan
     */
    public void setKuntosali(Kuntosali kuntosali) {
        this.kuntosali = kuntosali;
    }


    /**
     * Aliohjelma, joka avaa selaimella verkkosivun, jossa näkyy ohjeita ohjelman käyttöön
     */
    private void apua() {
        if (Desktop.isDesktopSupported()
                && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(
                        "https://tim.jyu.fi/view/kurssit/tie/ohj2/2020k/ht/anolahop"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Dialogs.showMessageDialog("Ei onnistuttu avaamaan oletusselainta");
        }

    }


    /**
     * Aliohjelma, joka avaa ikkunan asiakkaan lisäämistä varten
     */
    private void lisaaAsiakas() {

        uusiAsiakas();
    }


    /**
     * Aliohjelma joka tarkistaa onko asiakkuus loppumassa 30pv sisällä.
     * Näyttää paljon päiviä jäljellä. Teksti punainen jos alle 
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
    private void asiakkuusLoppumassa() {
        asiakasKohdalla = chooserAsiakkaat.getSelectedObject();
        int paivia = kuntosali.asiakkuusLoppumassa(asiakasKohdalla);
        if (paivia < 30 && paivia >= 1) {
            labelLoppumassa
                    .setText("Asiakkuutta jäljellä " + paivia + " päivää");
            labelLoppumassa.getStyleClass().add("virhe");
            return;
        }
        if (paivia < 0) {
            labelLoppumassa.setText(
                    "Asiakkuus umpeutunut\n" + -paivia + " päivää sitten");
            labelLoppumassa.getStyleClass().add("virhe");
            return;
        }

        labelLoppumassa.setText("Asiakkuutta jäljellä " + paivia + " päivää");
        labelLoppumassa.getStyleClass().removeAll("virhe");

    }


    /**
     * Aliohjelma, joka lopettaa ohjelman. Ennen tätä tallentaa myös
     */
    private void lopeta() {
        tallenna();
        Platform.exit();
    }


    /**
     * Aliohjelma, joka avaa muokkaus dialogin ryhmaliikunnan muokkausta varten
     */
    private void muokkaaRyhma() {
        int r = tableRyhmaliikunnat.getRowNr();
        if (r < 0)
            return; // klikattu ehkä otsikkoriviä
        Ryhmaliikunta ryhma = tableRyhmaliikunnat.getObject();
        if (ryhma == null)
            return;
        int k = tableRyhmaliikunnat.getColumnNr() + ryhma.ekaKentta();
        try {
            ryhma = RyhmaDialogiController.kysyRyhma(null, ryhma.clone(), k);
            if (ryhma == null)
                return;
            kuntosali.korvaaTaiLisaa(ryhma);
            naytaRyhmaliikunnat(asiakasKohdalla);
            tableRyhmaliikunnat.selectRow(r); // järjestetään sama rivi takaisin
                                              // valituksi
        } catch (CloneNotSupportedException e) { /* clone on tehty */
        } catch (SailoException e) {
            Dialogs.showMessageDialog(
                    "Ongelmia lisäämisessä: " + e.getMessage());
        }
    }


    /**
     * Aliohjelma, joka avaa muokkausikkunan jäsenelle
     */
    private void muokkaa(int k) {
        if (asiakasKohdalla == null)
            return;
        try {
            Asiakas asiakas;
            asiakas = AsiakasDialogiController.kysyAsiakas(null,
                    asiakasKohdalla.clone(), k);
            if (asiakas == null)
                return;
            kuntosali.korvaaTaiLisaa(asiakas);
            hae(asiakas.getTunnusNro());
        } catch (CloneNotSupportedException e) {
            //
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    /**
     * Aliohjelma, joka hoitaa tallennuksen
     */
    private void tallenna() {
        try {
            kuntosali.tallenna();
        } catch (SailoException e) {
            // TODO Auto-generated catch block
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    /**
     * Aliohjelma, jolla voi avata toisen kuntosalin rekisterin
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = KuntosaliNimiController.kysyNimi(null,
                kuntosalinnimi);
        if (uusinimi == null)
            return false;
        lueTiedosto(uusinimi);
        return true;

    }


    /**
     * Lukee annetun nimen mukaisesta tiedostosa asiakkaat ja niiden ryhmäliikunnat, laittaa myös titleksi nimen
     * @param nimi kuntosalin nimi
     */
    protected void lueTiedosto(String nimi) {
        kuntosalinnimi = nimi;
        setTitle("Kuntosali - " + kuntosalinnimi);
        try {
            kuntosali.lueTiedostosta();
            hae(0);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    /**
     * Asettaa stagelle titlen
     * @param title title joka asetetaan näkyviin yläreunaan
     */
    private void setTitle(String title) {
        ModalController.getStage(chooserAsiakkaat).setTitle(title);
    }


    /**
     * Lisää listchooserista valitulle asiakkaalle ryhmäliikunnan
     */
    private void lisaaRyhma() {
        asiakasKohdalla = chooserAsiakkaat.getSelectedObject();
        if (asiakasKohdalla == null)
            return;

        int asiakasnro = asiakasKohdalla.getTunnusNro();
        Ryhmaliikunta ryhma = new Ryhmaliikunta();
        ryhma = RyhmaDialogiController.kysyRyhma(null, ryhma, 1);
        if (ryhma == null)
            return;
        ryhma.setAsiakasNro(asiakasnro);
        ryhma.rekisteroi();
        kuntosali.lisaa(ryhma);
        hae(asiakasKohdalla.getTunnusNro());

    }


    /**
     * Poistaa ryhmän valitulta asiakkaalta
     */
    private void poistaRyhma() {
        int rivi = tableRyhmaliikunnat.getRowNr();
        if (rivi < 0)
            return;
        Ryhmaliikunta ryhma = tableRyhmaliikunnat.getObject();
        if (ryhma == null)
            return;
        kuntosali.poistaRyhmaliikunta(ryhma);
        naytaRyhmaliikunnat(asiakasKohdalla);
        int ryhmia = tableRyhmaliikunnat.getItems().size();
        if (rivi >= ryhmia)
            rivi = ryhmia - 1;
        tableRyhmaliikunnat.getFocusModel().focus(rivi);
        tableRyhmaliikunnat.getSelectionModel().select(rivi);
    }


    /**
     * tarkistetaan onko tallennettu
     * @return true jos saa sulkea, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

}
