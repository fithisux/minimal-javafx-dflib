package org.ntua.trapezoid;

import java.io.IOException;
import org.ntua.trapezoid.ui.MainApp;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TULauncher extends Application {

    public Stage primaryStage;
    private BorderPane rootLayout;

    @FXML
    MainApp mainApp;

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TULauncher.class.getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            this.mainApp = loader.getController();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("CanvasXpress JavaFX demo");
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                	System.exit(0);;
                }
            });
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TrapezoidApp");
        initRootLayout();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
