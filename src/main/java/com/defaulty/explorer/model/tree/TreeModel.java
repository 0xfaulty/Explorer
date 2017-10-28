package com.defaulty.explorer.model.tree;

import com.defaulty.explorer.control.observer.ViewConnectorModel;

/**
 * Контракт {@code ModelOperations} расширенный методом установления
 * связывающего объекта для модели.
 */
public interface TreeModel extends ModelOperations {
    void setViewConnector(ViewConnectorModel viewConnector);
}
