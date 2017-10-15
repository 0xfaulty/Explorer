package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
import com.defaulty.explorer.model.TreeModel;
import javafx.scene.control.TreeItem;

import java.io.File;

public interface ViewConnector {

    /**
     * Вызвов подгрузки к дереву указанной ветки.
     *
     * @param fork - ветка необходимая для подгрузки.
     */
    void loadFork(TreeItem<File> fork, boolean checkout);

    /**
     * Вызов изменения стилей.
     *
     * @param t - новый стиль.
     */
    void changeTheme(ThemeType t);

    void changeRightView(ViewType t);

    void treeSearch(TreeItem<File> item, String s);

    /**
     * Вызов для добавления папки (отображения нового элемента в таблице).
     */
    void createFolder();

    void register(ViewObserver outlet);

    void createModel(TreeModel treeModel);

}
