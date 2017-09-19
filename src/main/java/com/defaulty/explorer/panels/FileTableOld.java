package com.defaulty.explorer.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class FileTableOld extends BorderPane {

    private TableView<Person> table = new TableView<>();
    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
                    new Person("Jacob", "Smith", "jacob.smith@example.com", "Coffee"),
                    new Person("Isabella", "Johnson", "isabella.johnson@example.com", "Fruit"),
                    new Person("Ethan", "Williams", "ethan.williams@example.com", "Fruit"),
                    new Person("Emma", "Jones", "emma.jones@example.com", "Coffee"),
                    new Person("Michael", "Brown", "michael.brown@example.com", "Fruit")

            );

    public FileTableOld() {
        TableColumn<Person, String> firstNameCol = new TableColumn<>("Имя");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        //String style = "-fx-text-base-color";
        //firstNameCol.setStyle("-fx-text-fill: " + style);    tableView.
        table.setStyle("-fx-selection-bar: white; " +
                "-fx-selection-bar-non-focused: salmon; " +
                "-fx-background-color: red;" +
                "-fx-border-color: white"
        );

        //firstNameCol.setStyle("-fx);

        TableColumn<Person, String> changeDateCol = new TableColumn<>("Дата изменения");
        changeDateCol.setMinWidth(100);
        changeDateCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));

        TableColumn<Person, String> typeCol = new TableColumn<>("Тип");
        typeCol.setMinWidth(200);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        TableColumn<Person, String> sizeCol = new TableColumn<>("Размер");
        typeCol.setMinWidth(200);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, changeDateCol, typeCol, sizeCol);

        super.setCenter(table);
    }
}
