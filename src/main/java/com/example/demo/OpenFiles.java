package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;

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
        String[] txt={"Notepad","Wordpad"};
        String fileType;

        //tiedostotyypin selvitys
        fileType = Files.probeContentType(fileName.toPath());
        //tiedostopääte
        System.out.println(FilenameUtils.getExtension(String.valueOf(fileName)));
        System.out.println(fileType);
        if (Objects.equals(fileType, "text/plain"))
        {
            //alasvetovalikko
            ChoiceDialog<String> d = new ChoiceDialog<>(txt[0],txt[1],"Cancel");
            d.showAndWait();
            String choice = d.getSelectedItem();
            if (Objects.equals(choice, "Wordpad"))
            {
                Runtime.getRuntime().exec("cmd.exe /c Start wordpad.exe " + fileName);

            }
            else if (Objects.equals(choice, "Notepad"))
            {
                Runtime.getRuntime().exec("cmd.exe /c Start notepad.exe " + fileName);

            }
            else {
                d.hide();
            }

        }






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
