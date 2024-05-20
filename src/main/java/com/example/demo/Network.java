package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Network {

    @FXML
    public MenuButton processID;
    @FXML
    public TextField pidTxt;

    public void ShowNetworkInfo(TextField pidTxt, CheckBox pingUrl, TextArea txtBox, MenuButton processID) throws IOException, InterruptedException {
        pidTxt.setVisible(true);
        pingUrl.setVisible(true);
        processID.setText("WLAN");
        String con = "";
        Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
        int status = process.waitFor();
        if (status == 0) {
            con = "Online";

        } else {
            con = "Offline";
        }

        txtBox.setText("IP-address: " + InetAddress.getLocalHost().getHostAddress() + "\n" +
                "Hostname: " + InetAddress.getLocalHost().getHostName() + "\n" + "Internet connection: " + con);
        Process netshcom = Runtime.getRuntime().exec("netsh wlan show profile");
        List<String> wlans = new ArrayList<String>();
        new Thread(() -> {
            int i = 0;

            Scanner sc = new Scanner(netshcom.getInputStream());
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {

                String netshLine = sc.nextLine();
                //korvataan all user profile teksti tyhjällä.
                wlans.add(netshLine.replace("All User Profile",""));
                MenuItem m1 = new MenuItem(wlans.get(i));
                i = i + 1;
                //prosessi id:n lisäys menuitemiin.
                processID.getItems().add((m1));




                System.out.println(netshLine);
                txtBox.appendText(netshLine.trim()+"\n");



            }

        }).start();
        netshcom.waitFor();


    }


    public void DoPing(TextArea txtBox, CheckBox pingUrl, TextField pidTxt, CheckBox wView) throws IOException, InterruptedException {
        if (pingUrl.isSelected()) {
            String urlStr = pidTxt.getText();
            Process process = java.lang.Runtime.getRuntime().exec("ping " + urlStr);
            int status = process.waitFor();
            if (status == 0) {
                txtBox.setText(urlStr + " OK");
                wView.setVisible(true);

            } else {
                txtBox.setText(urlStr + " Error, can't reach");
            }

        }
    }

    public void DoWebView(TextField pidTxt) {
        Stage stage = new Stage();
        WebView wv = new WebView();
        wv.minHeight(1050);
        wv.prefHeight(1070);
        wv.prefWidth(1950);
        wv.minHeight(1050);

        final WebEngine weEng = wv.getEngine();
        weEng.load(STR."https://\{pidTxt.getText()}");
        VBox vb = new VBox(wv);
        Scene scene = new Scene(vb);
        stage.setScene(scene);
        stage.show();

    }


}
