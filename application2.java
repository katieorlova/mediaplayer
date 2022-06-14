package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Компонент - класс HelloApplicationNew, отвечающий за запуск MP3-плеера
 */
public class HelloApplicationNew extends Application {
    @Override

    /**
     * Метод start отвечает за запуск данной программы
     * @throws I0Exception при возникновении ошибки запуска
     */
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface1New.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(arg0 -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Метод main отвечает за
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}