package com.defaulty.explorer.control;

import com.defaulty.explorer.App;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.io.FilenameUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class IconSetter {

    private static FileSystemView fsv = FileSystemView.getFileSystemView();
    private static Map<String, ImageView> iconCache = new HashMap<>();

    private static Image imageOpenDir = getImageResource("icons/openf.png");
    private static Image imageCloseDir = getImageResource("icons/closef.png");
    private static Image imageTxt = getImageResource("icons/txt_s.png");
    private static Image imageDoc = getImageResource("icons/doc_s.png");

    public static ImageView getImageView(File file) {
        if (file.isDirectory()) {
            return new ImageView(imageCloseDir);
        } else {
            ImageView imageView = getCustomIcon(file);
            if (imageView == null) {
                imageView = iconCache.get(file.getName());
                if (imageView == null) {
                    imageView = getFileIcon(file);
                    iconCache.put(file.getName(), imageView);
                }
            }
            return imageView;
        }
    }

    private static ImageView getCustomIcon(File file) {
        String ext = FilenameUtils.getExtension(file.getPath());
        if (ext.equals("txt")) return new ImageView(imageTxt);
        if (ext.equals("log")) return new ImageView(imageTxt);
        if (ext.equals("doc")) return new ImageView(imageDoc);
        if (ext.equals("docx")) return new ImageView(imageDoc);
        return null;
    }

    public static ImageView getImageView(TreeItem<File> item) {
        if (item.isExpanded())
            return new ImageView(imageOpenDir);
        else
            return getImageView(item.getValue());
    }

    private static Image getImageResource(String name) {
        Image img = null;
        try {
            img = new Image(App.class.getClassLoader().getResourceAsStream(name),
                    20, 20, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    private static ImageView getFileIcon(File file) {
        javax.swing.Icon icon = fsv.getSystemIcon(file);

        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

        return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
    }

}
