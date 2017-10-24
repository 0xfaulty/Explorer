package com.defaulty.explorer.control.rescontrol.image;

import com.defaulty.explorer.App;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * Класс для ассоциативного пристваивания иконок некоторым типам файлов.
 * Также устанавливает иконки для разного состояния папки.
 */
public class CustomIcons {

    private ImageSizePack imageUnloadDir;
    private ImageSizePack imageOpenDir;
    private ImageSizePack imageCloseDir;
    private ImageSizePack imageLoadDir;

    private ImageSizePack imageTxt;
    private ImageSizePack imageDoc;
    private ImageSizePack imagePpt;

    public CustomIcons() {
        imageUnloadDir = getImageResourcePack("folder_u.png");
        imageOpenDir = getImageResourcePack("folder_o.png");
        imageCloseDir = getImageResourcePack("folder_c.png");
        imageLoadDir = getImageResourcePack("folder_l.png");
        imageTxt = getImageResourcePack("txt.png");
        imageDoc = getImageResourcePack("doc.png");
        imagePpt = getImageResourcePack("ppt.png");
    }

    /**
     * Метод для получения замённой иконки указанного состояния папки.
     *
     * @param type - состояние папки.
     * @return - контейнер размеров ассоциативной иконки или null если
     * такой ассоциации не указано в методе.
     */
    public ImageSizePack getCustomFolderIcon(FolderIcons type) {
        switch (type) {
            case UNLOAD_FOLDER:
                return imageUnloadDir;
            case LOADABLE_FOLDER:
                return imageLoadDir;
            case CLOSE_FOLDER:
                return imageCloseDir;
            case OPEN_FOLDER:
                return imageOpenDir;
        }
        return null;
    }

    /**
     * Метод получающий заменнёную иконку для расширения переданного файла
     * если с типом данного файла есть такая ассоциация.
     *
     * @param file - входной файл.
     * @return - контейнер размеров ассоциативной иконки или null если
     * такой ассоциации не указано в методе.
     */
    public ImageSizePack getCustomFileIcon(File file) {
        String ext = FilenameUtils.getExtension(file.getPath()).toLowerCase();
        if (ext.equals("txt")) return imageTxt;
        if (ext.equals("log")) return imageTxt;
        if (ext.equals("doc")) return imageDoc;
        if (ext.equals("docx")) return imageDoc;
        if (ext.equals("ppt")) return imagePpt;
        return null;
    }

    /**
     * Метод получения иконок из ресурсов приложения.
     * Каждая иконка храниться в контейнере {@code ImageSizePack}
     * в трех видах.
     *
     * @param name - имя иконки в ресурсах.
     * @return - контейнер размеров иконки.
     */
    private ImageSizePack getImageResourcePack(String name) {
        ImageSizePack pack = new ImageSizePack();
        pack.setBig(getImageResource("icons/big/" + name, 100, 100));
        pack.setMedium(getImageResource("icons/big/" + name, 50, 50));
        pack.setSmall(getImageResource("icons/small/" + name, 20, 20));

        return pack;
    }

    /**
     * Получение иконки из русурсов приложения по указанному пути.
     *
     * @param patch  - путь к иконке.
     * @param width  - требуемуя ширина.
     * @param height - требуемая высота.
     * @return - изображение.
     */
    private Image getImageResource(String patch, int width, int height) {
        Image img = null;
        try {
            img = new Image(App.class.getClassLoader().getResourceAsStream(patch),
                    width, height, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

}