package harkkapackage;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author antti
 * @version 7.6.2020
 *
 */
public class HarkkaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("HarkkaGUIView.fxml"));
            final Pane root = ldr.load();
            //final HarkkaGUIController harkkaCtrl = (HarkkaGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("harkka.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("harkka");
            primaryStage.show();
        } catch(Exception e) {
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