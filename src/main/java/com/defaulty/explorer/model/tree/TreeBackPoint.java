package com.defaulty.explorer.model.tree;

import java.io.File;

/**
 * Точка возвращения из задачи элементов типа {@code TreeItem<File>}.
 */
public interface TreeBackPoint {
    void accept(File file);
}
