package com.defaulty.explorer.model.tree;

import com.defaulty.explorer.control.observer.ViewConnectorModel;

/**
 * Контракт {@code ModelCRUD} расширенный методом установления
 * связывающего объекта для модели.
 */
public interface TreeModel extends ModelCRUD {
    void setViewConnector(ViewConnectorModel viewConnector);
}
