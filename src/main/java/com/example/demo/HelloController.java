package com.example.demo;

import com.sun.management.OperatingSystemMXBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.net.InetAddress;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class HelloController {
    HelloApplication n = new HelloApplication();
    OpenApps openApp = new OpenApps();

    Dir dir = new Dir();

    OpenFiles openFile = new OpenFiles();


    public TextField osField;
    //tuodaan fxml tiedostosta
    @FXML
    public Label user,title;

    @FXML
    public TextArea txtBox;

    @FXML
    public Button graphBtn;

    @FXML
    public Button memBtn;

    @FXML
    public Label hiddenLbl;

    @FXML
    public TextField pidTxt;
    @FXML
    public TextField urlField;

    @FXML
    public CheckBox pingUrl;

    @FXML
    public Button closeBtn;



    @FXML
    public MenuButton pids;
    @FXML
    public Button noteBtn;

    @FXML public MenuItem openNote,openChrome,openPaint;

    @FXML
    public TextField dirInput,extField;

    @FXML
    public GridPane functionsGP;

    @FXML
    public CheckBox hiddenFiles;

    public  String HDserial;
    public String setSerial(String p)
    {
        this.HDserial = p;
        return this.HDserial;

    }

    public String getSerial() {
        return this.HDserial;
    }



    //INITIALIZE METODI SUORITETAAN HETI OHJELMAN KÄYNNISTYESSÄ TÄSSÄ TAPAUKSESSA
    //SE KUTSUU SHOWUSER METODIA, JOKA TOTEUTETAAN HETI.
    public void initialize() {

        //openApp on OpenApps luokan olio ja tässä olion avulla käytetäänn
        //openapps luokan metodia.
        openNote.setOnAction(e->{
            openApp.OpenNote();
        });
        openChrome.setOnAction((e->{
            openApp.OpenChrome();
        }));
        openPaint.setOnAction((e->{
            openApp.OpenPaint();
        }));

        showUser();



    }
    @FXML
    protected void showOs() throws IOException {

        // käyttöjärjestelmän nimi ja version asetetaan txtBox id-nimellä
        //nimettyyn kenttään
        txtBox.setText("Operating System: " +System.getProperty("os.name")+"\n" +"version: " +System.getProperty("os.version")+" Architecture: "+System.getProperty("os.arch")+"\n"
                +"Windows " + System.getProperty("sun.arch.data.model")+" bit");

    }
    @FXML
    protected void showJava() {
        //pidTxt ja closebtnia käytetään vain running apps metodin yhteydessä, joten ne tehdään
        //aina näkymättöksi.
        pidTxt.setVisible(false);
        closeBtn.setVisible(false);


        java.lang.String javav = System.getProperty("java.version");

        //Installation directory for Java Runtime Environment (JRE)
        java.lang.String direc= System.getProperty("java.home");
        java.lang.String userName = System.getProperty("user.name");

        txtBox.setText("Java version: "+ javav+" installed in: "+direc+"\n" );
        user.setText("User: " +userName);


    }
    @FXML
    protected void showMemory() {
        pidTxt.setVisible(false);
        closeBtn.setVisible(false);

        double memorySize = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize()/(1024.0*1024*1024);
        //pyöristys 2 desimaalin tarkkuuteen.
        double roundedTotalMemory = Math.floor(memorySize * 100) / 100.0;
        double freeMemory = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize()/(1024.0*1024*1024);
        double roundedFree = Math.floor(freeMemory*100)/100.0;
        double usedMemory = roundedTotalMemory - roundedFree;
        txtBox.setText("Total memory: "+roundedTotalMemory+ " GB"+"\nFree: "+roundedFree+" GB" +"\n"
                + "memory in use: "+usedMemory +" GB");




    }
    @FXML
    //system.getenv saa parametrina ympäristömuuttujan
    public void processor() throws IOException {
        pidTxt.setVisible(false);
        closeBtn.setVisible(false);

        double cpuLoad = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad();

        //prosessorin id
        java.lang.Process process = Runtime.getRuntime().exec("wmic cpu get ProcessorId");

        process.getOutputStream().close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            if (!line.trim().equals("")){
                //setteri tallettaan prosessorin id arvon
                //getteri näyttää sen textboksissa.
                n.setCpuID(line);


            }
        }
        txtBox.setText("Processor: "+ System.getenv("PROCESSOR_IDENTIFIER")+"\n"+"Architechture: "+
                System.getenv("PROCESSOR_ARCHITECTURE")+"\n"+
                "Number of processors:"+
                System.getenv("NUMBER_OF_PROCESSORS")+"\n"+"Processor ID: "+ n.getCpuID()
                +"\n"+"CPU load: "+ Math.round(cpuLoad*100.0)/100.0 +" %");






    }

    public void showUser() {
        java.lang.String userName = System.getProperty("user.name");
        user.setText("Cur. Windows username: " +userName);
    }

    public void filesystem() throws IOException {
        Process p = Runtime.getRuntime().exec("cmd.exe /c vol c: " );
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            setSerial(line.replace("Volume Serial Number is",""));

        }
        reader.close();
        java.lang.String curDir = System.getProperty("user.dir");
        File[] roots = File.listRoots();



        /* For each filesystem root, print some info */
        for (File root : roots) {




            txtBox.setText("Hard drive serial number: "+getSerial()+  "\nFile system root: " + root.getAbsolutePath()+ "\n "+"Total Disk space (Gb): " +root.getTotalSpace()/(1024*1024*1024)+"\n" +
                    ""+ "free disk space (GB): "+root.getUsableSpace()/(1024*1024*1024)+"\n"
                    + "current directory: "+curDir+" "+ System.getenv("PROGRAM_FILES"));


        }





    }

    //metodi avaa uuden ikkunan, eli graphics.fxml

    public void openGraph(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graphics.fxml"));
        Parent root1 = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Graphics");

        stage.setScene(new Scene(root1));
        stage.show();
    }

    //käynnissä olevien sovellusten haku ja näyttö
    public void showRunningApps(ActionEvent actionEvent) throws IOException, InterruptedException {
        //luodaan merkkijonolista

        List<String> runningApps = new ArrayList<String>();
        List<Integer>runningPids = new ArrayList<Integer>();
        //lisäämällä ID namen jälkeen saa process id:n
        java.lang.Process process2 = new ProcessBuilder("powershell","\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders  name,ID").start();
        java.lang.Process process = new ProcessBuilder("powershell","\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders  name,ID").start();
        new Thread(() -> {
            Scanner sc = new Scanner(process.getInputStream());
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                //poistetaan välilyönnit
                String res = line.replaceAll(" ","");
                System.out.println(res);

                //lisätään löydetyt prosessit listalle ja lisätään rivinvainto jokaisen
                //alkion jälkeen
                runningApps.add(res+"\n");
            }
            txtBox.setText(String.valueOf(runningApps));

            //metodin suorituksen myötä tekstikenttä tulee näkyviin
            pidTxt.setVisible(true);
            closeBtn.setVisible(true);
            urlField.setVisible(false);

        }).start();
        process.waitFor();
    }

    public void closeTask(ActionEvent actionEvent) {
        java.lang.String pid = pidTxt.getText();
        int pidInt = Integer.parseInt(pid);

        try{
            //prosessin sulku process id:n perusteella.
            ProcessHandle.of(pidInt).ifPresent(ProcessHandle::destroy);

        }
        catch (Exception e) {
            System.out.println("not found");
        }

    }

    public void startFc(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        File app = fc.showOpenDialog(stage);
        if (app != null) {
            //valitun tiedoston polku eli sijainti
            System.out.println(app.getAbsolutePath());
            //käytetään OpenApps luoka openselected metodia valitun
            //sovelluksen avaamiseen. file eli valittu tiedoston annetaan metodille
            //parametrina kts. myös luokasta käyttö
            openApp.OpenSelected(app);

        }

    }

    public void FcTxtFile(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            //valitun tiedoston polku eli sijainti
            System.out.println(file.getAbsolutePath());

            openFile.openAndShowTextbox(file);
            txtBox.setText(openFile.getTxtContent());

        }
    }


    public void OpenFile(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        File fileName = fc.showOpenDialog(stage);
        if (fileName != null)
        {
            //metodi avaa valitun tiedoston sopivalla ohjelmalla
            openFile.OpenSelectedFile(fileName);
        }
    }


    public void NetWork(ActionEvent actionEvent) throws IOException, InterruptedException {

        pidTxt.setVisible(true);
        pingUrl.setVisible(true);
        String con="";
        Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
        int status = process.waitFor();
        if (status ==0)
        {
            con = "Online";

        }
        else {
            con = "Offline";
        }
        txtBox.setText("IP-address: "+InetAddress.getLocalHost().getHostAddress()+"\n"+
                "Hostname: "+InetAddress.getLocalHost().getHostName()+"\n"+"Internet connection: "+con
                );
    }

    public void checkUrl(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (pingUrl.isSelected()) {
            String urlStr = pidTxt.getText();
            Process process = java.lang.Runtime.getRuntime().exec("ping "+urlStr);
            int status = process.waitFor();
            if (status == 0)
            {
                txtBox.setText(urlStr + "OK");
            }
            else {
                txtBox.setText(urlStr +"Error, can't reach");
            }

        }


    }


    public void winReg(ActionEvent actionEvent) throws IOException {
        Process process =  Runtime.getRuntime().exec("cmd reg query /?" );
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println(reader);
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            txtBox.appendText(line+"\n");
        }
        reader.close();



    }

    //haetaan kansio kovalelyltä ja tulostetaan sisältö txtboksissa.
    public void excecuteDIr(ActionEvent actionEvent) throws IOException, InterruptedException {
        String path = dirInput.getText();
        dir.DoStandardDir(path,txtBox,dirInput,hiddenFiles,hiddenLbl);

    }



    public void txtFileSrc(ActionEvent actionEvent) throws IOException, InterruptedException {
        String path = dirInput.getText();
        dir.TxtFileSrc(path,txtBox,dirInput);

    }


    public void srcOwnExt(ActionEvent actionEvent) {


    }


    //metodin suoritus tab näppäimen painalluksella ja käyttäjän syöttämällä tiedostopäätteellä.
    public void srcOwnKeyPressed(KeyEvent keyEvent) {

        extField.setOnKeyPressed(e->{
            if (e.getCode()== KeyCode.TAB)
            {
                String ext = extField.getText();
                String path = dirInput.getText();
                System.out.println("TAB pressed");
                String cmdArr[] = {"cmd","/c","dir",path};
                Process p = null;
                try {
                    p = Runtime.getRuntime().exec(cmdArr);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //Jos järjestelmäkomento tuottaa jonkin tuloksen, meidän on kaapattava tulos luomalla BufferedReader,
                // joka kääri prosessista palautetun InputStreamin eli tulostetaan tulos luettavassa mudossa.
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                String fileType;

                //tiedostotyypin selvitys


                while (true) {
                    try {
                        if (!((line = reader.readLine()) != null)) break;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    File fName = new File(line);
                    if (line.contains(ext))
                    {
                        //NÄYTETÄÄN VAIN TXT PÄÄTTEISET TIEDOSTOT
                        txtBox.appendText(line+"\n");
                    }
                }
                try {
                    reader.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

    }

    public void hideGP(ActionEvent actionEvent) {
        functionsGP.setVisible(false);
    }

    public void SubDir(ActionEvent actionEvent) {
        String path = dirInput.getText();
        //alikansioiden haku isDirectoryn avulla ja tallennus listaan
        dir.SubDirSrc(path,txtBox,dirInput);

    }
}
