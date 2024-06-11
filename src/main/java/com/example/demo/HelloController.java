package com.example.demo;


import com.sun.management.OperatingSystemMXBean;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.List;


public class HelloController {


    public LocalDateTime timenow = LocalDateTime.now();
    public DateTimeFormatter timenowFormatted = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String timeToStr;
    HelloApplication n = new HelloApplication();
    OpenApps openApp = new OpenApps();

    Customization c = new Customization();


    Network net = new Network();
    Dir dir = new Dir();
    DeleteFunctions df = new DeleteFunctions();



    OpenFiles openFile = new OpenFiles();

    @FXML
    public TextFlow DBvalues;
    public TextField osField;
    //tuodaan fxml tiedostosta
    @FXML
    public MenuItem item1;
    @FXML
    public Label user, title,titleLbl;

    @FXML
    public TextArea txtBox;

    @FXML
    public Button graphBtn,findBtn,dirBtn,hideBtn;

    @FXML
    public MenuButton processID,select,advDir;
    @FXML
    public VBox mainV;
    @FXML
    public Button memBtn;

    @FXML
    public Label hiddenLbl,UItip,fwstatus;

    @FXML
    public TextField pidTxt;
    @FXML
    public TextField urlField;

    @FXML
    public CheckBox pingUrl,wView;

    @FXML
    public Button closeBtn;


    @FXML
    public MenuButton pids,directories;
    @FXML
    public Button noteBtn,fin;

    @FXML
    public MenuItem openNote, openChrome, openPaint;

    @FXML
    public TextField dirInput, extField;


    @FXML
    public GridPane functionsGP;

    @FXML
    public CheckBox hiddenFiles;

    public String HDserial;

    public String setSerial(String p) {
        this.HDserial = p;
        return this.HDserial;

    }

    public String getSerial() {
        return this.HDserial;
    }

    Timer timer = new Timer();






    //INITIALIZE METODI SUORITETAAN HETI OHJELMAN KÄYNNISTYESSÄ TÄSSÄ TAPAUKSESSA
    //SE KUTSUU SHOWUSER METODIA, JOKA TOTEUTETAAN HETI.

    public void initialize() throws IOException {


        //dirinput tekstikentässä on "kuuntelija", joka kopioi
        //kenttää kirjoitetun tekstin findBtn painikkeeseen.

        dirInput.textProperty().addListener((obs, oldText, newText) -> {
            String userPath = dirInput.getText();
            // do what you need with newText here, e.g.
            findBtn.setText("searh from "+userPath);
        });


        //openApp on OpenApps luokan olio ja tässä olion avulla käytetäänn
        //openapps luokan metodia.
        openNote.setOnAction(e -> {
            openApp.OpenNote();
        });
        openChrome.setOnAction((e -> {
            openApp.OpenChrome();
        }));
        openPaint.setOnAction((e -> {
            openApp.OpenPaint();
        }));

        showUser();
        firewStatus();
       // getBootupTime();


    }



    public void textAnimation() {

        titleLbl.setStyle("-fx-text-fill:red;");
        titleLbl.setFont(Font.font("System", 20));

    }

    @FXML
    protected void showOs() throws IOException {

        // käyttöjärjestelmän nimi ja version asetetaan txtBox id-nimellä
        //nimettyyn kenttään
        txtBox.setText("Operating System: " + System.getProperty("os.name") + "\n" + "version: " + System.getProperty("os.version") + " Architecture: " + System.getProperty("os.arch") + "\n"
                + "Windows " + System.getProperty("sun.arch.data.model") + " bit");

    }

