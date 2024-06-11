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

    //tähän tallennetaan käyttäjän dialogista valitseman ohjelman nimi.
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
            //suoritetaan cmd komento, userchoiceen on taletettu valitun ohjelman nimi. lisätään
            //loppuun .exe pääte niin ohjelma voidaan avata.
            Runtime.getRuntime().exec("cmd.exe /c Start " +userChoice +".exe " + fileName);
            //jos metodissa userchoice kenttään on tallennettu (eli valittu) wordpad


        }
        else if (Objects.equals(fileType, "image/jpeg"))
        {
            SetImgList();
            Runtime.getRuntime().exec("cmd.exe /c Start "+userChoice +".exe "+ fileName);



        }
        else if (Objects.equals(fileType,"audio/mpeg"))
        {
            SetAudioList();

            Runtime.getRuntime().exec("cmd.exe /c Start " +userChoice+".exe " + fileName);


        }
        else if (Objects.equals(fileType,"application/pdf"))
        {
            SetPDFList();

            Runtime.getRuntime().exec("cmd.exe /c Start "+userChoice +".exe "+ fileName);


        }


    }

    public void SetTextList() {
        String[] txt={"notepad","wordpad"};
        //dialogin vaihtoehdoiksi asetetaan txt-listan sisältö
        ChoiceDialog<String> d = new ChoiceDialog<>(txt[0],txt[1],"Cancel");
        d.showAndWait();
        //userchoice on kenttä eli luokan muuttuja johon käyttäjän valints tallennetaan.
        userChoice = d.getSelectedItem();
    }

    public void SetImgList() {
        String[] img={"mspaint"};
        ChoiceDialog<String> d = new ChoiceDialog<>(img[0],"Cancel");
        d.showAndWait();
        //String choice = d.getSelectedItem();
        userChoice = d.getSelectedItem();

    }

    public void SetAudioList() {
        String[] audio={"wmplayer","vlc"};
        ChoiceDialog<String> d = new ChoiceDialog<>(audio[0],audio[1],"Cancel");
        d.showAndWait();
        //String choice = d.getSelectedItem();
        userChoice = d.getSelectedItem();
    }

    public void SetPDFList() {
        String[] browsers={"msedge","Firefox","Chrome"};
        ChoiceDialog<String> d = new ChoiceDialog<>(browsers[0],browsers[1],browsers[2],"Cancel");
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
