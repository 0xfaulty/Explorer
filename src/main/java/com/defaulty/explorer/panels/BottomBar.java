package com.defaulty.explorer.panels;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class BottomBar extends BorderPane{

    private Label label = new Label();

    public BottomBar() {
        this.setCenter(label);
    }
}