    @FXML
    protected void showJava() {
        //pidTxt ja closebtnia käytetään vain running apps metodin yhteydessä, joten ne tehdään
        //aina näkymättöksi.
        pidTxt.setVisible(false);
        closeBtn.setVisible(false);


        java.lang.String javav = System.getProperty("java.version");

        //Installation directory for Java Runtime Environment (JRE)
        java.lang.String direc = System.getProperty("java.home");
        java.lang.String userName = System.getProperty("user.name");

        txtBox.setText("Java version: " + javav + " installed in: " + direc + "\n");
        user.setText("User: " + userName);


    }
    //tarkistetaan palomuurin tila on/off
    public void firewStatus() throws IOException {
        Process status = Runtime.getRuntime().exec("netsh advfirewall show currentprofile");
        status.getOutputStream().close();
        BufferedReader fwstatus = new BufferedReader(new InputStreamReader(status.getInputStream()));
        String line;
        line = fwstatus.readLine();



        while ((line = fwstatus.readLine()) != null) {
            System.out.println(line);
            if (line.contains("ON"))
            {
                setFwstatus();

            }


        }

    }


    public void getBootupTime() throws IOException {
        Process p = Runtime.getRuntime().exec("wmic os get lastbootuptime");
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        line = r.readLine();

        while ((line = r.readLine()) != null) {
            System.out.println(line);

        }


    }

    public void setFwstatus() {
        fwstatus.setText("Firewall OK");

    }
    @FXML
    protected void showMemory() {
        timeToStr = timenow.format(timenowFormatted);

        pidTxt.setVisible(false);
        //closeBtn.setVisible(false);

        double memorySize = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize() / (1024.0 * 1024 * 1024);
        //pyöristys 2 desimaalin tarkkuuteen.
        double roundedTotalMemory = Math.floor(memorySize * 100) / 100.0;
        double freeMemory = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize() / (1024.0 * 1024 * 1024);
        double roundedFree = Math.floor(freeMemory * 100) / 100.0;
        double usedMemory = roundedTotalMemory - roundedFree;
        txtBox.setText(timeToStr+"\n"+ "Total memory: " + roundedTotalMemory + " GB" + "\nFree: " + roundedFree + " GB" + "\n"
                + "memory in use: " + usedMemory + " GB");
    }


