package com.defaulty.explorer.control.rescontrol.image;

import com.defaulty.explorer.model.item.FilteredTreeItem;
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

    /**
     * Класс для взаимодействия с файловой системой.
     */
    private static FileSystemView fsv = FileSystemView.getFileSystemView();

    /**
     * Кеш соответствия файла и его иконки.
     */
    private static Map<File, ImageSizePack> iconCache = new HashMap<>();

    /**
     * Класс получения не стандартных иконок для файлов или папок.
     */
    private static CustomIcons customIcons = new CustomIcons();

    /**
     * Метод для установки в соответствии с переданным типом
     * иконки заданной папке из элемента. Если элемент не содержит
     * значения, будет выброшено NullPointerException.
     *
     * @param file - папкf.
     * @param type - тип иконки.
     */
    public synchronized void setFolderImageView(File file, FolderIcons type) {
        putCacheImageIcon(file, customIcons.getCustomFolderIcon(type));
    }

    /**
     * Добавления в кэш соответствия файла и контейнера иконок.
     *
     * @param file - файл.
     * @param pack - контейнер иконок.
     */
    private void putCacheImageIcon(File file, ImageSizePack pack) {
        if (file != null) iconCache.put(file, pack);
    }

    /**
     * Метод получения контейнера иконок для файла, вызываемый при
     * отсутствии такового в кэше.
     *
     * @param file - файл или папка.
     * @return - контейнер иконок.
     */
    private ImageSizePack getCustomImageView(File file) {
        if (file.isDirectory()) {
            return customIcons.getCustomFolderIcon(FolderIcons.UNLOAD_FOLDER);
        } else {
            ImageSizePack pack = customIcons.getCustomFileIcon(file);
            if (pack == null) pack = getFileIcon(file);
            return pack;
        }
    }

    /**
     * Получение иконки заданного размера для файла или папки.
     *
     * @param file - входной файл или папка.
     * @param size - размер.
     * @return - иконка.
     */
    public ImageView getImageView(File file, ImageSizePack.ImageSize size) {
        ImageSizePack pack = iconCache.get(file);
        if (pack == null) {
            pack = getCustomImageView(file);
            putCacheImageIcon(file, pack);
        }
        return new ImageView(pack.getImage(size));
    }

    /**
     * Получение иконки заданного размера для файла или папки
     * содержащегося в элементе дерева.
     *
     * @param item - входной элемент.
     * @param size - размер.
     * @return - иконка.
     */
    public ImageView getImageView(TreeItem<File> item, ImageSizePack.ImageSize size) {
        if (item != null) {
            ImageSizePack pack = iconCache.get(item.getValue());
            if (pack == null) {
                if (item instanceof FilteredTreeItem) {
                    FilteredTreeItem fti = (FilteredTreeItem) item;
                    if (!fti.getIconType().moreThan(FolderIcons.LOADABLE_FOLDER)) {
                        pack = customIcons.getCustomFolderIcon(FolderIcons.LOADABLE_FOLDER);
                    }
                }
                if (pack == null) {
                    if (item.isExpanded())
                        pack = customIcons.getCustomFolderIcon(FolderIcons.OPEN_FOLDER);
                    else
                        pack = getCustomImageView(item.getValue());
                }
            }
            return new ImageView(pack.getImage(size));
        } else
            throw new NullPointerException();
    }

    /**
     * Получение контейнера иконок файла с помощью класса взаимодействия с
     * файловой системой.
     *
     * @param file - входной файл.
     * @return - контейнер иконок.
     */
    private ImageSizePack getFileIcon(File file) {
        javax.swing.Icon icon = fsv.getSystemIcon(file);

        ImageSizePack pack = new ImageSizePack();
        pack.setSmall(getFileIconImage(icon, 20, 20));
        pack.setMedium(getFileIconImage(icon, 50, 50));
        pack.setBig(getFileIconImage(icon, 100, 100));

        return pack;
    }

    /**
     * Преобразование типа и размера изображения.
     *
     * @param icon   - изображение типа  {@code javax.swing.Icon}.
     * @param width  - ширина изображения.
     * @param height - высота  изображения.
     * @return - изображение типа  {@code javafx.scene.image.Image}.
     */
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

    /**
     * Преобразование типов изображения.
     *
     * @param image - изображение типа  {@code java.awt.Image}.
     * @return - изображение типа в {@code BufferedImage}.
     */
    private BufferedImage imageToBufferedImage(java.awt.Image image) {
        BufferedImage bi = new BufferedImage
                (image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(image, 0, 0, null);
        bg.dispose();
        return bi;
    }

}
