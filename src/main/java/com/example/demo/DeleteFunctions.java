package com.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteFunctions {

    public int loginAttemptsLeft=3;

    public void LoginForm() {
        Stage stage = new Stage();
        VBox vb = new VBox();
        Label titleLbl = new Label();
        titleLbl.setText("LOGIN PAGE");
        Label loginAtt = new Label();
        loginAtt.setText("Login attempts: "+loginAttemptsLeft);
        Button loginBtn = new Button();
        loginBtn.setText("Login");

        TextField userInput = new TextField();
        TextField pswInput = new TextField();
        loginBtn.setLayoutX(0.0);
        loginBtn.setLayoutY(5.0);
        loginBtn.setOnAction(e->{
            try {
                checkCredentials(userInput,pswInput,loginAtt,loginBtn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        vb.getChildren().addAll(titleLbl,userInput,pswInput,loginBtn,loginAtt);

        Scene scene = new Scene(vb,200,200);
        stage.setScene(scene);
        stage.show();


    }

    public void checkCredentials (TextField userInput, TextField pswInput, Label loginAtt, Button loginBtn) throws SQLException, IOException {
        DBconnection conn = new DBconnection();
        Connection connDB = conn.getConnection();
        Statement stmt=connDB.createStatement();
        ResultSet rs=stmt.executeQuery("select username,password FROM login");


        while (rs.next())
            if (userInput.getText().equals(rs.getString(1)) && pswInput.getText().equals(rs.getString(2)))
            {
                OpenDeleteWindow();
            }
        else {
            loginAttemptsLeft = loginAttemptsLeft -1;
            loginAtt.setText(STR."Login attempts: \{loginAttemptsLeft}");

            }
        if (loginAttemptsLeft==0)
        {
            loginBtn.setDisable(true);

        }
            
        connDB.close();


    }

    public void OpenDeleteWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileRemover.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("File Remover");

        stage.setScene(new Scene(root1));
        stage.show();

    }
}
