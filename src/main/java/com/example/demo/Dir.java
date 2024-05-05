package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Dir {




    public  String PathName;
    public String setPath(String p)
    {
        this.PathName = p;
        return this.PathName;

    }

    public String getPath() {
        return this.PathName;
    }
    @FXML
    public TextField dirInput;

    @FXML
    public TextArea txtBox;





}
