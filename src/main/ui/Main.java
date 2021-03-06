/**
 * Obsahuje tridu Main, ktera predstavuje aplikaci s gui.
 *
 * @author Miroslav Válka (xvalka05)
 * @author Jan Trněný (xtrnen03)
 */
package main.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Aplikace.
 */
public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
      System.out.println(getClass().getResource("Sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
        primaryStage.setTitle("SchemeBuilder 1.1 alpha");
        primaryStage.setScene(new Scene(root, 800, 600));

        Main.primaryStage = primaryStage;

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
