package com.example.demo;

import com.sun.management.OperatingSystemMXBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Graphics {

    public LocalDateTime timenow = LocalDateTime.now();
    public DateTimeFormatter timenowFormatted = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
         //muunnetaan merkkijonoksi
         String freeTxt = String.valueOf(freesize);
         //tuloksen pyöristys 2 desimaaliin
         freeTxt= String.format("%.2f",freesize);
         String totalTxt = String.valueOf(totalsize);
         totalTxt = String.format("%.2f",totalsize);


        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                //getfree ja gettotal metodeilla voidaan asettaa arvot piecharttiin
                new PieChart.Data(STR."Free \{freeTxt}",getFree()),
                new PieChart.Data(STR."Total \{totalTxt}",getTotal())
                //new PieChart.Data("Used ",getUsed())
        );
        PieChart pieChart = new PieChart(pieChartData);
        Group root = new Group(pieChart);
        Scene scene = new Scene(root,600,400);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
        Button saveBtn = new Button();
        saveBtn.setText("Save");
        Label timeLbl = new Label();
        timeLbl.setText(timeStr);
        timeLbl.setLayoutX(200);
        timeLbl.setLayoutY(95);
        //lisätään komponentit root-komponenttiin
        root.getChildren().add(saveBtn);
        root.getChildren().add(timeLbl);


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

    public void DoDiskSpacePie() {
        //tietokoneen kovalelyjen haku
        File[] roots2 = File.listRoots();
        for(int i = 0; i < roots2.length ; i++)
            System.out.println("Root["+i+"]:" + roots2[i]);
        //setter metodin käyttö,
        setHardDrives(Arrays.toString(roots2));
        TextInputDialog td = new TextInputDialog();
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
            //dynaamisesti luodussa ikkunnassa buttonille asetetaan
            //tapahtumakäsittelijä näin.
            saveBtn.setOnAction(e->{
                try {
                    DoSave(scene);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
        catch (Exception e) {
            System.out.println("Unknown hard drive");

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
