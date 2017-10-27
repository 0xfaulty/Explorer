package com.defaulty.explorer.panels.center;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import com.defaulty.explorer.model.item.FilteredTreeItem;
import com.defaulty.explorer.model.tree.ModelCRUD;
import com.defaulty.explorer.panels.FilePopupMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
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
    private final ModelCRUD modelCRUD;
    private TilePane tilePane = new TilePane();

    public FileGrid(ViewConnector connector) {
        connector.register(this);
        this.modelCRUD = connector.getModelCRUD();

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
        }
    }

    /**
     * Добавить новый элемент на панель.
     *
     * @param fork - новый элемент.
     */
    private void addNode(TreeItem<File> fork) {
        if (fork != null) {
            GridItem gridItem = new GridItem(fork.getValue());
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
            gridItem.updateIcon();
    }

    /**
     * Сменить отображаемую ветку
     *
     * @param fork - новая ветка.
     */
    private void changeFork(TreeItem<File> fork) {
        tilePane.getChildren().clear();
        for (TreeItem<File> fItem : fork.getChildren()) {
            GridItem gridItem = new GridItem(fItem.getValue());
            itemHashMap.put(fItem.getValue(), gridItem);
            tilePane.getChildren().add(gridItem);
        }
        if (fork instanceof FilteredTreeItem) {
            FilteredTreeItem ftItem = (FilteredTreeItem) fork;
            for (TreeItem<File> fItem : ftItem.getFileChildren()) {
                GridItem gridItem = new GridItem(fItem.getValue());
                itemHashMap.put(fItem.getValue(), gridItem);
                tilePane.getChildren().add(gridItem);
            }
        }
        changeState(fork);
    }

    /**
     * Единичный элемент сетки. Содержит изображение и подпись.
     */
    private class GridItem extends VBox {

        private File file;

        private Label icon;
        private Label label;

        GridItem(File file) {
            this.file = file;
            icon = new Label();
            updateIcon();
            String name = file.getName();
            if (name.length() > 25) name = name.substring(0, 22) + "..";
            label = new Label(name);
            getChildren().addAll(icon, label);

            setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (file.isDirectory()) modelCRUD.loadFork(file);
                }
                if (mouseEvent.getClickCount() == 1) {
                    //setStyle("-fx-background-color: #9dc6e0");
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        popup.show(file, this, mouseEvent);
                    } else
                        popup.hide();
                }
            });

            setAlignment(Pos.CENTER);
        }

        void updateIcon() {
            ImageView view = new ImageSetter().getImageView(file, ImageSizePack.ImageSize.BIG);
            icon.setGraphic(view);
        }

    }
}
