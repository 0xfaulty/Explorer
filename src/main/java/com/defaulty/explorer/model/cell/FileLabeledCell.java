package com.defaulty.explorer.model.cell;

import com.defaulty.explorer.control.rescontrol.files.FileOperations;
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
public class FileLabeledCell implements LabeledCell {

    private TreeCell<File> treeCell;
    private TableCell<TreeItem<File>, File> tableCell;

    private final HashMap<File, FileLabeledCell> cellHash;

    private TextField textField;

    private Labeled labeled;

    private FileOperations fo = null;

    public FileLabeledCell(TreeView<File> treeView, HashMap<File, FileLabeledCell> cellHash) {
        this.cellHash = cellHash;
        treeCell = new TreeCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                updateView(treeCell, item, empty);
            }
        };
    }

    public FileLabeledCell(TableView<TreeItem<File>> tableView, HashMap<File, FileLabeledCell> cellHash) {
        this.cellHash = cellHash;
        tableCell = new TableCell<TreeItem<File>, File>() {

            @Override
            public void startEdit() {
                super.startEdit();

                if (textField == null) createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
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

            /**
             * Создать текстовое поле для редактирования имени.
             */
            private void createTextField() {
                textField = new TextField(getText());
                textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                textField.focusedProperty().addListener((arg0, arg1, arg2) -> {
                    if (!arg2) {
                        rename(textField.getText(), getItem(), this);
                    }
                });

                textField.setOnKeyReleased(t -> {
                    if (t.getCode() == KeyCode.ENTER) {
                        String value = textField.getText();
                        if (value != null) {
                            rename(textField.getText(), getItem(), this);
                        }
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
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
            this.labeled = labeled;
            cellHash.put(file, this);
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

    /**
     * Переименовать объект в ячейке.
     * @param newName - новое имя.
     * @param sourceFile - переименуемый объект.
     * @param cell - ячейка.
     */
    private void rename(String newName, File sourceFile, TableCell<TreeItem<File>, File> cell) {
        if (fo != null) {
            String path = sourceFile.getParentFile().getPath();
            File newFile = new File(path + "\\" + newName);
            if (!sourceFile.getName().equals(newName)) {
                if (sourceFile.exists() && !newFile.exists()) {
                    if (fo.rename(sourceFile, newFile)) {
                        cell.setItem(newFile);
                        updateView(tableCell, newFile, false);
                    }
                }
            } else
                cell.cancelEdit();
        }
    }

    @Override
    public void startEditCell(FileOperations fo) {
        this.fo = fo;
        tableCell.startEdit();
    }
}
