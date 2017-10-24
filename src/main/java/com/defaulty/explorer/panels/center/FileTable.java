package com.defaulty.explorer.panels.center;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import com.defaulty.explorer.model.item.FileLabeledCell;
import com.defaulty.explorer.model.item.FilteredTreeItem;
import com.defaulty.explorer.model.tree.ModelCRUD;
import com.defaulty.explorer.panels.FilePopupMenu;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.File;
import java.util.HashMap;

/**
 * Панель представляющая отображение системы в виде таблицы.
 * Для каждого элемента имеется четыре колонки информации.
 */
public class FileTable extends BorderPane implements ViewObserver {

    private final FilePopupMenu popup;
    private final ModelCRUD modelCRUD;
    private TableView<TreeItem<File>> table = new TableView<>();
    private TreeItem<File> currentRoot;

    private HashMap<File, FileLabeledCell> cellHashMap = new HashMap<>();

    public FileTable(ViewConnector connector) {
        connector.register(this);
        this.modelCRUD = connector.getModelCRUD();
        popup = new FilePopupMenu(connector);
        init();
    }

    /**
     * Инициализация таблицы.
     */
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

        nameCol.setCellFactory(col -> new FileLabeledCell(table, cellHashMap).getTableCell());

        table.getColumns().add(nameCol);
        table.getColumns().add(sizeCol);
        table.getColumns().add(typeCol);
        table.getColumns().add(changeDateCol);
        table.getSortOrder().add(nameCol);
        table.setEditable(true);
        table.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (table.getSelectionModel().getSelectedItem() != null) {
                    if (table.getSelectionModel().getSelectedItem().getValue().isDirectory())
                        modelCRUD.loadFork(table.getSelectionModel().getSelectedItem());
                }
            }
            if (mouseEvent.getClickCount() == 1) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    TreeItem<File> item = table.getSelectionModel().getSelectedItem();
                    if (item != null) {
                        popup.show(item, table, mouseEvent);
                    }
                } else
                    popup.hide();
            }
        });

        disableColumnUnsortedOnClick(table);

        super.setCenter(table);
    }

    /**
     * Отключение третьего вида сортировки у колонок.
     * @param tableView - табличное представление.
     * @param <T> - тип элементов.
     */
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
            case SEARCH_TASK:
                table.getItems().clear();
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
        if (fork != null) table.getItems().add(fork);
    }

    /**
     * Обновить состояние объекта
     *
     * @param fork - обновляемый объект.
     */
    private void changeState(TreeItem<File> fork) {
        if (fork != null && fork.getValue() != null) {
            if (currentRoot != null && currentRoot.getChildren().contains(fork)) {
                FileLabeledCell cell = cellHashMap.get(fork.getValue());
                if (cell != null) {
                    Labeled labeled = cell.getLabeled();
                    if (labeled != null)
                        labeled.setGraphic(new ImageSetter().getImageView(fork, ImageSizePack.ImageSize.SMALL));
                }
            }
        }
    }

    /**
     * Сменить отображаемую ветку
     *
     * @param fork - новая ветка.
     */
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

    /**
     * Сметить оформление панели.
     *
     * @param t тип офорления.
     */
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

    /**
     * Создание колонки.
     * @param name - имя.
     * @param param - параметр для бинда.
     * @param width - ширина.
     * @param <T1> - представляемый тип.
     * @param <T2> - рабочий тип.
     * @return - новая колонка.
     */
    private <T1, T2> TableColumn<T1, T2> getColumn(String name, String param, int width) {
        TableColumn<T1, T2> column = new TableColumn<>(name);
        column.prefWidthProperty().setValue(width);
        column.setMinWidth(50);
        column.setCellValueFactory(new PropertyValueFactory<>(param));
        setPosAlignment(column);
        return column;
    }

    /**
     * Установка отступов для колонки.
     * @param col - колонка.
     * @param <T1> - представляемый тип.
     * @param <T2> - рабочий тип.
     */
    private <T1, T2> void setPosAlignment(TableColumn<T1, T2> col) {
        Callback<TableColumn<T1, T2>, TableCell<T1, T2>> cellFactory = col.getCellFactory();
        col.setCellFactory(column -> {
            TableCell<T1, T2> cell = cellFactory.call(column);
            cell.setPadding(new Insets(0, 0, 0, 20));
            return cell;
        });
    }

}
