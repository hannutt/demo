package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeleteFunctions {

    @FXML
    public MenuButton directories;
    @FXML
    public TextField userDirInput;

    @FXML
    public ListView fileList;

    @FXML
    public Button bareDirBtn;

    public int loginAttemptsLeft = 3;

    public boolean isAdmin=false;


    public boolean returnUserAccount() throws IOException {
        //tarkistetaan windows käyttäjä
        Process p = Runtime.getRuntime().exec("whoami");
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        String result;
        while ((line = reader.readLine()) != null) {
            result=line;
            //jos ehto pitää paikkansa isadmin boolean muuttuu trueksi
            //isadmin booleania käytetään myöhemmin alempana koodissa
            if (result.equals("desktop-i10k48a\\omistaja"))
            {
                isAdmin=true;
                System.out.println(line);
                System.out.println(isAdmin);
                //isadmin boolean saa arvoksi true, jos ehto pitää paikkansa
                return true;
            }
            else {
                isAdmin=false;
                return false;
            }
        }
        reader.close();
        //tässä on pakko käyttää return komentoa, joten palautetaan boolean, ei tulosta (true/false)
        return isAdmin;

    }

    public void LoginForm() throws IOException {
        returnUserAccount();
        Stage stage = new Stage();
        VBox vb = new VBox();
        Label titleLbl = new Label();
        titleLbl.setText("LOGIN PAGE");
        Label loginAtt = new Label();
        loginAtt.setText("Login attempts: " + loginAttemptsLeft);
        Button loginBtn = new Button();
        loginBtn.setText("Login");
        Button skipLogin = new Button();
        skipLogin.setText("Skip login");
        skipLogin.setOpacity(0.0);
        TextField userInput = new TextField();
        PasswordField pswInput = new PasswordField();

        loginBtn.setLayoutX(0.0);
        String userName= System.getProperty("user.name");
        System.out.println(userName);
        //jos isadmin on true näytetään kirjautumisen ohituspainike
        if (isAdmin)
        {
            skipLogin.setOpacity(1.0);
            skipLogin.setOnAction(e->{
                try {

                    OpenDeleteWindow();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

        }
        //jos isadmin on false, piilotetaan skiplogin button
       if (!isAdmin)
       {
           skipLogin.setOpacity(0.0);
       }
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


        vb.getChildren().addAll(titleLbl, userInput, pswInput, loginBtn, loginAtt,skipLogin);

        Scene scene = new Scene(vb, 200, 200);
        stage.setScene(scene);
        stage.show();



    }


    //kirjautumistietojen tarkastus eli haetaan sql login taulusta
    public void checkCredentials(TextField userInput, TextField pswInput, Label loginAtt, Button loginBtn) throws SQLException, IOException {
        DBconnection conn = new DBconnection();
        Connection connDB = conn.getConnection();
        Statement stmt = connDB.createStatement();
        ResultSet rs = stmt.executeQuery("select username,password FROM login");


        while (rs.next())
            //jos tiedot täsmäävät eli syötenttiin syötetyt tiedon löytyvät login taulusta kutsutaan open
            //delete metodia, joka avaa uuden ikkunan.
            if (userInput.getText().equals(rs.getString(1)) && pswInput.getText().equals(rs.getString(2))) {
                OpenDeleteWindow();
            } else {
                //kirjautumisyrityksiä on 3 jos kirjautuminen menee väärin, vähennetään loginattemps kentästä luku 1
                loginAttemptsLeft = loginAttemptsLeft - 1;
                loginAtt.setText(STR."Login attempts: \{loginAttemptsLeft}");

            }
        //jos loginattemptsleft menee nollan, disabloidaan loginbutton
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
        //file merkkijonoon talletaan valitun rivin sisältö.
        String file= fileList.getSelectionModel().getSelectedItems().toString();
        //huomaa trim metodi lopussa, eli välilyöntien poisto, ilman sitä polku on virheellinen
        String finaFile= STR."\{path}\\\{file.replace("[", "").replace("]", "")}".trim();
        if (!file.isEmpty())
        {

            DeleteSelFile(finaFile);
        }


    }


    public void DeleteSelFile(String finaFile) throws IOException {
        //vahvistusikkuna
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Delete");
        a.setContentText("You are deleting "+finaFile+" are you sure?");
        Optional<ButtonType> result = a.showAndWait();
        //jos ok-painiketta painetaan
        if (result.get() == ButtonType.OK) {
            //luodaan tiedostopolku finalfilen merkkijonosta
            Path p = Paths.get(finaFile);
            Files.delete(p);
            System.out.println(finaFile +" deleted");
        }









    }


    //c:\ aseman kansioiden listaus ja niiden vienti menubuttonin välilehtiin
    public void dirC(ActionEvent actionEvent) throws IOException {
        Path dir = Paths.get("c:\\");
        String dirStr = String.valueOf(dir);
        //maxdepthin avulla määritetäänt kuinka monta alikansiota näytetään haussa, 1=ensimmäiset kansiot
        List<String> dirs=new ArrayList<>();
        Files.walk(dir, 1)
                .filter(p -> Files.isDirectory(p) && ! p.equals(dir))
                .forEach(p -> dirs.add(String.valueOf(p.getFileName())));
        int listSize = dirs.size();
        for (int i =0;i<listSize;i++) {
            //luodaan yhtä monta menuitemiä kuin kansioita on listassa
            MenuItem mi = new MenuItem();
            mi.setText("C:\\"+dirs.get(i));
            directories.getItems().add(mi);
            //luodaan tapahtumankäsittelijä for-silmukan sisässä, muuten handle metodi ei tunnista
            //mi-menuitemia
            EventHandler<ActionEvent> PathClick = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String p =mi.getText();
                    userDirInput.setText(p);

                }
            };
            //lisätään tapahtumankäsittelinä menuitem komponenttiin.
            mi.setOnAction(PathClick);
        }




    }
}




