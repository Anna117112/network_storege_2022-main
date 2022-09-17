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

public class ResponceController implements Initializable {
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
    public TextField responceLogin;
    @FXML
    public TextField responcePassword1;
    @FXML
    public TextField responcePassword2;
    @FXML
    public Label responceMessage;


    public void openPanelAuth(ActionEvent actionEvent) {
        if ((responceLogin.getText().isEmpty()) && (responcePassword1.getText().isEmpty())&&(responcePassword2.getText().isEmpty()) ){
            System.out.println("Введите логин  и пароль " );
        }
        else if (!(responcePassword1.getText().equals(responcePassword2.getText()))){
            System.out.println("Пароли не совпадают ");
        }
        else {
            try {
                network.write( new Responce(responceLogin.getText(), responcePassword1.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    private void readLoop() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                // читаем сообщения
                CloudMessage message = network.read();

                if (message instanceof ResponceToClient responceMessage) {
                    Platform.runLater(() -> {
                        if (responceMessage.isResponce()) {
                            // если пролучили от севрера что такой клиент есть в базе данных
                            // передаем имя дирретории чтды панель авторизации исчезла

                            try {

                                ChatApplication.setRoot("auth");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            System.out.println("Такой пользователь уже зарегистрирован вернитесь назад и войдите ");
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
        System.out.println("Подключисля контроллер регистрации");

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
    }

    public void back(ActionEvent actionEvent) {
        try {
            ChatApplication.setRoot("auth");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
