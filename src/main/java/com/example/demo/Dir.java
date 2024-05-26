package com.example.demo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Dir {

    String dirPath;


    @FXML
    public CheckBox hiddenFiles;
    @FXML
    public TextField dirInput;


    public String FinaFileName;
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


    //tyhjien kansioiden selvitys
    public void DoListOfEmptydirs(String path, TextArea txtBox) throws IOException {


        File parentDir = new File(path);
        if(parentDir.isDirectory() && parentDir.list().length == 0) {
            txtBox.appendText("Directory "+ path + " is empty");
        } else {
            txtBox.appendText("Directory "+ path +  " is not empty");
        }

    }
    public void ClickNdrop(TextField dirInput) throws IOException {
        Stage stage = new Stage();
        VBox vb = new VBox();
        Label title = new Label("CLICK & DROP");

        Path dir = Paths.get("c:\\");
        String dirStr = String.valueOf(dir);
        vb.getChildren().add(title);

        //maxdepthin avulla määritetäänt kuinka monta alikansiota näytetään haussa, 1=ensimmäiset kansiot
        List<String> dirs=new ArrayList<>();
        Files.walk(dir, 1)
                .filter(p -> Files.isDirectory(p) && ! p.equals(dir))
                .forEach(p -> dirs.add(String.valueOf(p.getFileName())));
        int listSize = dirs.size();
        for (int i =0;i<listSize;i++) {

            //luodaan label komponentteja for-silmukassa yhtä monta kuin dirs-listassa on
            //alkioita. for-silmukkaa käytetään kun tiedetään tarkasti montako kierrosta tarvitaan.
            Label pathLbl = new Label();

            //tapahtumakäsittelijä hiiren painallukselle eli siirretään klikatun labelin teksti
            //dirinput kenttään
            EventHandler<MouseEvent> PathClick = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    String p = pathLbl.getText();
                    dirInput.setText(p);
                    //ikkunan minimointi
                    stage.getScene().getWindow();
                    stage.setIconified(true);



                }

            };
            pathLbl.setText(String.valueOf(dirStr  + dirs.get(i)));
            vb.getChildren().add(pathLbl);
            pathLbl.setOnMousePressed(PathClick);
        }


        Scene scene = new Scene(vb, 300, 300);

        // set the scene
        stage.setScene(scene);

        stage.show();


    }

    public void DoSearch(TextField dirInput, TextArea txtBox, String path) throws IOException {

        TextInputDialog td = new TextInputDialog();
        td.setTitle("File search");
        td.setContentText("Enter a file name: ");
        Optional<String>res = td.showAndWait();
        if (res.isPresent()) {
            System.out.println(res.get());
            FinaFileName = res.get();
        }

        String fileName = "timenow.py";
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
            if (line.contains(FinaFileName))
            {
                txtBox.appendText(line+ " found.");

            }
        }


        reader.close();



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

    public void DoHiddenFileSearch(String path, TextArea txtBox) throws IOException {
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
            txtBox.appendText(line + "\n");
            }

            reader.close();

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
