package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class Dir {

    String dirPath;


    @FXML
    public CheckBox hiddenFiles;
    @FXML
    public TextField dirInput;



    @FXML
    public TextArea txtBox;
    public  String PathName;
    public String setPath(String p)
    {
        this.PathName = p;
        return this.PathName;

    }

    public String getPath() {
        return this.PathName;
    }

    public void DoDirBySize(String path, TextArea txtBox, TextField dirInput) throws IOException {

        if (dirInput.getText().isEmpty()) {
            //textfieldin taustaväri muutos punaiseksi jos se on tyhjä.
            dirInput.setStyle("-fx-background-color: red;");
            //dirInput.setStyle("-fx-background-color: white;");

        }
        else{
            String cmdArr[] = {"cmd", "/c", "dir/o:s", path};
            Process p = Runtime.getRuntime().exec(cmdArr);
            //Jos järjestelmäkomento tuottaa jonkin tuloksen, meidän on kaapattava tulos luomalla BufferedReader,
            // joka kääri prosessista palautetun InputStreamin eli tulostetaan tulos luettavassa mudossa.
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                File fName = new File(line);

                txtBox.appendText(line + "\n");
            }
            reader.close();

        }

    }

    public void DoBareFormatDir(String path, TextArea txtBox, TextField dirInput) throws IOException {
        if (dirInput.getText().isEmpty()) {
            //textfieldin taustaväri muutos punaiseksi jos se on tyhjä.
            dirInput.setStyle("-fx-background-color: red;");
            //dirInput.setStyle("-fx-background-color: white;");

        }
        else{
            String cmdArr[] = {"cmd", "/c", "dir/b", path};
            Process p = Runtime.getRuntime().exec(cmdArr);
            //Jos järjestelmäkomento tuottaa jonkin tuloksen, meidän on kaapattava tulos luomalla BufferedReader,
            // joka kääri prosessista palautetun InputStreamin eli tulostetaan tulos luettavassa mudossa.
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                File fName = new File(line);

                txtBox.appendText(line + "\n");
            }
            reader.close();

        }

    }


    public void DoStandardDir(String path, TextArea txtBox, TextField dirInput, CheckBox hiddenFiles, Label hiddenLbl) throws IOException {
        if (dirInput.getText().isEmpty()) {
            //textfieldin taustaväri muutos punaiseksi jos se on tyhjä.
             dirInput.setStyle("-fx-background-color: red;");
            //dirInput.setStyle("-fx-background-color: white;");

        }
        else if (hiddenFiles.isSelected())
        {

            String cmdArr[] = {"cmd", "/c", "dir/a:h", path};
            Process p = Runtime.getRuntime().exec(cmdArr);
            //Jos järjestelmäkomento tuottaa jonkin tuloksen, meidän on kaapattava tulos luomalla BufferedReader,
            // joka kääri prosessista palautetun InputStreamin eli tulostetaan tulos luettavassa mudossa.
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            String fileType;

            //tiedostotyypin selvitys


            while ((line = reader.readLine()) != null) {
                File fName = new File(line);
                if (line.contains(".txt")) {

                    //NÄYTETÄÄN VAIN TXT PÄÄTTEISET TIEDOSTOT
                    //txtBox.appendText(line+"\n");
                }

                txtBox.appendText(line + "\n");
            }

            reader.close();
        }


        else {


            String cmdArr[] = {"cmd", "/c", "dir", path};
            String cmdHidden[] = {"cmd", "/c", "dir/a:h", path};
            Process p = Runtime.getRuntime().exec(cmdArr);
            Process pHidden = Runtime.getRuntime().exec(cmdHidden);
            //Jos järjestelmäkomento tuottaa jonkin tuloksen, meidän on kaapattava tulos luomalla BufferedReader,
            // joka kääri prosessista palautetun InputStreamin eli tulostetaan tulos luettavassa mudossa.
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader hiddenFileReader = new BufferedReader(new InputStreamReader(pHidden.getInputStream()));
            String line;
            String hidFiles;
            String fileType;

            /*
            Path path2 = Paths.get(folder+"\\typingGui.py");
            long bytes = Files.size(path2);
            System.out.println(String.format("%,d kilobytes", bytes / 1024));
            */
            //tiedostotyypin selvitys


            while ((line = reader.readLine()) != null) {
                File fName = new File(line);
                txtBox.appendText(line + "\n");

                while ((hidFiles = hiddenFileReader.readLine()) !=null) {
                    File Hfile = new File(hidFiles);

                    txtBox.appendText(hidFiles+"\n");
                    System.out.println(hidFiles);


                }


            }
            reader.close();
            hiddenFileReader.close();
        }

    }


    //Alikansioiden haku
    public void SubDirSrc(String path, TextArea txtBox, TextField dirInput) {

        File Fname = new File(path);
        String[] names = Fname.list();
        for(String name : names)
        {
            if (new File(path+"\\" + name).isDirectory())
            {
                txtBox.appendText(name+"\n");
            }
        }
    }


    //.txt päätteisten tiedostojen haku
    public void TxtFileSrc (String path, TextArea txtBox, TextField dirInput) throws IOException {
        ////textfieldin taustaväri muutos punaiseksi, jos polkua ei ole annettu.
        if (path.isEmpty())
        {

            System.out.println("invalid input");
            dirInput.setStyle("-fx-background-color: red;");


        }
        else {


            String cmdArr[] = {"cmd","/c","dir",path};
            Process p = Runtime.getRuntime().exec(cmdArr);
            //Jos järjestelmäkomento tuottaa jonkin tuloksen, meidän on kaapattava tulos luomalla BufferedReader,
            // joka kääri prosessista palautetun InputStreamin eli tulostetaan tulos luettavassa mudossa.
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            String fileType;

            //tiedostotyypin selvitys


            while ((line = reader.readLine()) != null) {
                File fName = new File(line);
                if (line.contains(".txt"))
                {//NÄYTETÄÄN VAIN TXT PÄÄTTEISET TIEDOSTOT
                    txtBox.appendText(line+"\n");

                }
            }

            reader.close();
        }
    }








}
