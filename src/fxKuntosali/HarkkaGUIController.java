package fxKuntosali;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import kuntosali.Asiakas;
import kuntosali.Kuntosali;
import kuntosali.Ryhmaliikunta;
import kuntosali.SailoException;

import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;

/**
 * @author antti
 * @version 12.6.2020
 *
 */
public class HarkkaGUIController implements Initializable {
    
    @FXML ListChooser<Asiakas> chooserAsiakkaat;
    @FXML ScrollPane panelAsiakas;
    
    
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
    void handleLisaaJasen() {
        lisaaJasen();
        
    }

    @FXML
    void handleLopeta() {
        lopeta();
        
    }

    @FXML
    void handleMuokkaa() {
        muokkaa();
        
    }

    @FXML
    void handleTallenna() {
        tallenna();
        
    }

    

    @FXML
    void handleHaku() {
        haku();
        
    }

    @FXML
    void handleHakukriteeri() {
        hakuKriteeri();
        
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
    
    
    

 //--------------------------------------------------------------------------------
    
    
    
    /**
     * Kuntosali johon viitataan
     */
    private Kuntosali kuntosali;
    private TextArea areaAsiakas = new TextArea();
    
    
    /**
     *  Lisää uuden asiakkaan. Tällä hetkellä alustaa oletustiedoilla, myöhemmin avataan dialogi ja voi täyttää oikeat tiedot
     */
    private void uusiAsiakas() {
        Asiakas uusi = new Asiakas();
        uusi.rekisteroi();
        uusi.taytaAsiakas(); // TODO: Korvaa oikealla dialogilla
        try {
            kuntosali.lisaa(uusi);
        } catch (SailoException e) {
            
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa" + e.getMessage());
            return;
        }
        hae(uusi.getTunnusNro());
    }
    
    
    /**
     * Hakee asiakkaan tiedot näkymään listchooseriin
     * @param tunnusNro asiakkaan tunnusnro
     */
    private void hae(int tunnusNro) {
        chooserAsiakkaat.clear();
        int index = 0;
        for ( int i = 0; i < kuntosali.getAsiakkaat(); i++) {
            Asiakas asiakas = kuntosali.annaAsiakas(i);
            if (asiakas.getTunnusNro() == tunnusNro) index = i;
            chooserAsiakkaat.add(asiakas.getNimi(), asiakas);
        }
        chooserAsiakkaat.setSelectedIndex(index); // tämä tekee muutosviestin joka näyttää
        }
    
     
     /**
      * Alustetaan listchooseri ja scrollpane jossa näkyy asiakkaat
      */
    private void alusta() {
        panelAsiakas.setContent(areaAsiakas);
        areaAsiakas.setFont(new Font("Times New Roman", 12));
        panelAsiakas.setFitToHeight(true);
        
        chooserAsiakkaat.clear();
        chooserAsiakkaat.addSelectionListener(e -> naytaAsiakas());
        
    }
    
    
    /**
     * Kun on valittu asiakas listchooserista, niin tulostetaan se keskellä olevaan textareaan
     */
    private void naytaAsiakas() {
        Asiakas asiakasKohdalla = chooserAsiakkaat.getSelectedObject();
        
        if (asiakasKohdalla == null) return;
        
        areaAsiakas.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaAsiakas)){
            tulosta(os, asiakasKohdalla);
        }
    }
    
    /**
     * Tulostaa asiakkaan ja sen ryhmäliikunnat
     * 
     * @param os Minne tietovirtaan tulostetaan
     * @param asiakas asiakas kenet tulostetaan
     */
    private void tulosta(PrintStream os, Asiakas asiakas) {
        os.println("------------------------------------------");
        asiakas.tulosta(os);
        os.println("------------------------------------------");
        List<Ryhmaliikunta> loytyneet = kuntosali.annaRyhmaliikunnat(asiakas);
        for (Ryhmaliikunta ryhma : loytyneet)
            ryhma.tulosta(os);
        
    }
    /**
     * asetetaan kontrollerin kuntosaliviite
     * @param kuntosali kuntosali mihin viitataan
     */
    public void setKuntosali(Kuntosali kuntosali) {
        this.kuntosali = kuntosali;
    }
    
    
    /**
     * Aliohjelma, joka avaisi selaimella ikkunan, jossa näkyisi ohjeita ohjelman käyttöön kun valmis
     */
    private void apua() {
        Dialogs.showMessageDialog("Tämä painike avaisi ohjelman käytöstä kertovan sivun selaimella");
    }
    
    /**
     * Aliohjelma, joka avaa ikkunan jäsenten lisäämistä varten
     */
    private void lisaaJasen() {
    
        uusiAsiakas();
        //ModalController.showModal(HarkkaGUIController.class.getResource("LisaysGUIView.fxml"), "Jäsenen lisääminen", null, "");
    }
    
    /**
     * Aliohjelma, joka lopettaa ohjelman
     */
    private void lopeta() {
        Dialogs.showMessageDialog("Tämä painike lopettaa ohjelman");
    }
    
    /**
     * Aliohjelma, joka avaa muokkausikkunan jäsenelle
     */
    private void muokkaa(){
        ModalController.showModal(HarkkaGUIController.class.getResource("MuokkausGUIView.fxml"), "Jäsenen muokkaus", null, "");
    }
    
    /**
     * Aliohjelma, joka hoitaa tallennuksen
     */
    private void tallenna(){
        Dialogs.showMessageDialog("Vielä ei osata tallentaa jäsentä");
    }
    
    /**
     * Aliohjelma, joka hoitaa jäsenten hakemisen
     */
    private void haku() {
        Dialogs.showMessageDialog("Ei osata vielä hakea jäseniä");
    }
    
    /**
     * Aliohjelma, jolla valitaan hakukriteeri hakemiseen
     */
    private void hakuKriteeri() {
        Dialogs.showMessageDialog("Ei osata vielä valita hakukriteeriä");
    }
    
    /**
     * Aliohjelma, jolla voi avata toisen kuntosalin rekisterin
     */
    public void avaa() {
        String kuntosalinNimi = Dialogs.showInputDialog("Anna kuntosalin nimi", "Kuntosali");
        if ( kuntosalinNimi == null ) return;
    }
    
    
    /**
     * Lisää listchooserista valitulle asiakkaalle ryhmäliikunnan
     */
    private void lisaaRyhma() {
        Asiakas asiakasKohdalla  = chooserAsiakkaat.getSelectedObject();
        if ( asiakasKohdalla == null) return;
        
        Ryhmaliikunta ryhma = new Ryhmaliikunta();
        ryhma.rekisteroi();
        ryhma.taytaRyhmaliikunta(asiakasKohdalla.getTunnusNro());
        kuntosali.lisaa(ryhma);
        hae(asiakasKohdalla.getTunnusNro());
    }
    
    /**
     * Poistaa ryhmän valitulta asiakkaalta
     */
    private void poistaRyhma() {
        Dialogs.showMessageDialog("Ei osata vielä poistaa ryhmäliikuntaa");
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
                
           
    

