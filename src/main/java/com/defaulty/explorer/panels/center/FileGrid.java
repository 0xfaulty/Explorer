package com.defaulty.explorer.panels.center;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.files.FileOperations;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import com.defaulty.explorer.model.cell.LabeledCell;
import com.defaulty.explorer.model.item.FilteredTreeItem;
import com.defaulty.explorer.model.tree.ModelOperations;
import com.defaulty.explorer.panels.FilePopupMenu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.HashMap;

/**
 * Панель представляющая отображение системы в виде сетки иконок с подписями.
 */
public class FileGrid extends ScrollPane implements ViewObserver {

    private HashMap<File, GridItem> itemHashMap = new HashMap<>();
    private final FilePopupMenu popup;
    private final ModelOperations modelOperations;

    private TilePane tilePane = new TilePane();

    private String style = "";

    public FileGrid(ViewConnector connector) {
        //connector.register(this);
        this.modelOperations = connector.getModelOperations();

        popup = new FilePopupMenu(connector);
        tilePane.setPadding(new Insets(10));
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(10);
        tilePane.setMaxWidth(Region.USE_PREF_SIZE);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(tilePane);

        setFitToWidth(true);
        setContent(hbox);
    }

    @Override
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case CHANGE_FORK:
                changeFork(event.getFork());
                break;
            case CHANGE_STATE:
                changeState(event.getFork());
                break;
            case SEARCH_TASK:
                tilePane.getChildren().clear();
                break;
            case ADD_NODE:
                addNode(event.getFork());
                break;
            case SET_THEME:
                setTheme(event.getThemeType());
                break;
        }
    }

    /**
     * Добавить новый элемент на панель.
     *
     * @param fork - новый элемент.
     */
    private void addNode(TreeItem<File> fork) {
        if (fork != null) {
            GridItem gridItem = new GridItem(fork.getValue(), style);
            tilePane.getChildren().add(gridItem);
        }
    }

    /**
     * Обновить состояние объекта
     *
     * @param fork - обновляемый объект.
     */
    private void changeState(TreeItem<File> fork) {
        GridItem gridItem = itemHashMap.get(fork.getValue());
        if (gridItem != null)
            gridItem.updateItem(fork.getValue());
    }

    /**
     * Сменить отображаемую ветку
     *
     * @param fork - новая ветка.
     */
    private void changeFork(TreeItem<File> fork) {
        tilePane.getChildren().clear();
        for (TreeItem<File> fItem : fork.getChildren()) {
            GridItem gridItem = new GridItem(fItem.getValue(), style);
            itemHashMap.put(fItem.getValue(), gridItem);
            tilePane.getChildren().add(gridItem);
        }
        if (fork instanceof FilteredTreeItem) {
            FilteredTreeItem ftItem = (FilteredTreeItem) fork;
            for (TreeItem<File> fItem : ftItem.getFileChildren()) {
                GridItem gridItem = new GridItem(fItem.getValue(), style);
                itemHashMap.put(fItem.getValue(), gridItem);
                tilePane.getChildren().add(gridItem);
            }
        }
        changeState(fork);
    }

    /**
     * Сметить оформление панели.
     *
     * @param t тип офорления.
     */
    private void setTheme(ThemeType t) {
        switch (t) {
            case DARK:
                style = "-fx-background-color: #262626; -fx-text-fill: #0ed9e0;";
                tilePane.setStyle(style);
                break;
            case LIGHT:
                style = "-fx-background-color: #f4f5f7; -fx-text-fill: black ;";
                tilePane.setStyle(style);
                break;
        }
        for (GridItem item : itemHashMap.values()) {
            item.setItemStyle(style);
        }
    }

    /**
     * Единичный элемент сетки. Содержит изображение и подпись.
     */
    private class GridItem extends VBox implements LabeledCell {

        private File file;

        private Label icon = new Label();
        private Label label = new Label();

        private TextField textField = new TextField();

        private FileOperations fo;

        GridItem(File file, String style) {
            updateItem(file);
            setItemStyle(style);

            getChildren().addAll(icon, label);

            textField.setOnKeyReleased(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (fo != null && !textField.getText().equals("")) {
                        String path = file.getParentFile().getAbsolutePath();
                        File newFile = new File(path + "\\" + textField.getText());
                        if (fo.rename(file, newFile)) updateItem(newFile);
                    }
                    getChildren().remove(textField);
                    getChildren().add(label);
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    getChildren().remove(textField);
                    getChildren().add(label);
                }
            });

            setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (file.isDirectory()) modelOperations.loadFork(file);
                }
                if (mouseEvent.getClickCount() == 1) {
                    //setStyle("-fx-background-color: #9dc6e0");
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        popup.show(this, file, this, mouseEvent);
                    } else
                        popup.hide();
                }
            });

            setAlignment(Pos.CENTER);

            setOnMouseEntered(e -> setStyle("-fx-background-color: #9dc6e0"));
            setOnMouseExited(e -> setStyle("-fx-background-color: #f4f5f7"));

        }

        /**
         * Обновить данные в ячейке.
         *
         * @param file - элемент данных ячейки.
         */
        void updateItem(File file) {
            this.file = file;
            String name = formatName(file.getName());
            this.label.setText(name);
            this.textField.setText(name);
            ImageView view = new ImageSetter().getImageView(file, ImageSizePack.ImageSize.BIG);
            icon.setGraphic(view);
        }

        /**
         * Сокращение длинных имён файлов.
         *
         * @param s - имя файла.
         * @return - форматированное при превышенной
         * длинне имя файла.
         */
        private String formatName(String s) {
            if (s.length() > 25)
                return s.substring(0, 22) + "..";
            else
                return s;
        }

        /**
         * Добавляет новые параметры стиля необходимым подэлементам.
         *
         * @param s - строка параметров.
         */
        public void setItemStyle(String s) {
            label.setStyle(s);
            //textField.setStyle(s);
        }

        @Override
        public void startEditCell(FileOperations fo) {
            this.fo = fo;
            textField.setText(file.getName());
            getChildren().remove(label);
            getChildren().add(textField);
            textField.selectAll();
        }

    }
}
