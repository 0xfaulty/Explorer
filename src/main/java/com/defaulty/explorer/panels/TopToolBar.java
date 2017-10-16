package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import impl.org.controlsfx.skin.BreadCrumbBarSkin;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.BreadCrumbBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TopToolBar extends BorderPane implements ViewObserver {

    private List<TreeItem<File>> history = new ArrayList<>();
    private int historyIndex = -1;

    private TreeItem<File> currentNode;

    private Button btnForward = new Button();
    private Button btnBack = new Button();

    private BreadCrumbBar<File> crumbBar = new BreadCrumbBar<>();

    private final ViewConnector connector;

    public TopToolBar(ViewConnector connector) {
        this.connector = connector;
        this.connector.register(this);
        init();
    }

    private void init() {
        btnBack.setText("<");
        btnBack.setDisable(true);
        btnBack.setOnAction(event -> historyBack());

        btnForward.setText(">");
        btnForward.setDisable(true);
        btnForward.setOnAction(event -> historyForward());

        crumbBar.setOnCrumbAction(bae -> {
            connector.loadFork(bae.getSelectedCrumb(), true);
            if (!crumbBar.getSelectedCrumb().equals(history.get(historyIndex))) historyBack();
        });

        crumbBar.setCrumbFactory(param -> {
            if (param.getValue() != null) {
                File file = param.getValue();
                String name = file.getParentFile() == null ? file.getAbsolutePath() : file.getName();
                return new BreadCrumbBarSkin.BreadCrumbButton(name);
            } else return new BreadCrumbBarSkin.BreadCrumbButton("");
        });

        BorderPane buttonsPane = new BorderPane();
        buttonsPane.setLeft(btnBack);
        buttonsPane.setRight(btnForward);
        BorderPane.setMargin(btnBack, new Insets(0, 5, 0, 0));

        BorderPane crumbBarPane = new BorderPane();
        crumbBarPane.setCenter(crumbBar);

        SearchBox searchBox = new SearchBox();

        final BorderPane searchPane = new BorderPane();
        searchPane.getStylesheets().setAll("css/SearchBox.css");
        searchPane.setPrefWidth(200);
        searchPane.setMaxWidth(Control.USE_PREF_SIZE);
        searchPane.setCenter(searchBox);

        searchBox.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER && currentNode != null)
                connector.treeSearch(currentNode, searchBox.getText());
        });

        setLeft(buttonsPane);
        setCenter(crumbBarPane);
        setRight(searchPane);

        BorderPane.setMargin(buttonsPane, new Insets(5));
        BorderPane.setMargin(crumbBarPane, new Insets(5));
        BorderPane.setMargin(searchPane, new Insets(5));
    }

    private void historyBack() {
        if (!history.isEmpty() && historyIndex > 0) {
            historyIndex--;
            TreeItem<File> item = history.get(historyIndex);
            crumbBar.setSelectedCrumb(item);
            connector.loadFork(item, true);
            btnForward.setDisable(false);
            if (historyIndex == 0) btnBack.setDisable(true);
        }
    }

    private void historyForward() {
        if (history.size() > historyIndex + 1) {
            historyIndex++;
            TreeItem<File> item = history.get(historyIndex);
            crumbBar.setSelectedCrumb(item);
            connector.loadFork(item, true);
            btnBack.setDisable(false);
            if (historyIndex + 1 == history.size()) btnForward.setDisable(true);
        }
    }

    @Override
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case CHANGE_FORK:
                changeFork(event.getFork());
                break;
        }
    }

    private void changeFork(TreeItem<File> fork) {
        if (fork != null && fork.getValue() != null) {
            currentNode = fork;
            if (historyIndex >= 0) {
                TreeItem<File> curItem = history.get(historyIndex);
                if (curItem != null && fork.getValue() != null && curItem.getValue() != null) {
                    if (!curItem.getValue().getPath().equals(fork.getValue().getPath()))
                        addHistory(fork);
                } else
                    addHistory(fork);
            } else
                addHistory(fork);
        }
    }

    private void addHistory(TreeItem<File> item) {
        Platform.runLater(() -> {
            crumbBar.setSelectedCrumb(item);
            history.add(historyIndex + 1, item);
            historyIndex++;
            for (int i = history.size() - 1; i > historyIndex; i--) {
                history.remove(i);
            }
            btnBack.setDisable(false);
            btnForward.setDisable(true);
        });
    }

}
