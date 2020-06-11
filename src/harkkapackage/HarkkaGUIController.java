package harkkapackage;

import javafx.fxml.FXML;
import fi.jyu.mit.fxgui.*;

/**
 * @author antti
 * @version 7.6.2020
 *
 */
public class HarkkaGUIController {


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
    
 //--------------------------------------------------------------------------------
    private void apua() {
        Dialogs.showMessageDialog("Tämä painike avaisi ohjelman käytöstä kertovan sivun selaimella");
    }
    
    
    private void lisaaJasen() {
    ModalController.showModal(HarkkaGUIController.class.getResource("LisaysGUIView.fxml"), "Jäsenen lisääminen", null, "");
    }
    
    
    private void lopeta() {
        Dialogs.showMessageDialog("Tämä painike lopettaa ohjelman");
    }
    
    
    private void muokkaa(){
        ModalController.showModal(HarkkaGUIController.class.getResource("MuokkausGUIView.fxml"), "Jäsenen muokkaus", null, "");
    }
    
    
    private void tallenna(){
        Dialogs.showMessageDialog("Vielä ei osata tallentaa jäsentä");
    }
    
    
    private void haku() {
        Dialogs.showMessageDialog("Ei osata vielä hakea jäseniä");
    }
    
    private void hakuKriteeri() {
        Dialogs.showMessageDialog("Ei osata vielä valita hakukriteeriä");
    }
    
    private void avaa() {
        String kuntosalinNimi = Dialogs.showInputDialog("Anna kuntosalin nimi", "Kuntosali");
        if ( kuntosalinNimi == null ) return;
    }
}
                
           
    

