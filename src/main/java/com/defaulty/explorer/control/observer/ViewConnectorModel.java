package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.model.search.SearchTask;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Контракт описывающий
 */
public interface ViewConnectorModel extends ViewConnector {

    /**
     * Сменить текущую ветку представления.
     *
     * @param fork - новая ветка.
     */
    void changeFork(TreeItem<File> fork);

    /**
     * Изменить состояние элемента.
     *
     * @param fork - измененный элемент.
     */
    void changeState(TreeItem<File> fork);

    /**
     * Отправить выполняемую задачу поиска.
     *
     * @param searchTask - задача поиска.
     */
    void sendSearchTask(SearchTask searchTask);

    /**
     * Добавить в представление наблюдателя новый элемент полученный при
     * исполнении задачи поиска.
     *
     * @param fork - новый найденный элемент.
     */
    void addNode(TreeItem<File> fork);

}
