package com.defaulty.explorer.control.rescontrol.image;

import com.defaulty.explorer.model.FilteredTreeItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageSetter {

    private static FileSystemView fsv = FileSystemView.getFileSystemView();
    private static Map<File, ImageSizePack> iconCache = new HashMap<>();

    private static CustomIcons customIcons = new CustomIcons();

    public void setImageView(File file, CustomIcons.FolderIcons type, ImageSizePack.ImageSize size) {
        setImageViewType(file, type);
    }

    public void setImageView(TreeItem<File> item, CustomIcons.FolderIcons type, ImageSizePack.ImageSize size) {
        if (item != null) {
            setImageViewType(item.getValue(), type);
        } else
            throw new NullPointerException();
    }

    private void setImageViewType(File file, CustomIcons.FolderIcons type) {
        putCacheImageIcon(file, customIcons.getCustomFolderIcon(type));
    }

    private void putCacheImageIcon(File file, ImageSizePack pack) {
        if (file != null) iconCache.put(file, pack);
    }

    private Image getSize(ImageSizePack pack, ImageSizePack.ImageSize size) {
        switch (size) {
            case SMALL:
                return pack.getSmall();
            case MEDIUM:
                return pack.getMedium();
            case BIG:
                return pack.getBig();
        }
        return null;
    }

    private ImageSizePack getCustomImageView(File file) {
        if (file.isDirectory()) {
            return customIcons.getCustomFolderIcon(CustomIcons.FolderIcons.UNLOAD_FOLDER);
        } else {
            ImageSizePack pack = customIcons.getCustomFileIcon(file);
            if (pack == null) pack = getFileIcon(file);
            return pack;
        }
    }

    public ImageView getImageView(File file, ImageSizePack.ImageSize size) {
        ImageSizePack pack = iconCache.get(file);
        if (pack == null) {
            pack = getCustomImageView(file);
            putCacheImageIcon(file, pack);
        }
        return new ImageView(pack.getImage(size));
    }

    public ImageView getImageView(TreeItem<File> item, ImageSizePack.ImageSize size) {
        if (item != null) {
            ImageSizePack pack = iconCache.get(item.getValue());
            if (pack == null) {
                if (item instanceof FilteredTreeItem) {
                    FilteredTreeItem fti = (FilteredTreeItem) item;
                    if (!fti.getIconType().moreThen(FilteredTreeItem.IconType.LOADABLE)) {
                        pack = customIcons.getCustomFolderIcon(CustomIcons.FolderIcons.LOADABLE_FOLDER);
                    }
                }
                if (pack == null) {
                    if (item.isExpanded())
                        pack = customIcons.getCustomFolderIcon(CustomIcons.FolderIcons.OPEN_FOLDER);
                    else
                        pack = getCustomImageView(item.getValue());
                }
            }
            return new ImageView(pack.getImage(size));
        } else
            throw new NullPointerException();
    }

    private ImageSizePack getFileIcon(File file) {
        javax.swing.Icon icon = fsv.getSystemIcon(file);

        ImageSizePack pack = new ImageSizePack();
        pack.setSmall(getFileIconImage(icon, 20, 20));
        pack.setMedium(getFileIconImage(icon, 50, 50));
        pack.setBig(getFileIconImage(icon, 100, 100));

        return pack;
    }

    private Image getFileIconImage(javax.swing.Icon icon, int width, int height) {


        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

        java.awt.Image image = bufferedImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);

        return SwingFXUtils.toFXImage(imageToBufferedImage(image), null);
    }

    private BufferedImage imageToBufferedImage(java.awt.Image im) {
        BufferedImage bi = new BufferedImage
                (im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

}
