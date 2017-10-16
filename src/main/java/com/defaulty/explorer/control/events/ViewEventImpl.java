package com.defaulty.explorer.control.events;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
import javafx.scene.control.TreeItem;

import java.io.File;

public class ViewEventImpl implements ViewEvent {

    private EventType eventType;

    private TreeItem<File> fork;
    private ThemeType themeType;
    private ViewType viewType;

    public ViewEventImpl(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public TreeItem<File> getFork() {
        return fork;
    }

    public void setFork(TreeItem<File> fork) {
        this.fork = fork;
    }

    public ThemeType getThemeType() {
        return themeType;
    }

    public void setThemeType(ThemeType themeType) {
        this.themeType = themeType;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }
}
