package fxKuntosali;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import kuntosali.Kuntosali;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * @author antti
 * @version 12.6.2020
 *
 */
public class HarkkaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(
                    getClass().getResource("PaavalikkoGUIView.fxml"));
            final Pane root = (Pane) ldr.load();
            final HarkkaGUIController kuntosaliCtrl = (HarkkaGUIController) ldr
                    .getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets()
                    .add(getClass().getResource("harkka.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kuntosalin Asiakasrekisteri");

            Kuntosali kuntosali = new Kuntosali(); // Luodaan kuntosali
            kuntosaliCtrl.setKuntosali(kuntosali); // Asetetaan kuntosali
                                                   // controllerille

            primaryStage.setOnCloseRequest((event) -> { // Suljettaessa
                                                        // tarkistaa voiko
                                                        // sulkea
                if (!kuntosaliCtrl.voikoSulkea())
                    event.consume();
            });

            primaryStage.show();
            primaryStage.setResizable(false); // ei voi muuttaa kokoa
            if (!kuntosaliCtrl.avaa())
                Platform.exit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}