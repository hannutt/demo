module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires jdk.management;
    requires java.desktop;
    requires javafx.swing;
    requires java.prefs;
    requires org.apache.commons.io;
    requires java.sql;
    requires mysql.connector.j;
    requires jdk.security.auth;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}