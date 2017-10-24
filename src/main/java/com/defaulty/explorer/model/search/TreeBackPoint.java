package com.defaulty.explorer.model.search;

import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Точка возвращения из задачи элементов типа {@code TreeItem<File>}.
 */
public interface TreeBackPoint {
    void accept(TreeItem<File> item);
}
