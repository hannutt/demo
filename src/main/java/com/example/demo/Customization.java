package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;

public class Customization {

    @FXML
    public TextArea txtBox;

    public void DoFontIncrease(javafx.scene.control.TextArea txtBox)
    {

        //selvitetään fontin koko txtboxissa
        double fontSize = (javafx.scene.text.Font.font(txtBox.getFont().getSize())).getSize();
        //kasvatetaan kokoa
        fontSize = fontSize +1.0;
        //näytetään teksi boksissa kasvatetulla fontin koolla.
        txtBox.setFont(javafx.scene.text.Font.font(fontSize));
    }

    public void DoFontDecrease(javafx.scene.control.TextArea txtBox) {
        //selvitetään fontin koko txtboxissa
        double fontSize = (javafx.scene.text.Font.font(txtBox.getFont().getSize())).getSize();
        //kasvatetaan kokoa
        fontSize = fontSize - 1.0;
        //näytetään teksi boksissa kasvatetulla fontin koolla.
        txtBox.setFont(Font.font(fontSize));
    }

    public void ColorPick() {
        //vbox on ns. pohjakomponentti, jonka päälle voidaan lisätä muita komponentteja.
       //Button blueBtn = new Button("blue");
        VBox vb = new VBox();


        //luodaan oma ikkuna color pickerille.
        Group root = new Group(vb);

        vb.getChildren().add(new javafx.scene.control.Button("blue"));


        Scene scene = new Scene(root,300,300);
        Stage stage = new Stage();
        stage.setTitle("Color Picker");
        stage.setScene(scene);
        stage.show();


    }

    public void DoChangeToBlue(javafx.scene.control.TextArea txtBox) {
        String style = "-fx-control-inner-background:blue;";
        txtBox.setStyle(style);
    }

}
