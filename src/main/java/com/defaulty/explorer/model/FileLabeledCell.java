package com.defaulty.explorer.model;

import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import javafx.scene.control.*;

import java.io.File;
import java.util.HashMap;

public class FileLabeledCell {

    private TreeCell<File> treeCell;
    private TableCell<TreeItem<File>, File> tableCell;

    private final HashMap<File, Labeled> cellHashMap;

    public FileLabeledCell(TreeView<File> treeView, HashMap<File, Labeled> cellHashMap) {
        this.cellHashMap = cellHashMap;
        treeCell = new TreeCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                updateView(treeCell, item, empty);
            }
        };
    }

    public FileLabeledCell(TableView<TreeItem<File>> tableView, HashMap<File, Labeled> cellHashMap) {
        this.cellHashMap = cellHashMap;
        tableCell = new TableCell<TreeItem<File>, File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                updateView(tableCell, item, empty);
            }
        };
    }

    private void updateView(Labeled labeled, File file, boolean empty) {
        if (file == null || empty) {
            labeled.setText(null);
            labeled.setGraphic(null);
            labeled.setStyle("");
        } else {
            labeled.setText(file.getParentFile() == null ? file.getAbsolutePath() : file.getName());
            labeled.setGraphic(new ImageSetter().getImageView(file, ImageSizePack.ImageSize.SMALL));
            cellHashMap.put(file, labeled);
        }
    }

    public TreeCell<File> getTreeCell() {
        return treeCell;
    }

    public TableCell<TreeItem<File>, File> getTableCell() {
        return tableCell;
    }

}
