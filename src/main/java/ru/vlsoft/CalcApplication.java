package ru.vlsoft;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalcApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        DBUtils.initDatabase();

        Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));

        stage.setTitle("Calculator");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        DBUtils.getConnection().close();
        Platform.exit();
    }

    private void doSomething(){
        System.out.println("som console output");
        System.out.println("som console output");
    }
}
