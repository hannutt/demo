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

    Graphics g = new Graphics();







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
        this.total =  newSpace;
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
      g.DoMemoryPie();
    }



    public void DiskSpace(ActionEvent actionEvent) {
     g.DoDiskSpacePie();

    }



}