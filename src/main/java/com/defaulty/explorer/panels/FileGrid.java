package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import com.defaulty.explorer.model.FilteredTreeItem;
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

public class FileGrid extends ScrollPane implements ViewObserver {

    private HashMap<File, GridItem> itemHashMap = new HashMap<>();
    private FilePopupMenu popup = new FilePopupMenu();

    private final ViewConnector connector;
    private TilePane tilePane = new TilePane();

    public FileGrid(ViewConnector connector) {
        this.connector = connector;
        this.connector.register(this);
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
        }
    }

    private void changeFork(TreeItem<File> fork) {
        tilePane.getChildren().clear();
        for (TreeItem<File> fItem : fork.getChildren()) {
            GridItem gridItem = new GridItem(fItem);
            itemHashMap.put(fItem.getValue(), gridItem);
            tilePane.getChildren().add(gridItem);
        }
        if (fork instanceof FilteredTreeItem) {
            FilteredTreeItem ftItem = (FilteredTreeItem) fork;
            for (TreeItem<File> fItem : ftItem.getFileChildren()) {
                GridItem gridItem = new GridItem(fItem);
                itemHashMap.put(fItem.getValue(), gridItem);
                tilePane.getChildren().add(gridItem);
            }
        }
    }

    private void changeState(TreeItem<File> fork) {
        GridItem gridItem = itemHashMap.get(fork.getValue());
        if (gridItem != null)
            gridItem.updateIcon();
    }

    private class GridItem extends VBox {

        private TreeItem<File> item;

        private Label icon;
        private Label label;

        public GridItem(TreeItem<File> item) {
            this.item = item;
            icon = new Label();
            updateIcon();
            String name = item.getValue().getName();
            if (name.length() > 25) name = name.substring(0, 22) + "..";
            label = new Label(name);
            getChildren().addAll(icon, label);

            setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (item.getValue().isDirectory()) connector.loadFork(item, true);
                }
                if (mouseEvent.getClickCount() == 1) {
                    //setStyle("-fx-background-color: #9dc6e0");
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        popup.show(item.getValue(), this, mouseEvent);
                    } else
                        popup.hide();
                }
            });

            setAlignment(Pos.CENTER);
        }

        public void updateIcon() {
            ImageView view = new ImageSetter().getImageView(item.getValue(), ImageSizePack.ImageSize.BIG);
            icon.setGraphic(view);
        }

    }
}
