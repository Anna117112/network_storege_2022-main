package com.geekbrains.cloud.june.cloudapplication;

import com.geekbrains.cloud.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    private final int PORT = 8289;
    private Network network;
    {
        try {
            network = new Network(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public TextField login;
    @FXML
    public TextField password;
    @FXML
    public Label serverMessage;

    public String  nik;


    @FXML
    // // кнопка войти при нажатии передаем логин и пароль на сервер где проверяем их по бд
    private void authButton () throws IOException {
        String name = login.getText();
        String pas = password.getText();
        nik = login.getText();
        network.write(new LogPass(name,pas));// передаем логин и пароль
      //  System.out.println(login.getText() + password.getText());

   // ChatApplication.setRoot("hello-view");
}


    public void registrationButton(ActionEvent actionEvent) {
    }

    private void readLoop() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                // читаем сообщения
                CloudMessage message = network.read();

                if (message instanceof Auth authMessage) {
                    Platform.runLater(() -> {
                        if (authMessage.isAuth()) {
                            // если пролучили от севрера что такой клиент есть в базе данных
                            // передаем имя дирретории чтды панель авторизации исчезла

                            try {

                                System.out.println( nik+ "login.getText()");


                               // network.write( new AuthToServer(login.getText()));
                                // переключаем панель
                                ChatApplication.setRoot("hello-view");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            serverMessage = new Label("введены неверные данные ");
                        }


                    });

                    // передаем файл с сервера на клиент
                }
            }
        }
        catch (Exception e) {
            System.err.println("Connection lost");
        }
    }

    @java.lang.Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            Thread readThread = new Thread(() -> {
                try {
                    readLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            readThread.setDaemon(true);
            readThread.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
