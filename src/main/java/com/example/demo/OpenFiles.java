package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.controlsfx.tools.Platform;

import java.io.*;
import java.util.Scanner;

public class OpenFiles {
    @FXML
    public TextArea txtBox;
    public String txtContent;

    public String setTxtContent(String con)
    {
        this.txtContent = con;
        return this.txtContent;
    }

    public String getTxtContent() {
        return this.txtContent;
    }

    //avataan valittu tiedosto ohjelmalla joka osaa lukea tiedoston.
    public void OpenSelectedFile(File fileName) throws IOException {
        Runtime.getRuntime().exec("cmd.exe /c Start " + fileName);

    }
    public void openAndShowTextbox(File file) throws IOException {
        String fileName = String.valueOf(file);
        File fileFinal = new File(fileName);
        FileInputStream fis = new FileInputStream(fileFinal);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        while((line = br.readLine()) != null){
            //process the line
            System.out.println(line);
            setTxtContent(line);


        }
        br.close();
    }

}
