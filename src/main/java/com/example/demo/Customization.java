package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Button;

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

    public void ColorPick(TextArea txtBox) {
        Stage stage = new Stage();

        Button greenBtn = new Button("green");
        Button redBtn = new Button("red");
        greenBtn.setOnAction(e->{
            String colorValue=greenBtn.getText();
            String style = STR."-fx-text-fill:\{colorValue};";
            txtBox.setStyle(style);
        });
        redBtn.setOnAction(e->{
            String colorValue=redBtn.getText();
            String style = STR."-fx-text-fill:\{colorValue};";
            txtBox.setStyle(style);
        });

        VBox vb = new VBox();
        vb.getChildren().addAll(greenBtn,redBtn);
        Scene scene = new Scene(vb, 300, 300);

        // set the scene
        stage.setScene(scene);

        stage.show();


    }

    public void DoChangeBg(TextArea txtBox, String colorValue) {
        System.out.println(colorValue);
        String style = "-fx-text-fill:green;";
        txtBox.setStyle(style);

    }

    public void DoChangeToBlue(javafx.scene.control.TextArea txtBox) {
        String style = "-fx-text-background:blue;";
        txtBox.setStyle(style);
        //String style = "-fx-text-fill:green;";
    }

    public void DoBolding(javafx.scene.control.TextArea txtBox) {
        Font currentFont = Font.font((Font.font(txtBox.getFont().getSize())).getStyle());
       // currentfontin ansioista fontti ja koko oysyvät samana, ainoastaan fontweight vaihtuu
        txtBox.setFont(Font.font(String.valueOf(currentFont), FontWeight.BOLD, currentFont.getSize()));

    }

}
