package com.example.demo;

import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenApps {

// metodilla avataan file chooserilla valittu tiedosto valittu tiedosto on parametrina.
    public void OpenSelected(File app) {
        ProcessBuilder pb = new ProcessBuilder(String.valueOf(app));
        try {
            pb.start();
        }
        catch (Exception e) {
        }
    }
    public void OpenNote() {
        ProcessBuilder pb = new ProcessBuilder("Notepad.exe");
        try {
            pb.start();
        }
        catch (Exception e) {
        }
    }

    public void OpenChrome() {
        //ikkuna ja syötekenttä
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText(" Enter URL: ");
        td.showAndWait();
        //syötekentän tekstin tallennus muuttujaan.
        String address = td.getEditor().getText();
        try {
            java.awt.Desktop.getDesktop().browse(new URI(address));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


    }

    public void OpenPaint() {
        ProcessBuilder pb = new ProcessBuilder("mspaint.exe");
        try {
            pb.start();
        }
        catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
