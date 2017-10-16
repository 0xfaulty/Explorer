package com.defaulty.explorer.control.events;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
import javafx.scene.control.TreeItem;

import java.io.File;

public interface ViewEvent {

    EventType getEventType();

    TreeItem<File> getFork();

    void setFork(TreeItem<File> fork);

    ThemeType getThemeType();

    void setThemeType(ThemeType themeType);

    ViewType getViewType();

    void setViewType(ViewType viewType);

}
