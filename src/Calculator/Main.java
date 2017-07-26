package Calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        primaryStage.setTitle("AКалькулятор");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        String css = this.getClass().getResource("Style/mystylesheet.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(css);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("Style/CalcSmallIcon.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}