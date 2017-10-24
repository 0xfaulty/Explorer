package com.defaulty.explorer.control.events;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.model.search.SearchTask;
import com.defaulty.explorer.panels.center.ViewType;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Контракт для задания и последующего получения из события переданных
 * в зависимости от типа события в нём объектов.
 */
public interface ViewEvent {

    /**
     * Получения типа события.
     * @return - тип {@code EventType}.
     */
    EventType getEventType();

    TreeItem<File> getFork();

    void setFork(TreeItem<File> fork);

    ThemeType getThemeType();

    void setThemeType(ThemeType themeType);

    ViewType getViewType();

    void setViewType(ViewType viewType);

    SearchTask getSearchTask();

    void setSearchTask(SearchTask searchTask);

}