    @FXML
    //system.getenv saa parametrina ympäristömuuttujan
    public void processor() throws IOException {
        pidTxt.setVisible(false);
        //closeBtn.setVisible(false);

        double cpuLoad = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad();

        //prosessorin id
        java.lang.Process process = Runtime.getRuntime().exec("wmic cpu get ProcessorId");

        process.getOutputStream().close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            if (!line.trim().equals("")) {
                //setteri tallettaan prosessorin id arvon
                //getteri näyttää sen textboksissa.
                n.setCpuID(line);


            }
        }
        txtBox.setText("Processor: " + System.getenv("PROCESSOR_IDENTIFIER") + "\n" + "Architechture: " +
                System.getenv("PROCESSOR_ARCHITECTURE") + "\n" +
                "Number of processors:" +
                System.getenv("NUMBER_OF_PROCESSORS") + "\n" + "Processor ID: " + n.getCpuID()
                + "\n" + "CPU load: " + Math.round(cpuLoad * 100.0) / 100.0 + " %");


    }

    public void showUser() {
        java.lang.String userName = System.getProperty("user.name");
        user.setText("Logged user: " + userName);

    }

    public void filesystem() throws IOException {
        Process p = Runtime.getRuntime().exec("cmd.exe /c vol c: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            setSerial(line.replace("Volume Serial Number is", ""));

        }
        reader.close();
        java.lang.String curDir = System.getProperty("user.dir");
        File[] roots = File.listRoots();



        /* For each filesystem root, print some info */
        for (File root : roots) {


            txtBox.setText("Hard drive serial number: " + getSerial() + "\nFile system root: " + root.getAbsolutePath() + "\n " + "Total Disk space (Gb): " + root.getTotalSpace() / (1024 * 1024 * 1024) + "\n" +
                    "" + "free disk space (GB): " + root.getUsableSpace() / (1024 * 1024 * 1024) + "\n"
                    + "current directory: " + curDir + " " + System.getenv("PROGRAM_FILES"));


        }


    }

    //metodi avaa uuden ikkunan, eli graphics.fxml

    public void openGraph(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graphics.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Graphics");

        stage.setScene(new Scene(root1));
        stage.show();
    }



    public void showRunningPids() throws IOException, InterruptedException {

        List<String> runningPids = new ArrayList<String>();
        Process pidsOnly = new ProcessBuilder("powershell", "\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders  ID").start();

        new Thread(() -> {

            int i = 0;
            Scanner sc = new Scanner(pidsOnly.getInputStream());
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {


                String line = sc.nextLine();
                //poistetaan välilyönnit
                String res = line.replaceAll(" ", "");
                System.out.println(res);

                //lisätään löydetyt prosessit listalle ja lisätään rivinvainto jokaisen
                //alkion jälkeen
                runningPids.add(res + "\n");
                //process id numeroiden siirto menuitemiin, i kasvaa aina yhdellä
                //jolloin seuraava listalla oleva pid lisätään menuitemiin.
                MenuItem m1 = new MenuItem(runningPids.get(i));
                i = i + 1;
                //prosessi id:n lisäys menuitemiin.
                processID.getItems().add((m1));
                EventHandler<ActionEvent> pidEvent = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        //valittu menuitem talletetaan idvaluen muuttujaan
                        String idvalue = ((MenuItem)e.getSource()).getText();
                        //asetetaan valittu pid tekstikenttään.
                        pidTxt.setText(idvalue);
                        closeProcess();
                       // showPidValue(idvalue);
                    }
                };
                //menuitemeille lisätään tapahtumankäsittelijät.
                m1.setOnAction(pidEvent);


            }


            System.out.println(runningPids);


        }).start();
        pidsOnly.waitFor();

    }


    //käynnissä olevien sovellusten haku ja näyttö
    public void showRunningApps(ActionEvent actionEvent) throws IOException, InterruptedException {
        showRunningPids();
        //luodaan merkkijonolista

        List<String> runningApps = new ArrayList<String>();

        //lisäämällä ID namen jälkeen saa process id:n

        java.lang.Process process = new ProcessBuilder("powershell", "\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders  name,ID").start();
        new Thread(() -> {
            Scanner sc = new Scanner(process.getInputStream());
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                //poistetaan välilyönnit
                String res = line.replaceAll(" ", "");
                System.out.println(res);

                //lisätään löydetyt prosessit listalle ja lisätään rivinvainto jokaisen
                //alkion jälkeen
                runningApps.add(res + "\n");
            }
            txtBox.setText(String.valueOf(runningApps));

            //metodin suorituksen myötä tekstikenttä tulee näkyviin
            pidTxt.setVisible(true);

            //urlField.setVisible(false);

        }).start();
        process.waitFor();
    }



    public void closeProcess() {
        java.lang.String pid = pidTxt.getText();
        int pidInt = Integer.parseInt(pid);

        try {
            //prosessin sulku process id:n perusteella.
            ProcessHandle.of(pidInt).ifPresent(ProcessHandle::destroy);

        } catch (Exception e) {
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
        if (fileName != null) {
            //metodi avaa valitun tiedoston sopivalla ohjelmalla
            openFile.OpenSelectedFile(fileName);
        }
    }



    public void NetWork(ActionEvent actionEvent) throws IOException, InterruptedException {

        net.ShowNetworkInfo(pidTxt,pingUrl,txtBox,processID);



    }

    public void checkUrl(ActionEvent actionEvent) throws IOException, InterruptedException {
        net.DoPing(txtBox,pingUrl,pidTxt,wView);



    }


    public void winReg(ActionEvent actionEvent) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd reg query /?");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println(reader);
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            txtBox.appendText(line + "\n");
        }
        reader.close();


    }

    //haetaan kansio kovalelyltä ja tulostetaan sisältö txtboksissa.
    public void excecuteDIr(ActionEvent actionEvent) throws IOException, InterruptedException {
        String path = dirInput.getText();
        dir.DoStandardDir(path, txtBox, dirInput, hiddenFiles, hiddenLbl);

    }


    public void txtFileSrc(ActionEvent actionEvent) throws IOException, InterruptedException {
        String path = dirInput.getText();
        dir.TxtFileSrc(path, txtBox, dirInput);

    }


    public void srcOwnExt(ActionEvent actionEvent) {


    }


    //metodin suoritus tab näppäimen painalluksella ja käyttäjän syöttämällä tiedostopäätteellä.
    public void srcOwnKeyPressed(KeyEvent keyEvent) {

        extField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.TAB) {
                String ext = extField.getText();
                String path = dirInput.getText();
                System.out.println("TAB pressed");
                String cmdArr[] = {"cmd", "/c", "dir", path};
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
                    if (line.contains(ext)) {
                        //NÄYTETÄÄN VAIN TXT PÄÄTTEISET TIEDOSTOT
                        txtBox.appendText(line + "\n");
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

    //Grip panen piilotus ja UItip labelin näyttö
    public void hideGP(ActionEvent actionEvent) {
        functionsGP.setOpacity(0.0);
        UItip.setOpacity(1.0);
    }

    public void SubDir(ActionEvent actionEvent) {
        String path = dirInput.getText();
        //alikansioiden haku isDirectoryn avulla ja tallennus listaan
        dir.SubDirSrc(path, txtBox, dirInput);

    }


    public void BareDir(ActionEvent actionEvent) throws IOException {
        String path = dirInput.getText();
        dir.DoBareFormatDir(path, txtBox, dirInput);
    }

    public void sizeDir(ActionEvent actionEvent) throws IOException {

        String path = dirInput.getText();
        dir.DoDirBySize(path, txtBox, dirInput);
    }


    public void showUi(KeyEvent keyEvent) {
    //painamalla näppäintä s ui näytetään.
        mainV.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S) {
                //set opacity toimii true/falsea paremmin uin piilottamisessa ja uudelleen näyttämisessä
                functionsGP.setOpacity(1.0);
                UItip.setOpacity(0.0);

            }
        });
    }

    public void increaseFont(ActionEvent actionEvent) {

        c.DoFontIncrease(txtBox);


    }


    public void decreaceFont(ActionEvent actionEvent) {
     c.DoFontDecrease(txtBox);

    }

    public void colChoose(ActionEvent actionEvent) {
        c.ColorPick(txtBox);
    }

    public void changeBlue(ActionEvent actionEvent) {
        c.DoChangeToBlue(txtBox);


    }

    public void hiddenFileSearch(ActionEvent actionEvent) throws IOException {
        String path = dirInput.getText();
        dir.DoHiddenFileSearch(path,txtBox);
    }

    public void BoldText(ActionEvent actionEvent) {
        c.DoBolding(txtBox);
    }



    public void showWview(ActionEvent actionEvent) {
        net.DoWebView(pidTxt);
    }

    public void findFile(ActionEvent actionEvent) throws IOException {
        String path = dirInput.getText();
        dir.DoSearch(dirInput,txtBox,path);
    }

    public void UiToFIn(ActionEvent actionEvent) {
        c.LngToFin(graphBtn,dirBtn,select,hideBtn,advDir,findBtn);
    }

    public void wlanReport(ActionEvent actionEvent) throws URISyntaxException, IOException, InterruptedException {
        net.DoWlanReport(txtBox);

    }

    public void ListSubDirs(ActionEvent actionEvent) throws IOException {
        dir.ClickNdrop(dirInput);

    }


    public void listEmptyDirs(ActionEvent actionEvent) throws IOException {
        String path = dirInput.getText();
        dir.DoListOfEmptydirs(path,txtBox);
    }

    public void showLogin(ActionEvent actionEvent) throws IOException {
        df.LoginForm();
    }


    public void showPrinters(ActionEvent actionEvent) throws IOException {
        Process p = Runtime.getRuntime().exec("wmic printer list brief");
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        line = r.readLine();

        while ((line = r.readLine()) != null) {
            txtBox.appendText(line.trim()+"\n");

        }

    }

    public void cursiveFont(ActionEvent actionEvent) {
        c.DoCursive(txtBox);
    }
}