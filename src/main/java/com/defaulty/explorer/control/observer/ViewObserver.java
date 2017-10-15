package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
import javafx.scene.control.TreeItem;

import java.io.File;

public interface ViewObserver {


    void changeFork(TreeItem<File> fork);

    /**
     * Обновить текущую ветку вручную если представление не основывается на
     * основной модели дерева (например таблица).
     * @param fork - родительский элемент обновлённой ветки.
     */
    void changeState(TreeItem<File> fork);

    /**
     * Вызов изменения стилей.
     * @param t - новый стиль.
     */
    void setTheme(ThemeType t);

    void setRightView(ViewType t);

    void createFolder();

}
