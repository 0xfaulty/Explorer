package com.defaulty.explorer.control.events;

/**
 * Список восможных событий передаваемых наблюдателям.
 */
public enum EventType {
    /**
     * Переключание ветки.
     */
    CHANGE_FORK,
    /**
     * Изменения состояния элемента.
     */
    CHANGE_STATE,
    /**
     * Изменение внешнего вида (темы).
     */
    SET_THEME,
    /**
     * Переключение вида отображения файловой системы.
     */
    SET_RIGHT_VIEW,
    /**
     * Задача поиска элемента.
     */
    SEARCH_TASK,
    /**
     * Добавить новый элемент.
     */
    ADD_NODE
}
