package com.defaulty.explorer.panels;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.BreadCrumbBar;

public class TopToolBar extends BorderPane {

    public TopToolBar() {
        Button btnBack = new Button();
        btnBack.setText("<");
        btnBack.setOnAction(event -> System.out.println("back"));

        Button btnForward = new Button();
        btnForward.setText(">");
        btnForward.setOnAction(event -> System.out.println("forward"));

        BreadCrumbBar<String> crumbBar = new BreadCrumbBar<>();
        TreeItem<String> model = BreadCrumbBar.buildTreeModel("Hello", "World", "This", "is", "cool");
        crumbBar.setSelectedCrumb(model);

        crumbBar.setOnCrumbAction(bae -> System.out.println(bae.getSelectedCrumb()));

        ToolBar toolBarLeft = new ToolBar();
        toolBarLeft.getItems().add(btnBack);
        toolBarLeft.getItems().add(btnForward);

        ToolBar toolBarCenter = new ToolBar();
        toolBarCenter.getItems().add(crumbBar);

        TextField searchField = new TextField("поиск..");
        ToolBar toolBarRight = new ToolBar();
        toolBarRight.getItems().add(searchField);

        setLeft(toolBarLeft);
        setCenter(toolBarCenter);
        setRight(toolBarRight);
    }
}
