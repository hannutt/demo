package com.example.demo;

import com.sun.management.OperatingSystemMXBean;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javafx.embed.swing.SwingFXUtils;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("System info");
        stage.setScene(scene);
        stage.show();
    }
    public LocalDateTime timenow = LocalDateTime.now();
    public DateTimeFormatter timenowFormatted = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String timeStr = timenow.format(timenowFormatted);
    public long free=0;
    public  long total=0;

    public long used;
    public Button memBtn;


    public Scene chartImg;

    public Scene GetChartImg()
    {
        return chartImg;
    }

    public Scene setChartImg(Scene newChart)
    {
        this.chartImg =newChart;
        return this.chartImg;
    }
    public String CpuID;

    public String setCpuID(String newCpuid)
    {
        this.CpuID = newCpuid;
        return this.CpuID;
    }

    public String getCpuID() {
        return this.CpuID;
    }







    //tämä on metodi joka palauttaa num1 arvon
    //jolloin sitä voi käyttää toisessa luokassa
    public long getFree() {
        return free;
    }

    public long getTotal() {
        return total;
    }

    public long getUsed () {
        return used;
    }

    public long setUsed (long newUsed)
    {
        this.used=newUsed;
        return this.used;
    }





    public long setFree(long newFree)
    {
        this.free=  newFree;
        return this.free;

    }

    public long setTotal(long newTotal)
    {
        this.total = newTotal;
        return this.total;
    }

    public long DIspace = 0;
    public long getTotalDisk() {
        return DIspace;

    }

    public long setDiskSpace(long newSpace) {
        this.total = (int) newSpace;
        return this.total;
    }

    public String HardDrives;

    public String setHardDrives(String newHD) {
        this.HardDrives=newHD;
        return this.HardDrives;
    }

    public String getHardDrives() {
        return HardDrives;
    }
    @FXML
    public Label user;








    public static void main(String[] args) {
        launch();
    }
    public void memPie(ActionEvent actionEvent) {
        //setter metodilla talletetaan arvot
        setTotal(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize());
        setFree(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize());
        //setteriä voi käyttää myös näin, eli setUsed saa arvoksi getTotal ja getfreen erotuksen.
        setUsed(getTotal()-getFree());



        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                //getfree ja gettotal metodeilla voidaan asettaa arvot piecharttiin
                new PieChart.Data("Free ",getFree()),
                new PieChart.Data("Total ",getTotal()),
                new PieChart.Data("Used ",getUsed())
        );
        PieChart pieChart = new PieChart(pieChartData);
        Group root = new Group(pieChart);
        Scene scene = new Scene(root,600,400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    //piechartin tallennus png muotoisena
    public void saveChart() throws IOException {
        WritableImage image = GetChartImg().snapshot(null);
        File file = new File("C:\\codes\\java\\chart.png");
        ImageIO.write(SwingFXUtils.fromFXImage(image,null),"PNG",file);
    }

    public void DiskSpace(ActionEvent actionEvent) {
        TextInputDialog td = new TextInputDialog();
        //tietokoneen kovalelyjen haku
        File[] roots2 = File.listRoots();
        for(int i = 0; i < roots2.length ; i++)
            System.out.println("Root["+i+"]:" + roots2[i]);
        //setter metodin käyttö,
        setHardDrives(Arrays.toString(roots2));
        td.setHeaderText("Enter a HD, these hard drives were found "+getHardDrives());
        td.showAndWait();
        java.lang.String userPath = td.getEditor().getText();
        System.out.println(userPath);
        try {
            File file = new File(userPath);
            long freeSize = file.getFreeSpace() / (1024 * 1024 * 1024);
            long totalSize = file.getTotalSpace() / (1024*1024*1024);
            setFree(freeSize);
            setTotal(totalSize);
            String FreeSizeTxt = String.valueOf(freeSize);
            String TotalSizeTxt = String.valueOf(totalSize);
            //setter metodilla talletetaan arvot



            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    //getfree ja gettotal metodeilla voidaan asettaa arvot piecharttiin
                    new PieChart.Data("Free GB: "+FreeSizeTxt,getFree()),
                    new PieChart.Data("Total GB "+TotalSizeTxt,getTotal())
            );
            PieChart pieChart = new PieChart(pieChartData);
            Label timeLbl = new Label();
            timeLbl.setText(timeStr);
            timeLbl.setLayoutX(200);
            timeLbl.setLayoutY(95);
            Button saveBtn = new Button();
            saveBtn.setText("Save");
            //dynaamisesti luodussa ikkunnassa buttonille asetetaan
            //tapahtumakäsittelijä näin.
            saveBtn.setOnAction(e->{
                try {
                    saveChart();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Group root = new Group(pieChart);
            //lisätään pvm label root grouppiin, eli näin
            //voidaan lisätä näkymään uusia komponentteja
            root.getChildren().add(timeLbl);
            root.getChildren().add(saveBtn);
            Scene scene = new Scene(root,600,400);
            // getterien avulla voidaan tallentaa kuva tässä metodissa
            // ja tehdä tallennus setterin avulla toisessa metodissa.
            setChartImg(scene);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Unknown hard drive");

        }

    }



}