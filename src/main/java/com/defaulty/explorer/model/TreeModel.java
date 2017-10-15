package com.defaulty.explorer.model;

import com.defaulty.explorer.control.observer.ViewConnectorModel;
import javafx.scene.control.TreeItem;

import java.io.File;

public interface TreeModel {

    void loadFork(TreeItem<File> fork, boolean checkout);

    void setViewConnector(ViewConnectorModel viewConnector);

    void treeSearch(TreeItem<File> item, String s);
}
