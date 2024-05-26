package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteFunctions {

    public void LoginForm() {
        Stage stage = new Stage();
        VBox vb = new VBox();
        Label titleLbl = new Label();
        titleLbl.setText("LOGIN PAGE");
        Button loginBtn = new Button();
        loginBtn.setText("Login");
        loginBtn.setLayoutX(0.0);
        loginBtn.setLayoutY(5.0);
        loginBtn.setOnAction(e->{
            try {
                checkCredentials();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        TextField user = new TextField();
        TextField psw = new TextField();
        vb.getChildren().addAll(titleLbl,user,psw,loginBtn);

        Scene scene = new Scene(vb,200,200);
        stage.setScene(scene);
        stage.show();


    }

    public void checkCredentials () throws SQLException {
        DBconnection conn = new DBconnection();
        Connection connDB = conn.getConnection();
        Statement stmt=connDB.createStatement();
        ResultSet rs=stmt.executeQuery("select username FROM login");

        String user="user1";
        while (rs.next())
            if (user.equals(rs.getString(1)))
            {
                System.out.println("OK");
            }

        connDB.close();

    }
}
