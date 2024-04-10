package com.example.demo;

import javafx.scene.control.TextInputDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenApps {
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
