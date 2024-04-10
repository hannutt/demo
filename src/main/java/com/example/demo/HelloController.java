package com.example.demo;

import com.sun.management.OperatingSystemMXBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HelloController {
    HelloApplication n = new HelloApplication();
    OpenApps openApp = new OpenApps();
    public TextField osField;
    //tuodaan fxml tiedostosta
    @FXML
    public Label user;

    @FXML
    public TextArea txtBox;

    @FXML
    public Button graphBtn;

    @FXML
    public Button memBtn;

    @FXML
    public TextField pidTxt;

    @FXML
    public Button closeBtn;

    @FXML
    public MenuButton pids;
    @FXML
    public Button noteBtn;

    @FXML public MenuItem openNote,openChrome,openPaint;











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
        txtBox.setText("Operating System: " +System.getProperty("os.name")+ " version: " +System.getProperty("os.version")+" Architecture: "+System.getProperty("os.arch")+"\n"
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



        //User account name


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

    public void filesystem() {
        java.lang.String curDir = System.getProperty("user.dir");
        File[] roots = File.listRoots();



        /* For each filesystem root, print some info */
        for (File root : roots) {




            txtBox.setText("File system root: " + root.getAbsolutePath()+ "\n "+"Total Disk space (Gb): " +root.getTotalSpace()/(1024*1024*1024)+"\n" +
                    ""+ "free disk space: "+root.getUsableSpace()/(1024*1024*1024)+"\n"
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
}