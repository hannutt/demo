package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

public class DeleteFunctions {
    @FXML
    public TextField userDirInput;

    @FXML
    public ListView fileList;

    @FXML
    public Button bareDirBtn;

    public int loginAttemptsLeft = 3;

    public boolean txtDelete=false;

    public void intitialize() {


    }

    public void LoginForm() {
        Stage stage = new Stage();
        VBox vb = new VBox();
        Label titleLbl = new Label();
        titleLbl.setText("LOGIN PAGE");
        Label loginAtt = new Label();
        loginAtt.setText("Login attempts: " + loginAttemptsLeft);
        Button loginBtn = new Button();
        loginBtn.setText("Login");

        TextField userInput = new TextField();
        TextField pswInput = new TextField();
        loginBtn.setLayoutX(0.0);
        loginBtn.setLayoutY(5.0);
        loginBtn.setOnAction(e -> {
            try {
                checkCredentials(userInput, pswInput, loginAtt, loginBtn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        vb.getChildren().addAll(titleLbl, userInput, pswInput, loginBtn, loginAtt);

        Scene scene = new Scene(vb, 200, 200);
        stage.setScene(scene);
        stage.show();


    }

    public void checkCredentials(TextField userInput, TextField pswInput, Label loginAtt, Button loginBtn) throws SQLException, IOException {
        DBconnection conn = new DBconnection();
        Connection connDB = conn.getConnection();
        Statement stmt = connDB.createStatement();
        ResultSet rs = stmt.executeQuery("select username,password FROM login");


        while (rs.next())
            if (userInput.getText().equals(rs.getString(1)) && pswInput.getText().equals(rs.getString(2))) {
                OpenDeleteWindow();
            } else {
                loginAttemptsLeft = loginAttemptsLeft - 1;
                loginAtt.setText(STR."Login attempts: \{loginAttemptsLeft}");

            }
        if (loginAttemptsLeft == 0) {
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

    //dir-listaus bare-format muodossa.
    public void showFilesFromFolder(ActionEvent actionEvent) throws IOException {
        String txt = bareDirBtn.getText();


        String path = userDirInput.getText();
        String cmdArr[] = {"cmd", "/c", "dir/b", path};
        Process p = Runtime.getRuntime().exec(cmdArr);
        //Jos järjestelmäkomento tuottaa jonkin tuloksen, meidän on kaapattava tulos luomalla BufferedReader,
        // joka kääri prosessista palautetun InputStreamin eli tulostetaan tulos luettavassa mudossa.
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {

            File fName = new File(line);
            //tiedostolistauksen näyttö listview komponentissa
            fileList.getItems().add(line + "\n");
            }
            reader.close();



    }


    public void filesListClick(MouseEvent mouseEvent) throws IOException {
        String path = userDirInput.getText().trim();



        //replace täytyy tehdä 2 muuttujan avulla, koska muuten ohjelma ei huomio
        //replace komentoa.

        String file= fileList.getSelectionModel().getSelectedItems().toString();
        String finaFile= STR."\{path}\\\{file.replace("[", "").replace("]", "")}".trim();
        if (!file.isEmpty())
        {

            DeleteSelFile(finaFile);
        }


    }


    public void DeleteSelFile(String finaFile) throws IOException {

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Delete");
        a.setContentText("You are deleting "+finaFile+" are you sure?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            Path p = Paths.get(finaFile);
            Files.delete(p);
            System.out.println(finaFile +" deleted");
        }









    }
}




