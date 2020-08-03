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
import kuntosali.Ryhmaliikunta;

/**
 * @author antti
 * @version 3.8.2020
 *
 */
public class RyhmaDialogiController
        implements ModalControllerInterface<Ryhmaliikunta>, Initializable {

    @FXML
    private Label labelVirhe;

    @FXML
    private ScrollPane panelRyhma;

    @FXML
    private GridPane gridRyhma;

    @FXML
    private void handleOK() {
        if (ryhmaKohdalla != null
                && ryhmaKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä!");
            return;
        }
        ModalController.closeStage(panelRyhma);
    }


    @FXML
    private void handleCancel() {
        ryhmaKohdalla = null;
        ModalController.closeStage(panelRyhma);
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();

    }
    // -----------------------------------------------------------------------------------------------------------------------

    private Ryhmaliikunta ryhmaKohdalla;
    private static Ryhmaliikunta apuRyhma = new Ryhmaliikunta();
    private TextField edits[];
    private int kentta = 0;

    /**
     * Luodaan GridPaneen ryhmäliikunnan  tiedot
     * @param gridAsiakas mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridAsiakas) {
        gridAsiakas.getChildren().clear();
        TextField[] edits = new TextField[apuRyhma.getKenttia()];

        for (int i = 0, k = apuRyhma.ekaKentta(); k < apuRyhma
                .getKenttia(); k++, i++) {
            Label label = new Label(apuRyhma.getKysymys(k));
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
        edits = luoKentat(gridRyhma);
        for (TextField edit : edits)
            if (edit != null)
                edit.setOnKeyReleased(e -> kasitteleMuutosRyhmaan(
                        (TextField) (e.getSource())));
        panelRyhma.setFitToHeight(true);

    }


    private void setKentta(int kentta) {
        this.kentta = kentta;
    }


    @Override
    public Ryhmaliikunta getResult() {
        return ryhmaKohdalla;
    }


    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apuRyhma.ekaKentta(),
                Math.min(kentta, apuRyhma.getKenttia() - 1));
        edits[kentta].requestFocus();

    }


    @Override
    public void setDefault(Ryhmaliikunta oletus) {
        ryhmaKohdalla = oletus;
        naytaRyhma(edits, ryhmaKohdalla);

    }


    /**
     * @param edits taulukko TextFieldeistä johon tiedot näytetään
     * 
     * @param ryhma ryhma joka näytetään
     */
    public static void naytaRyhma(TextField[] edits, Ryhmaliikunta ryhma) {
        if (ryhma == null)
            return;
        for (int k = ryhma.ekaKentta(); k < ryhma.getKenttia(); k++) {
            edits[k].setText(ryhma.anna(k));
        }

    }

    /**
     * asettaa virheen näkyviin jos semmoinen on
     * @param virhe mikä meni pieleen
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            return;
        }
        labelVirhe.setText(virhe);
    }


    /**
     * Käsitellään asiakkaahan tullut muutos
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosRyhmaan(TextField edit) {
        if (ryhmaKohdalla == null)
            return;
        int k = getFieldId(edit, apuRyhma.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = ryhmaKohdalla.aseta(k, s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            naytaVirhe(virhe);
        }
    }


    /**
     * @param modalityStage mille modaalinen
     * @param oletus mitä näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan pois, muuten täytetty tietue
     */
    public static Ryhmaliikunta kysyRyhma(Stage modalityStage,
            Ryhmaliikunta oletus, int kentta) {
        return ModalController.showModal(
                RyhmaDialogiController.class
                        .getResource("RyhmaLisaysGUIView.fxml"),
                "Kuntosali", modalityStage, oletus,
                ctrl -> ((RyhmaDialogiController) ctrl).setKentta(kentta));
    }

}