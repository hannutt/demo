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
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.embed.swing.SwingFXUtils;

import java.sql.SQLException;
import java.util.List;

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

    @FXML
    public CheckBox DSval,DBmem,rmDate;
    @FXML
    public ListView DBvalues;

    public TextField CompareInt;
    @FXML
    public Text row;
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



    public void readDB(ActionEvent actionEvent) {
        if (DBmem.isSelected())
        {
            try {

                DBconnection conn = new DBconnection();
                Connection connDB = conn.getConnection();
                Statement stmt=connDB.createStatement();
                ResultSet memoryData=stmt.executeQuery("select dateval,free,used,total FROM memoryvalues");


                while(memoryData.next())
                    DBvalues.getItems().add(memoryData.getString(1)+" "+memoryData.getString(2)+" "+memoryData.getString(3) +" "+memoryData.getString(4));
                //row.setText("Date: "+memoryData.getString(1)+ " Free "+memoryData.getString(2)+" Used "+memoryData.getString(3)+" Total "+memoryData.getString(4));
                connDB.close();


            }catch (SQLException e) {
                System.out.println(e);


            }

        }
        else {
            DBvalues.getItems().clear();
        }

    }

    //listviewin rivin saa valittua klikkaamalla sitä.
    public void lvClick(MouseEvent mouseEvent) {
        CompareInt.setText(DBvalues.getSelectionModel().getSelectedItems().toString());
        System.out.println(DBvalues.getSelectionModel().getSelectedItems());

    }

    public void FreeMemOnly(ActionEvent actionEvent) {
        try {
            DBconnection conn = new DBconnection();
            Connection connDB = conn.getConnection();
            Statement stmt=connDB.createStatement();
            ResultSet memoryData=stmt.executeQuery("select free FROM memoryvalues");

            //LÄPIKÄYNTI silmukassa
            while(memoryData.next())
                //tietojen lisäys listviewiin (dbvalues)
                DBvalues.getItems().add(memoryData.getString(1));
            //row.setText("Date: "+memoryData.getString(1)+ " Free "+memoryData.getString(2)+" Used "+memoryData.getString(3)+" Total "+memoryData.getString(4));
            connDB.close();


        }catch (SQLException e) {
            System.out.println(e);

        }

    }

    public void readDiskSpaceDB(ActionEvent actionEvent) {
        //jos checboksi on valittu näytetään tietokannan arvot
        //muuten tyhjennetään listview
        if (DSval.isSelected())
        {
            try {

                DBconnection conn = new DBconnection();
                Connection connDB = conn.getConnection();
                Statement stmt=connDB.createStatement();
                ResultSet memoryData=stmt.executeQuery("select dateval, free,used,total FROM hdvalues");


                while(memoryData.next())
                    DBvalues.getItems().add(memoryData.getString(1)+" "+memoryData.getString(2)+" "+memoryData.getString(3) +" "+memoryData.getString(4));
                //row.setText("Date: "+memoryData.getString(1)+ " Free "+memoryData.getString(2)+" Used "+memoryData.getString(3)+" Total "+memoryData.getString(4));
                connDB.close();


            }catch (SQLException e) {
                System.out.println(e);

            }


        }
        else {
            DBvalues.getItems().clear();
        }

    }

    public void removeDate(ActionEvent actionEvent) {
        //jos nämä checkboxit on valittu, poistetaan päivämäärät listviewsta.
        if (DSval.isSelected() && rmDate.isSelected())
        {
           DBvalues.getItems().clear();
            try {

                DBconnection conn = new DBconnection();
                Connection connDB = conn.getConnection();
                Statement stmt=connDB.createStatement();
                ResultSet memoryData=stmt.executeQuery("select free,used,total FROM hdvalues");


                while(memoryData.next())
                    DBvalues.getItems().add(memoryData.getString(1)+" "+memoryData.getString(2)+" "+memoryData.getString(3));
                //row.setText("Date: "+memoryData.getString(1)+ " Free "+memoryData.getString(2)+" Used "+memoryData.getString(3)+" Total "+memoryData.getString(4));
                connDB.close();


            }catch (SQLException e) {
                System.out.println(e);

            }

        }


        }
    }
