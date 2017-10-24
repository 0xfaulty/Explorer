package com.defaulty.explorer.control.events;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.model.search.SearchTask;
import com.defaulty.explorer.panels.center.ViewType;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Класс поддерживающий контракт {@code ViewEvent}.
 */
public class ViewEventImpl implements ViewEvent {

    private EventType eventType;

    private TreeItem<File> fork;
    private ThemeType themeType;
    private ViewType viewType;
    private SearchTask searchTask;

    public ViewEventImpl(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public TreeItem<File> getFork() {
        return fork;
    }

    @Override
    public void setFork(TreeItem<File> fork) {
        this.fork = fork;
    }

    @Override
    public ThemeType getThemeType() {
        return themeType;
    }

    @Override
    public void setThemeType(ThemeType themeType) {
        this.themeType = themeType;
    }

    @Override
    public ViewType getViewType() {
        return viewType;
    }

    @Override
    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    @Override
    public SearchTask getSearchTask() {
        return searchTask;
    }

    @Override
    public void setSearchTask(SearchTask searchTask) {
        this.searchTask = searchTask;
    }

}
