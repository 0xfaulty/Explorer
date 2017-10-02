package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.IconSetter;
import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewObserver;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.File;

public class FileTable extends BorderPane implements ViewObserver {

    private final ViewObserver viewObserver;
    private TableView<TreeItem<File>> table = new TableView<>();

    public FileTable(ViewObserver observer) {
        this.viewObserver = observer;
    }

    public void init() {
        TableColumn<TreeItem<File>, TreeItem<File>> nameCol = getColumn("Имя", "itemName", 200);
        TableColumn<TreeItem<File>, String> sizeCol = getColumn("Размер", "itemSize", 100);
        TableColumn<TreeItem<File>, String> typeCol = getColumn("Тип", "itemType", 150);
        TableColumn<TreeItem<File>, String> changeDateCol = getColumn("Дата", "itemData", 200);

        nameCol.setComparator((TreeItem<File> ti1, TreeItem<File> ti2) -> {
            if (ti1.getValue().isDirectory() == ti2.getValue().isDirectory()) {
                if (ti1.getValue() == null || ti2.getValue() == null) return 0;
                return ti1.getValue().getName().compareToIgnoreCase(ti2.getValue().getName());
            } else
                return ti1.getValue().isDirectory() ? -1 : 1;
        });

        nameCol.setCellFactory(column -> new TableCell<TreeItem<File>, TreeItem<File>>() {
            @Override
            protected void updateItem(TreeItem<File> item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty || item.getValue() == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    File f = item.getValue();
                    setText(f.getParentFile() == null ? File.separator : f.getName());
                    setGraphic(IconSetter.getImageView(item));
                }
            }
        });

        table.getColumns().add(nameCol);
        table.getColumns().add(sizeCol);
        table.getColumns().add(typeCol);
        table.getColumns().add(changeDateCol);
        table.getSortOrder().add(nameCol);
        table.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                TreeItem<File> item = table.getSelectionModel().getSelectedItem();
                if (item != null && item instanceof FolderTreeItem) {
                    if (((FolderTreeItem) item).isDirectory()) {
                        FolderTreeItem ftItem = (FolderTreeItem) item;
                        ftItem.createExpandTreeFork();
                        viewObserver.changeNode(item);
                    }
                }
            }
        });

        disableColumnUnsortedOnClick(table);

        super.setCenter(table);
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
    public void changeNode(TreeItem<File> item) {
        if (item != null) {
            table.getItems().clear();
            for (TreeItem<File> fItem : item.getChildren()) {
                table.getItems().add(fItem);
            }
            if (item instanceof FolderTreeItem) {
                FolderTreeItem ftItem = (FolderTreeItem) item;
                for (TreeItem<File> fItem : ftItem.getFileChildrenList()) {
                    table.getItems().add(fItem);
                }
            }
            table.sort();
        }
    }

    @Override
    public void setTheme(ThemeType t) {
        switch (t) {
            case DARK:
                table.getStylesheets().setAll("css/table-dark.css");
                break;
            case LIGHT:
                table.getStylesheets().setAll("css/table-light.css");
                break;
        }
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

    @Override
    public void createFolder() {
    }
}
