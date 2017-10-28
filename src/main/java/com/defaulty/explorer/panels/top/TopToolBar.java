package com.defaulty.explorer.panels.top;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.model.tree.ModelOperations;
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

/**
 * Верхняя панель. Содержит в себе кнопки навигации по истории переходов
 * файловой системы, панель навигации, панель поиска.
 */
public class TopToolBar extends BorderPane implements ViewObserver {

    private List<TreeItem<File>> history = new ArrayList<>();
    private int historyIndex = -1;

    private TreeItem<File> currentNode;

    private Button btnForward = new Button();
    private Button btnBack = new Button();

    private BreadCrumbBar<File> crumbBar = new BreadCrumbBar<>();

    private final ModelOperations modelOperations;

    public TopToolBar(ViewConnector connector) {
        connector.register(this);
        this.modelOperations = connector.getModelCRUD();
        init();
    }

    /**
     * Инициализация панели.
     */
    private void init() {
        btnBack.setText("<");
        btnBack.setDisable(true);
        btnBack.setOnAction(event -> historyBack());

        btnForward.setText(">");
        btnForward.setDisable(true);
        btnForward.setOnAction(event -> historyForward());

        crumbBar.setOnCrumbAction(bae -> {
            modelOperations.loadFork(bae.getSelectedCrumb().getValue());
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
                modelOperations.treeSearch(currentNode.getValue(), searchBox.getText());
        });

        setLeft(buttonsPane);
        setCenter(crumbBarPane);
        setRight(searchPane);

        BorderPane.setMargin(buttonsPane, new Insets(5));
        BorderPane.setMargin(crumbBarPane, new Insets(5));
        BorderPane.setMargin(searchPane, new Insets(5));
    }

    /**
     * Переключение текужей ветки на предыдущую по истории если такая существует.
     */
    private void historyBack() {
        if (!history.isEmpty() && historyIndex > 0) {
            historyIndex--;
            TreeItem<File> item = history.get(historyIndex);
            crumbBar.setSelectedCrumb(item);
            modelOperations.loadFork(item.getValue());
            btnForward.setDisable(false);
        }
        if (historyIndex == 0) btnBack.setDisable(true);
    }

    /**
     * Переключение текужей ветки на следующую по истории если такая существует.
     */
    private void historyForward() {
        if (history.size() > historyIndex + 1) {
            historyIndex++;
            TreeItem<File> item = history.get(historyIndex);
            crumbBar.setSelectedCrumb(item);
            modelOperations.loadFork(item.getValue());
            btnBack.setDisable(false);
        }
        if (historyIndex + 1 == history.size()) btnForward.setDisable(true);
    }

    @Override
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case CHANGE_FORK:
                changeFork(event.getFork());
                break;
        }
    }

    /**
     * Добавить новую ветку в историю и в бар навигации.
     *
     * @param fork - новая ветка.
     */
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

    /**
     * Добавление новой ветки в историю.
     * @param item - новая ветка.
     */
    private void addHistory(TreeItem<File> item) {
        Platform.runLater(() -> {
            crumbBar.setSelectedCrumb(item);
            history.add(historyIndex + 1, item);
            historyIndex++;
            for (int i = history.size() - 1; i > historyIndex; i--) {
                history.remove(i);
            }
            if (historyIndex > 0) btnBack.setDisable(false);
            btnForward.setDisable(true);
        });
    }

}
