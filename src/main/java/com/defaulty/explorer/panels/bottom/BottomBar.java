package com.defaulty.explorer.panels.bottom;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.image.CustomIcons;
import com.defaulty.explorer.model.search.SearchTask;
import com.defaulty.explorer.panels.center.ViewType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.File;

/**
 * Нижняя панель. Включает в себя панель задачь поиска и
 * кнопки переключения видов центральных панелей.
 */
public class BottomBar extends BorderPane implements ViewObserver {

    private HBox taskPane = new HBox();

    private final ViewConnector connector;

    /**
     * Конструктор нижней панели.
     *
     * @param connector - класс обслуживания наблюдателей.
     */
    public BottomBar(ViewConnector connector) {
        connector.register(this);
        this.connector = connector;

        Button tableViewButt = new Button();
        tableViewButt.setGraphic(new ImageView(
                new CustomIcons().getImageResource("icons/table.png", 16, 13)));
        tableViewButt.setOnAction(event -> connector.changeRightView(ViewType.TABLE));
        Button gridViewButt = new Button();
        gridViewButt.setGraphic(new ImageView(
                new CustomIcons().getImageResource("icons/grid.png", 16, 13)));
        gridViewButt.setOnAction(event -> connector.changeRightView(ViewType.GRID));

        BorderPane pane = new BorderPane();
        pane.setLeft(tableViewButt);
        pane.setRight(gridViewButt);

        this.setLeft(taskPane);
        this.setRight(pane);
    }

    /**
     * Метод получения наблюдателем событий.
     *
     * @param event - новое событие.
     */
    @Override
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case SEARCH_TASK:
                SearchTask task = event.getSearchTask();
                SearchTaskLabel taskLabel = new SearchTaskLabel(task, this::removeTaskLabel);
                taskPane.getChildren().add(taskLabel);
                if (taskPane.getChildren().size() > 5)
                    taskPane.getChildren().remove(0);
                break;
            case CHANGE_FORK:
                for (Node t : taskPane.getChildren()) {
                    if (t instanceof SearchTaskLabel) {
                        File file = event.getFork().getValue();
                        if (file != null) {
                            ((SearchTaskLabel) t).changeSendStatus(file.getPath());
                        }
                    }
                }
                break;
        }
    }

    /**
     * Удаления поппанели из панели задачпоиска.
     *
     * @param taskLabel - задача для удаления.
     */
    private void removeTaskLabel(SearchTaskLabel taskLabel) {
        taskLabel.getStopPoint().run();
        taskPane.getChildren().remove(taskLabel);
    }

    /**
     * Одиночная подпанель задачи поиска. Включает в себя две надписи и кнопку закрытия.
     */
    private class SearchTaskLabel extends HBox {

        private SearchTask task;
        private Label label;
        private Label labelCount;

        /**
         * Конструктор подпанели задачи поиска.
         *
         * @param task       - класс задачи.
         * @param closePoint - метод удаления панели из интерфейса.
         */
        SearchTaskLabel(SearchTask task, CloseBackPoint closePoint) {
            this.task = task;
            this.task.addCountListener(this::increaseCounter);

            String style = "-fx-text-fill: #393939; -fx-font-style: italic";

            setOnMouseClicked((e) -> {
                if (e.getButton() == MouseButton.PRIMARY)
                    connector.loadSearchResults(task);
            });

            label = new Label("Поиск: \"" + task.getKeyWord() + "\" ");
            label.setFont(new Font(11));
            label.setStyle(style);
            labelCount = new Label("Найдено: 0");
            labelCount.setFont(new Font(11));
            labelCount.setStyle(style);

            Button close = new Button("x");
            close.setFont(new Font(6));
            close.setOnAction(e -> closePoint.removeTaskLabel(this));

            getChildren().addAll(label, labelCount, close);

            setPadding(new Insets(5, 5, 5, 5));
            setSpacing(3);
            setStyle(style);
        }

        private void increaseCounter() {
            labelCount.setText("Найдено: " + task.getDoneCount());
        }

        Runnable getStopPoint() {
            return task::stop;
        }

        public void changeSendStatus(String s) {
            if (s.equals(task.getTaskFullName()))
                task.setSendNodes(true);
            else
                task.setSendNodes(false);

        }
    }

    /**
     * Интерфейс обозначения метода, вызывающегося из подпанели задачи поиска для
     * удаления этой панели из интерфейса.
     */
    private interface CloseBackPoint {
        void removeTaskLabel(SearchTaskLabel taskLabel);
    }


}
