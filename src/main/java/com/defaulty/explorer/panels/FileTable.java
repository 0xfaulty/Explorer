package com.defaulty.explorer.panels;

import com.defaulty.explorer.App;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.apache.commons.io.IOUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileTable extends BorderPane {

    private static FileSystemView fsv = FileSystemView.getFileSystemView();

    private SimpleDateFormat dateFormat = new SimpleDateFormat();
    private NumberFormat numberFormat = NumberFormat.getIntegerInstance();
    private Map<String, ImageView> iconCache = new HashMap<>();

    private final static int minColumnSize = 100;

    public FileTable() {
        String startDir = System.getProperty("user.dir");
        FileTreeItem root = new FileTreeItem(new File(startDir));

        final TreeTableView<File> treeTableView = new TreeTableView<>();

        //String s = getStringResource("hidden-headers.css");

        //treeTableView.getStylesheets().addAll("css/hidden-headers.css");
        //treeTableView.getStylesheets().addAll(".column-header-background { visibility: hidden; -fx-padding: -1em; }");

        treeTableView.setShowRoot(true);
        treeTableView.setRoot(root);
        root.setExpanded(true);
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        TreeTableColumn<File, FileTreeItem> nameColumn = new TreeTableColumn<>("Имя");

        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>((FileTreeItem) cellData.getValue())
        );

        Image image2 = getImageResource("icons/openf.png");
        Image image3 = getImageResource("icons/closef.png");

        nameColumn.setCellFactory(column -> new TreeTableCell<File, FileTreeItem>() {

            ImageView imageView2 = new ImageView(image2);
            ImageView imageView3 = new ImageView(image3);

            @Override
            protected void updateItem(FileTreeItem item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty || item.getValue() == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    File f = item.getValue();
                    String text = f.getParentFile() == null ? File.separator : f.getName();
                    setText(text);
                    //String style = item.isHidden() && f.getParentFile() != null ? "-fx-accent" : "-fx-text-base-color";
                    String style = "-fx-text-base-color";
                    setStyle("-fx-text-fill: " + style);

                    if (item.isDirectory()) {
                        setGraphic(item.isExpanded() ? imageView2 : imageView3);
                    } else {
                        ImageView imageView = iconCache.get(f.getName());
                        if (imageView == null) {
                            imageView = getFileIcon(f);
                            iconCache.put(f.getName(), imageView);
                        }
                        setGraphic(imageView);
                    }
                }
            }
        });

        nameColumn.setMinWidth(minColumnSize);
        nameColumn.setPrefWidth(300);
        nameColumn.setSortable(false);
        treeTableView.getColumns().add(nameColumn);

        TreeTableColumn<File, String> sizeColumn = new TreeTableColumn<>("Размер");

        sizeColumn.setCellValueFactory(cellData -> {
            FileTreeItem item = ((FileTreeItem) cellData.getValue());
            String s = item.isLeaf() ? readableSize(item.length(), false) : "";
            return new ReadOnlyObjectWrapper<>(s);
        });

        Callback<TreeTableColumn<File, String>, TreeTableCell<File, String>> sizeCellFactory = sizeColumn.getCellFactory();
        sizeColumn.setCellFactory(column -> {
            TreeTableCell<File, String> cell = sizeCellFactory.call(column);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.setPadding(new Insets(0, 8, 0, 0));
            return cell;
        });

        sizeColumn.setMinWidth(minColumnSize);
        sizeColumn.setPrefWidth(100);
        sizeColumn.setSortable(true);
        treeTableView.getColumns().add(sizeColumn);

        TreeTableColumn<File, String> typeColumn = new TreeTableColumn<>("Тип");
        typeColumn.setCellValueFactory(cellData -> {
            FileTreeItem item = (FileTreeItem) cellData.getValue();
            String s = "Тут тип";
            return new ReadOnlyObjectWrapper<>(s);
        });

        typeColumn.setMinWidth(minColumnSize);
        typeColumn.setPrefWidth(130);
        typeColumn.setSortable(true);
        treeTableView.getColumns().add(typeColumn);

        TreeTableColumn<File, String> lastModifiedColumn = new TreeTableColumn<>("Дата");
        lastModifiedColumn.setCellValueFactory(cellData -> {
            FileTreeItem item = (FileTreeItem) cellData.getValue();
            String s = dateFormat.format(new Date(item.lastModified()));
            return new ReadOnlyObjectWrapper<>(s);
        });

        lastModifiedColumn.setMinWidth(minColumnSize);
        lastModifiedColumn.setPrefWidth(130);
        lastModifiedColumn.setSortable(true);
        treeTableView.getColumns().add(lastModifiedColumn);

        TreeTableColumn<File, String> emptyColumn = new TreeTableColumn<>();
        emptyColumn.setSortable(false);
        treeTableView.getColumns().add(emptyColumn);

//        treeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            label.setText(newValue != null ? newValue.getValue().getAbsolutePath() : "");
//        });

        treeTableView.getSelectionModel().selectFirst();

        this.setCenter(treeTableView);
        this.setMinWidth(200);
    }

    private Image getImageResource(String name) {
        Image img = null;
        try {
            img = new Image(App.class.getClassLoader().getResourceAsStream(name),
                    20, 20, true, true);
        } catch (Exception e) {
        }
        return img;
    }

    private ImageView getFileIcon(File file) {
        javax.swing.Icon icon = fsv.getSystemIcon(file);

        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

        return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
    }

    public static String readableSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " Б";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "кМГТПЭ" : "КМГТПЭ").charAt(exp-1) + ""; // + (si ? "" : "и");
        return String.format("%.1f %sБ", bytes / Math.pow(unit, exp), pre);
    }
}
