package com.defaulty.explorer.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class FileTableOld extends BorderPane {

    private TableView<FileRow> table = new TableView<>();
    private final ObservableList<FileRow> data =
            FXCollections.observableArrayList(
                    new FileRow("test", "123", "folder", "11/11/11"),
                    new FileRow("test1", "1", "folder", "11/11/11"),
                    new FileRow("test2", "2", "exe", "11/11/11"),
                    new FileRow("test3", "3", "doc", "11/11/11"),
                    new FileRow("test4", "4", "txt", "11/11/11")
            );

    public FileTableOld() {
        TableColumn<FileRow, Pane> nameCol = new TableColumn<>("Имя");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        TableColumn<FileRow, String> sizeCol = new TableColumn<>("Размер");
        sizeCol.setMinWidth(200);
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<FileRow, String> typeCol = new TableColumn<>("Тип");
        typeCol.setMinWidth(200);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<FileRow, String> changeDateCol = new TableColumn<>("Дата");
        changeDateCol.setMinWidth(100);
        changeDateCol.setCellValueFactory(new PropertyValueFactory<>("data"));

        table.getStylesheets().addAll("css/table.css");

        table.setItems(data);
        table.getColumns().add(nameCol);
        table.getColumns().add(sizeCol);
        table.getColumns().add(typeCol);
        table.getColumns().add(changeDateCol);

        super.setCenter(table);
    }
}
