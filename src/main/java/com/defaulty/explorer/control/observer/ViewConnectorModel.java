package com.defaulty.explorer.control.observer;

import javafx.scene.control.TreeItem;

import java.io.File;

public interface ViewConnectorModel extends ViewConnector {

    void changeFork(TreeItem<File> fork);

    void changeState(TreeItem<File> fork);

}
