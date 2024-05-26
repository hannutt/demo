package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Network {

    @FXML
    public MenuButton processID;
    @FXML
    public TextField pidTxt;

    @FXML
    public TextArea txtBox;


    public void DoWlanReport(TextArea txtBox) throws URISyntaxException, IOException, InterruptedException {
        ChoiceDialog<String> cd = new ChoiceDialog<>("Show latest report","Create new report");
        cd.showAndWait();
        String sel = cd.getSelectedItem();
        if (Objects.equals(sel, "Show latest report"))
        {
            //näytetään selaimessa wlan raportti
            java.awt.Desktop.getDesktop().browse(new URI("file:///C:/ProgramData/Microsoft/Windows/WlanReport/wlan-report-latest.html"));
        }
        else if (Objects.equals(sel,"Create new report"));
        {
            createWlanReport(txtBox);
        }
    }

    public void createWlanReport(TextArea txtBox) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("netsh wlan show wlanreport");
        new Thread(p::getInputStream).start();
        p.waitFor();
        txtBox.appendText("report created in C:\\ProgramData\\Microsoft\\Windows\\WlanReport");

    }
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
                "Hostname: " + InetAddress.getLocalHost().getHostName() + "\n" + "Internet connection: " + con+"\n");
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
                //kierrosmuuttuja
                i = i + 1;
                //wlannin lisäys menuitemiin.
                processID.getItems().add((m1));
                EventHandler<ActionEvent> wifiEvent = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        //valitun menuitemin teksti talletetaan muuttujaan
                        String wifiname = ((MenuItem)e.getSource()).getText();

                        //poistetaan muuttujan merkkijonosto whitespacet ja korvataa : merkki tyhjällä
                        //jonka jälkeen näytetään lopputulos input kentässä.
                        pidTxt.setText(wifiname.trim().replace(":",""));
                        // showPidValue(idvalue);
                        try {
                            wlandAdanced(wifiname.trim().replace(":",""),txtBox);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                };
                //menuitemeille lisätään tapahtumankäsittelijät.
                m1.setOnAction(wifiEvent);
                System.out.println(netshLine);
                txtBox.appendText(netshLine.trim()+"\n");
            }

        }).start();
        netshcom.waitFor();


    }

    public void wlandAdanced(String wifiname, TextArea txtBox) throws IOException, InterruptedException {
        System.out.println(wifiname);
        Process p = Runtime.getRuntime().exec("netsh wlan show profile name="+wifiname);
        new Thread(() -> {
            int i = 0;

            Scanner sc = new Scanner(p.getInputStream());
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {

                String wlantxt = sc.nextLine();
                txtBox.appendText(wlantxt);

            }

        }).start();
        p.waitFor();

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
