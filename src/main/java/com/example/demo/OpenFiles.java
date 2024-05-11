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

    String userChoice;

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


        String fileType;

        //tiedostotyypin selvitys
        fileType = Files.probeContentType(fileName.toPath());
        //tiedostopääte
        System.out.println(FilenameUtils.getExtension(String.valueOf(fileName)));
        System.out.println(fileType);

        //jos tiedostotyyppi on tekstitiedosto
        if (Objects.equals(fileType, "text/plain"))
        {
            //kutsutaan metodia, joka asettaa ja näyttää dialogi-ikkunnassa tekstitiedoston
            //avaamiseen sopivia vaihtoehtoja
            SetTextList();
            //jos metodissa userchoice kenttään on tallennettu (eli valittu) wordpad
            if (Objects.equals(userChoice, "Wordpad"))
            {  //avataan wordpad ja näytetään siinä valitun tiedoston sisältö.
                Runtime.getRuntime().exec("cmd.exe /c Start wordpad.exe " + fileName);

            }
            else if (Objects.equals(userChoice, "Notepad"))
            {
                Runtime.getRuntime().exec("cmd.exe /c Start notepad.exe " + fileName);

            }


        }
        else if (Objects.equals(fileType, "image/jpeg"))
        {
            SetImgList();

            if (Objects.equals(userChoice, "Paint"))
            {
                Runtime.getRuntime().exec("cmd.exe /c Start mspaint.exe " + fileName);

            }

        }
        else if (Objects.equals(fileType,"audio/mpeg"))
        {
            SetAudioList();
            if (Objects.equals(userChoice, "Media Player"))
            {
                Runtime.getRuntime().exec("cmd.exe /c Start wmplayer.exe " + fileName);

            }
        }

    }

    public void SetTextList() {
        String[] txt={"Notepad","Wordpad"};
        //dialogin vaihtoehdoiksi asetetaan txt-listan sisältö
        ChoiceDialog<String> d = new ChoiceDialog<>(txt[0],txt[1],"Cancel");
        d.showAndWait();
        //userchoice on kenttä eli luokan muuttuja johon käyttäjän valints tallennetaan.
        userChoice = d.getSelectedItem();
    }

    public void SetImgList() {
        String[] img={"Paint"};
        ChoiceDialog<String> d = new ChoiceDialog<>(img[0],"Cancel");
        d.showAndWait();
        //String choice = d.getSelectedItem();
        userChoice = d.getSelectedItem();

    }

    public void SetAudioList() {
        String[] audio={"Media Player"};
        ChoiceDialog<String> d = new ChoiceDialog<>(audio[0],"Cancel");
        d.showAndWait();
        //String choice = d.getSelectedItem();
        userChoice = d.getSelectedItem();
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
