package harkkapackage;

import javafx.fxml.FXML;
import fi.jyu.mit.fxgui.*;

/**
 * @author antti
 * @version 12.6.2020
 *
 */
public class HarkkaGUIController {


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
    
    
    
   // @FXML
    //void handleLisaaUusiRyhmaNappi() {
    //     lisaaUusiRyhmaNappi();
   // }
 //--------------------------------------------------------------------------------
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
    ModalController.showModal(HarkkaGUIController.class.getResource("LisaysGUIView.fxml"), "Jäsenen lisääminen", null, "");
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
    private void avaa() {
        String kuntosalinNimi = Dialogs.showInputDialog("Anna kuntosalin nimi", "Kuntosali");
        if ( kuntosalinNimi == null ) return;
    }
    
    
    private void lisaaRyhma() {
        ModalController.showModal(HarkkaGUIController.class.getResource("RyhmaLisaysGUIView.fxml"), "Uuden ryhmäliikunnan lisäys", null, "");
    }
    
    private void poistaRyhma() {
        Dialogs.showMessageDialog("Ei osata vielä poistaa ryhmäliikuntaa");
    }
    
    
   // private void lisaaUusiRyhmaNappi(){
     //   Dialogs.showMessageDialog("Ei osata vielä lisätä ryhmäliikuntaa");
   // }
    
}
                
           
    

