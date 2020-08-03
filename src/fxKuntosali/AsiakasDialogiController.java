package fxKuntosali;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kuntosali.Asiakas;

/**
 * @author antti
 * @version 3.8.2020
 *
 */
public class AsiakasDialogiController
        implements ModalControllerInterface<Asiakas>, Initializable {

    @FXML
    private Label labelVirhe;

    @FXML
    private ScrollPane panelAsiakas;

    @FXML
    private GridPane gridAsiakas;

    @FXML
    private void handleOK() {
        if (asiakasKohdalla != null
                && asiakasKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä!");
            return;
        }
        ModalController.closeStage(panelAsiakas);
    }


    @FXML
    private void handleCancel() {
        asiakasKohdalla = null;
        ModalController.closeStage(panelAsiakas);
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();

    }
    // -----------------------------------------------------------------------------------------------------------------------

    private Asiakas asiakasKohdalla;
    private static Asiakas apuAsiakas = new Asiakas();
    private TextField edits[];
    private int kentta = 0;

    /**
     * Luodaan GridPaneen asiakkaan tiedot
     * @param gridAsiakas mihin tiedot luodaan
     * @return luodut tekstikentät textfield taulukkona
     */
    public static TextField[] luoKentat(GridPane gridAsiakas) {
        gridAsiakas.getChildren().clear();
        TextField[] edits = new TextField[apuAsiakas.getKenttia()];

        for (int i = 0, k = apuAsiakas.ekaKentta(); k < apuAsiakas
                .getKenttia(); k++, i++) {
            Label label = new Label(apuAsiakas.getKysymys(k));
            gridAsiakas.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e" + k);
            gridAsiakas.add(edit, 1, i);
        }
        return edits;
    }


    /**
     * Tyhjentää tekstikentät
     * @param edits tyhjennettävät kentät
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
            if (edit != null)
                edit.setText("");
    }


    /**
    * Palautetaan komponentin id:stä saatava luku
    * @param obj tutkittava komponentti
    * @param oletus mikä arvo jos id ei ole kunnollinen
    * @return komponentin id lukuna 
    */
    public static int getFieldId(Object obj, int oletus) {
        if (!(obj instanceof Node))
            return oletus;
        Node node = (Node) obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }


    /**
     * Tekee tarvittavat muut alustukset. Mm laittaa edit-kentistä tulevan
     * tapahtuman menemään kasitteleMuutosJaseneen-metodiin ja vie sille
     * kentännumeron parametrina.
     */
    protected void alusta() {
        edits = luoKentat(gridAsiakas);
        for (TextField edit : edits)
            if (edit != null)
                edit.setOnKeyReleased(e -> kasitteleMuutosAsiakkaahan(
                        (TextField) (e.getSource())));
        panelAsiakas.setFitToHeight(true);

    }


    /**
     * Asettaa kentän numeron halutuksi
     * @param kentta kentän nro
     */
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }


    /**
     * Palauttaa aina asiakaskohdalla
     */
    @Override
    public Asiakas getResult() {
        return asiakasKohdalla;
    }


    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apuAsiakas.ekaKentta(),
                Math.min(kentta, apuAsiakas.getKenttia() - 1));
        edits[kentta].requestFocus();

    }


    /**
     * Asettaa oletus asiakkaan asiakaskohdalla ja näyttää sen asiakkaan
     */
    @Override
    public void setDefault(Asiakas oletus) {
        asiakasKohdalla = oletus;
        naytaAsiakas(edits, asiakasKohdalla);

    }


    /**
     * Laittaa halutun asiakkaan näkyviin, viedään parametrinä sen tekstikentät jotka täytetään
     * @param edits taulukko TextFieldeistä johon tiedot näytetään
     * @param asiakas asiakas joka näytetään
     */
    public static void naytaAsiakas(TextField[] edits, Asiakas asiakas) {
        if (asiakas == null)
            return;
        for (int k = asiakas.ekaKentta(); k < asiakas.getKenttia(); k++) {
            edits[k].setText(asiakas.anna(k));
        }

    }


    /**
     * Näytetään virheellinen syöttö css tiedoston avulla
     * @param virhe mikä virhe
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }


    /**
     * Käsitellään asiakkaahan tullut muutos
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosAsiakkaahan(TextField edit) {
        if (asiakasKohdalla == null)
            return;
        int k = getFieldId(edit, apuAsiakas.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = asiakasKohdalla.aseta(k, s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }


    /**
     * @param modalityStage mille modaalinen
     * @param oletus mitä näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan pois, muuten täytetty tietue
     */
    public static Asiakas kysyAsiakas(Stage modalityStage, Asiakas oletus,
            int kentta) {
        return ModalController.showModal(
                AsiakasDialogiController.class
                        .getResource("MuokkausGUIView.fxml"),
                "Kuntosali", modalityStage, oletus,
                ctrl -> ((AsiakasDialogiController) ctrl).setKentta(kentta));
    }

}
