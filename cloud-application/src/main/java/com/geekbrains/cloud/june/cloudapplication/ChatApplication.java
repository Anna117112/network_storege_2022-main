package com.geekbrains.cloud.june.cloudapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class ChatApplication extends Application {
    private static Scene scene;

    @java.lang.Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("auth"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("auth.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Chat");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//
//    public static void main(String[] args) {
//        launch();
//    }
}