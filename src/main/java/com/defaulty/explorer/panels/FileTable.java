package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import com.defaulty.explorer.model.FileLabeledCell;
import com.defaulty.explorer.model.FilteredTreeItem;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.File;
import java.util.HashMap;

public class FileTable extends BorderPane implements ViewObserver {

    private final ViewConnector connector;
    private TableView<TreeItem<File>> table = new TableView<>();

    private TreeItem<File> currentRoot;

    private HashMap<File, Labeled> cellHashMap = new HashMap<>();

    private FilePopupMenu popup = new FilePopupMenu();

    public FileTable(ViewConnector connector) {
        this.connector = connector;
        this.connector.register(this);
        init();
    }

    private void init() {
        TableColumn<TreeItem<File>, File> nameCol = getColumn("Имя", "itemFile", 200);
        TableColumn<TreeItem<File>, String> sizeCol = getColumn("Размер", "itemSize", 100);
        TableColumn<TreeItem<File>, String> typeCol = getColumn("Тип", "itemType", 150);
        TableColumn<TreeItem<File>, String> changeDateCol = getColumn("Дата", "itemData", 200);

        nameCol.setComparator((ti1, ti2) -> {
            if (ti1.isDirectory() == ti2.isDirectory()) {
                return ti1.getName().compareToIgnoreCase(ti2.getName());
            } else
                return ti1.isDirectory() ? -1 : 1;
        });

        nameCol.setCellFactory(col -> getNewCell());

        table.getColumns().add(nameCol);
        table.getColumns().add(sizeCol);
        table.getColumns().add(typeCol);
        table.getColumns().add(changeDateCol);
        table.getSortOrder().add(nameCol);
        table.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (table.getSelectionModel().getSelectedItem() != null) {
                    if (table.getSelectionModel().getSelectedItem().getValue().isDirectory())
                        connector.loadFork(table.getSelectionModel().getSelectedItem(), true);
                }
            }
            if (mouseEvent.getClickCount() == 1) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    TreeItem<File> item = table.getSelectionModel().getSelectedItem();
                    if (item != null) {
                        popup.show(item.getValue(), table, mouseEvent);
                    }
                } else
                    popup.hide();
            }
        });

        disableColumnUnsortedOnClick(table);

        super.setCenter(table);
    }

    private TableCell<TreeItem<File>, File> getNewCell() {
        return new FileLabeledCell(table, cellHashMap).getTableCell();
    }

    private <T> void disableColumnUnsortedOnClick(TableView<T> tableView) {
        tableView.getSortOrder().addListener((ListChangeListener<TableColumn<T, ?>>) c -> {
            while (c.next()) {
                if (c.wasRemoved() && c.getRemovedSize() == 1 && !c.wasAdded()) {
                    final TableColumn<T, ?> removedColumn = c.getRemoved().get(0);
                    removedColumn.getTableView().getSortOrder().add(removedColumn);
                    removedColumn.setSortType(TableColumn.SortType.ASCENDING);
                }
            }
        });
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
            case SET_THEME:
                setTheme(event.getThemeType());
                break;
            case CREATE_FOLDER:
                createFolder();
                break;
        }
    }

    private void changeState(TreeItem<File> fork) {
        if (fork != null && fork.getValue() != null) {
            if (currentRoot != null && currentRoot.getChildren().contains(fork)) {
                Labeled labeled = cellHashMap.get(fork.getValue());
                if (labeled != null)
                    labeled.setGraphic(new ImageSetter().getImageView(fork, ImageSizePack.ImageSize.SMALL));
            }
        }
    }

    private void changeFork(TreeItem<File> fork) {
        if (fork != null) {
            currentRoot = fork;
            table.getItems().clear();
            if (fork instanceof FilteredTreeItem) {
                FilteredTreeItem ftItem = (FilteredTreeItem) fork;
                for (TreeItem<File> fItem : ftItem.getFileChildren()) {
                    table.getItems().add(fItem);
                }
            }
            for (TreeItem<File> fItem : fork.getChildren()) {
                table.getItems().add(fItem);
            }

            table.sort();
        }
    }

    private void setTheme(ThemeType t) {
        switch (t) {
            case DARK:
                table.getStylesheets().setAll("css/table-dark.css");
                break;
            case LIGHT:
                table.getStylesheets().setAll("css/table-light.css");
                break;
        }
    }

    private void createFolder() {
        //TODO: this
    }

    private <T1, T2> TableColumn<T1, T2> getColumn(String name, String param, int size) {
        TableColumn<T1, T2> column = new TableColumn<>(name);
        column.prefWidthProperty().setValue(size);
        column.setMinWidth(50);
        column.setCellValueFactory(new PropertyValueFactory<>(param));
        setPosAlignment(column);
        return column;
    }

    private <T1, T2> void setPosAlignment(TableColumn<T1, T2> col) {
        Callback<TableColumn<T1, T2>, TableCell<T1, T2>> cellFactory = col.getCellFactory();
        col.setCellFactory(column -> {
            TableCell<T1, T2> cell = cellFactory.call(column);
            cell.setPadding(new Insets(0, 0, 0, 20));
            return cell;
        });
    }

}
