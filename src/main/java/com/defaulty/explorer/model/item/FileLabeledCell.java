package com.defaulty.explorer.model.item;

import com.defaulty.explorer.control.rescontrol.files.FileOperationsImpl;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.HashMap;

/**
 * Класс как фабрика ячеек типов {@code TreeCell<File>} и {@code TableCell<TreeItem<File>}.
 * Добавляет некоторые дополнительные свойства ячейкам.
 */
public class FileLabeledCell {

    private TreeCell<File> treeCell;
    private TableCell<TreeItem<File>, File> tableCell;

    private final HashMap<File, FileLabeledCell> cellHashMap;

    private TextField textField;

    private boolean waitFlag;

    private Labeled labeled;

    private Runnable startEdit;

    public FileLabeledCell(TreeView<File> treeView, HashMap<File, FileLabeledCell> cellHashMap) {
        this.cellHashMap = cellHashMap;
        treeCell = new TreeCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                updateView(treeCell, item, empty);
            }
        };
    }

    public FileLabeledCell(TableView<TreeItem<File>> tableView, HashMap<File, FileLabeledCell> cellHashMap) {
        this.cellHashMap = cellHashMap;
        tableCell = new TableCell<TreeItem<File>, File>() {

            @Override
            public void startEdit() {
                super.startEdit();

                if (waitFlag) {
                    if (textField == null) {
                        createTextField();
                    }
                    setText(null);
                    setGraphic(textField);
                    textField.selectAll();
                } else
                    waitFlag = true;
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                updateView(tableCell, getItem(), false);
            }

            @Override
            public void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setText(null);
                        setGraphic(textField);
                    } else {
                        updateView(tableCell, item, false);
                    }
                }
            }

            private void createTextField() {
                textField = new TextField(getText());
                textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                textField.focusedProperty().addListener((arg0, arg1, arg2) -> {
                    if (!arg2) {
                        rename(textField.getText());
                        commitEdit(getItem());
                    }
                });

                textField.setOnKeyReleased(t -> {
                    if (t.getCode() == KeyCode.ENTER) {
                        String value = textField.getText();
                        if (value != null) {
                            rename(textField.getText());
                            commitEdit(getItem());
                        } else {
                            commitEdit(null);
                        }
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
            }

            private void rename(String newName) {
                File sourceFile = getItem();
                String path = sourceFile.getParentFile().getAbsolutePath();
                File newFile = new File(path + "\\" + newName);
                //TODO: убрать явное создание FileOperationsImpl
                new FileOperationsImpl().rename(sourceFile, newFile);
                setItem(newFile);
                updateView(tableCell, newFile, false);
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };
    }

    private void updateView(Labeled labeled, File file, boolean empty) {
        waitFlag = false;
        if (file == null || empty) {
            labeled.setText(null);
            labeled.setGraphic(null);
            labeled.setStyle("");
        } else {
            labeled.setText(file.getParentFile() == null ? file.getAbsolutePath() : file.getName());
            labeled.setGraphic(new ImageSetter().getImageView(file, ImageSizePack.ImageSize.SMALL));
            this.labeled = labeled;
            cellHashMap.put(file, this);
        }
    }

    public TreeCell<File> getTreeCell() {
        return treeCell;
    }

    public TableCell<TreeItem<File>, File> getTableCell() {
        return tableCell;
    }

    public Labeled getLabeled() {
        return labeled;
    }
}
