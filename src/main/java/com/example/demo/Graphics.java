package com.example.demo;

import com.sun.management.OperatingSystemMXBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Graphics {

    @FXML
    public TextFlow DBvalues;

    @FXML


    public LocalDateTime timenow = LocalDateTime.now();
    public DateTimeFormatter timenowFormatted = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    Date sqldate = new Date(Calendar.getInstance().getTime().getTime());
    public String timeStr = timenow.format(timenowFormatted);

    public long free;
    public  long total;

    public long used;

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

    public String HardDrives;

    public String setHardDrives(String newHD) {
        this.HardDrives=newHD;
        return this.HardDrives;
    }

    public String getHardDrives() {
        return HardDrives;
    }

    public void DoMemoryPie() {
        //setter metodilla talletetaan arvot
        setTotal(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize());
        setFree(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize());
        //setteriä voi käyttää myös näin, eli setUsed saa arvoksi getTotal ja getfreen erotuksen.
         setUsed(getTotal()-getFree());
         //talletetaan muuttujaan getterin tulos muutettuna gigatavuiksi
         double freesize = getFree() / (1024.0 * 1024 * 1024);
         double totalsize = getTotal() / (1024.0*1024*1024);
         double usedSize = getUsed() / (1024.0*1024*1024);
         //muunnetaan merkkijonoksi
         String freeTxt = String.valueOf(freesize);
         //tuloksen pyöristys 2 desimaaliin
         freeTxt= String.format("%.2f",freesize);
         String totalTxt = String.valueOf(totalsize);
         totalTxt = String.format("%.2f",totalsize);
         String usedTxt = String.format("%.2f",usedSize);


        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                //getfree ja gettotal metodeilla voidaan asettaa arvot piecharttiin
                new PieChart.Data(STR."Free \{freeTxt}",getFree()),
                new PieChart.Data(STR."Total \{totalTxt}",getTotal()),
                new PieChart.Data(STR."Used \{usedTxt}",getUsed())
        );
        PieChart pieChart = new PieChart(pieChartData);
        Group root = new Group(pieChart);
        Scene scene = new Scene(root,600,400);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
        Button saveBtn = new Button();
        saveBtn.setText("Save as png");
        Button savesqlBtn = new Button();
        savesqlBtn.setText("Save to SQL");
        savesqlBtn.setLayoutX(20);
        savesqlBtn.setLayoutY(40);
        Label timeLbl = new Label();
        timeLbl.setText(timeStr);
        timeLbl.setLayoutX(200);
        timeLbl.setLayoutY(95);
        //lisätään komponentit root-komponenttiin
        root.getChildren().addAll(saveBtn,savesqlBtn);
        root.getChildren().add(timeLbl);
        savesqlBtn.setOnAction(e->{
            try {
                memValToDB(freesize,usedSize,totalsize);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });


        //dynaamisesti luodussa ikkunnassa buttonille asetetaan
        //tapahtumakäsittelijä näin.
        saveBtn.setOnAction(e->{
            try {
                //scenen lähetys parametrina DoSave metodille.
                DoSave(scene);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });




    }

    public void memValToDB(double freesize, double usedSize, double totalsize) throws SQLException {
        //double fz = DoMemoryPie();

        DBconnection conn = new DBconnection();
        Connection connDB = conn.getConnection();
        String DBquery = "INSERT INTO memoryvalues (dateval,free,used,total) VALUES (?,?,?,?)";
        PreparedStatement pst = connDB.prepareStatement(DBquery);
        pst.setDate(1,sqldate);

        pst.setDouble(2,freesize);
        pst.setDouble(3,usedSize);
        pst.setDouble(4,totalsize);


        int rs = pst.executeUpdate();
        System.out.println(rs);



    }

    public void getMemoryData() throws SQLException {
        DBconnection conn = new DBconnection();
        Connection connDB = conn.getConnection();
        Statement stmt=connDB.createStatement();
        ResultSet memoryData=stmt.executeQuery("select dateval,free,used,total FROM memoryvalues");
        Text row = new Text("hello");
        List<String>values = new ArrayList<>();
        while(memoryData.next())

            System.out.println("Date: "+memoryData.getString(1)+ " Free "+memoryData.getString(2)+" Used "+memoryData.getString(3)+" Total "+memoryData.getString(4));
        DBvalues.getChildren().add(row);
        connDB.close();




    }

    public void DoDiskSpacePie() {
        //tietokoneen kovalelyjen haku
        File[] roots2 = File.listRoots();
        for (int i = 0; i < roots2.length; i++)
            System.out.println("Root[" + i + "]:" + roots2[i]);
        String firstGuess = String.valueOf(roots2[0]);


        //setter metodin käyttö,
        setHardDrives(Arrays.toString(roots2));
        TextInputDialog td = new TextInputDialog(firstGuess);
        td.setHeaderText("Enter a HD, these hard drives were found " + getHardDrives());
        String userPath = td.getEditor().getText();
        System.out.println(userPath);

        //selvitetään kumpaa painiketta käyttäjä klikkaa.
        Optional<String> result = td.showAndWait();

        if (result.get().equals("Cancel")) {
            td.close();
        } else {

        try {
            File file = new File(userPath);
            long freeSize = file.getFreeSpace() / (1024 * 1024 * 1024);
            long totalSize = file.getTotalSpace() / (1024 * 1024 * 1024);
            long usedSize = file.getTotalSpace() / (1024 * 1024 * 1024) - file.getFreeSpace() / (1024 * 1024 * 1024);

            setFree(freeSize);
            setTotal(totalSize);
            setUsed(usedSize);
            String FreeSizeTxt = String.valueOf(freeSize);
            String TotalSizeTxt = String.valueOf(totalSize);
            String usedSizeTxt = String.valueOf(usedSize);
            //setter metodilla talletetaan arvot


            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    //getfree ja gettotal metodeilla voidaan asettaa arvot piecharttiin
                    new PieChart.Data("Free GB: " + FreeSizeTxt, getFree()),
                    new PieChart.Data("Total GB " + TotalSizeTxt, getTotal()),
                    new PieChart.Data("Used GB " + usedSizeTxt, getUsed())
            );
            PieChart pieChart = new PieChart(pieChartData);
            Label timeLbl = new Label();
            timeLbl.setText(timeStr);
            timeLbl.setLayoutX(200);
            timeLbl.setLayoutY(95);
            Button saveBtn = new Button();
            saveBtn.setText("Save");


            Group root = new Group(pieChart);
            //lisätään pvm label root grouppiin, eli näin
            //voidaan lisätä näkymään uusia komponentteja
            root.getChildren().add(timeLbl);
            root.getChildren().add(saveBtn);
            Scene scene = new Scene(root, 600, 400);
            // getterien avulla voidaan tallentaa kuva tässä metodissa
            // ja tehdä tallennus setterin avulla toisessa metodissa.
            setChartImg(scene);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            //dynaamisesti luodussa ikkunnassa buttonille asetetaan
            //tapahtumakäsittelijä näin.
            saveBtn.setOnAction(e -> {
                try {
                    DoSave(scene);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception e) {
            System.out.println("Unknown hard drive");

        }
    }

    }

    public void DoSave(Scene scene) throws IOException {
        //tiedostodialogi ja tallennusvalikko
        FileChooser fil_chooser = new FileChooser();
        File save = fil_chooser.showSaveDialog(scene.getWindow());

        // getterien avulla voidaan tallentaa kuva tässä metodissa
        // ja tehdä tallennus setterin avulla toisessa metodissa.
        setChartImg(scene);

        WritableImage image = GetChartImg().snapshot(null);
        //save parametri sisältää polun ja tiedostonimen joka tallennetaan
        File chartFile = new File(STR."\{save}.png");
        ImageIO.write(SwingFXUtils.fromFXImage(image,null),"PNG",chartFile);



    }
}
