module com.geekbrains.cloud.june.cloudapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.codec;
    requires com.geekbrains.cloud.june.model;
    requires lombok;
    opens com.geekbrains.cloud.june.cloudapplication to javafx.fxml;
    exports com.geekbrains.cloud.june.cloudapplication;
}