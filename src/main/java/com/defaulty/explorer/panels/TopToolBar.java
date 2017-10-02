package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewObserver;
import impl.org.controlsfx.skin.BreadCrumbBarSkin;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.BreadCrumbBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TopToolBar extends BorderPane implements ViewObserver {

    private List<TreeItem<File>> history = new ArrayList<>();
    private int historyIndex = -1;

    private Button btnForward = new Button();
    private Button btnBack = new Button();

    //private String curPath = System.getProperty("user.dir");

    private BreadCrumbBar<File> crumbBar = new BreadCrumbBar<>();

    private final ViewObserver viewObserver;

    public TopToolBar(ViewObserver observer) {
        this.viewObserver = observer;
    }

    public void init() {
        btnBack.setText("<");
        btnBack.setDisable(true);
        btnBack.setOnAction(event -> historyBack());

        btnForward.setText(">");
        btnForward.setDisable(true);
        btnForward.setOnAction(event -> historyForward());

        crumbBar.setOnCrumbAction(bae -> {
            viewObserver.changeNode(bae.getSelectedCrumb());
            if (!crumbBar.getSelectedCrumb().equals(history.get(historyIndex))) historyBack();
        });

        crumbBar.setCrumbFactory(param -> {
            if (param.getValue() != null) {
                File file = param.getValue();
                String name = file.getParentFile() == null ? file.getAbsolutePath() : file.getName();
                return new BreadCrumbBarSkin.BreadCrumbButton(name);
            } else return new BreadCrumbBarSkin.BreadCrumbButton("");
        });

        ToolBar toolBarLeft = new ToolBar();
        toolBarLeft.getItems().add(btnBack);
        toolBarLeft.getItems().add(btnForward);

        ToolBar toolBarCenter = new ToolBar();
        toolBarCenter.getItems().add(crumbBar);

        TextField searchField = new TextField("поиск..");
        ToolBar toolBarRight = new ToolBar();
        toolBarRight.getItems().add(searchField);

        setLeft(toolBarLeft);
        setCenter(toolBarCenter);
        setRight(toolBarRight);
    }

    private void historyBack() {
        if (!history.isEmpty() && historyIndex > 0) {
            historyIndex--;
            TreeItem<File> item = history.get(historyIndex);
            crumbBar.setSelectedCrumb(item);
            viewObserver.changeNode(item);
            btnForward.setDisable(false);
            if (historyIndex == 0) btnBack.setDisable(true);
        }
    }

    private void historyForward() {
        if (history.size() > historyIndex + 1) {
            historyIndex++;
            TreeItem<File> item = history.get(historyIndex);
            crumbBar.setSelectedCrumb(item);
            viewObserver.changeNode(item);
            btnBack.setDisable(false);
            if (historyIndex + 1 == history.size()) btnForward.setDisable(true);
        }
    }

    @Override
    public void changeNode(TreeItem<File> item) {
        if (historyIndex >= 0) {
            TreeItem<File> curItem = history.get(historyIndex);
            if (curItem != null && curItem.getValue() != null) {
                if (!curItem.getValue().getPath().equals(item.getValue().getPath()))
                    addHistory(item);
            } else
                addHistory(item);
        } else
            addHistory(item);
    }

    private void addHistory(TreeItem<File> item) {
        crumbBar.setSelectedCrumb(item);
        history.add(historyIndex + 1, item);
        historyIndex++;
        for (int i = history.size() - 1; i > historyIndex; i--) {
            history.remove(i);
        }
        btnBack.setDisable(false);
        btnForward.setDisable(true);
    }

    @Override
    public void setTheme(ThemeType t) {

    }

    @Override
    public void createFolder() {

    }

}
