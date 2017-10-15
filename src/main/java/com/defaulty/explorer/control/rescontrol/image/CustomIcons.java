package com.defaulty.explorer.control.rescontrol.image;

import com.defaulty.explorer.App;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

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

    public enum FolderIcons {
        UNLOAD_FOLDER, LOADABLE_FOLDER, CLOSE_FOLDER, OPEN_FOLDER
    }

    public ImageSizePack getCustomFolderIcon(FolderIcons type){
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

    public ImageSizePack getCustomFileIcon(File file) {
        String ext = FilenameUtils.getExtension(file.getPath());
        if (ext.equals("txt")) return imageTxt;
        if (ext.equals("log")) return imageTxt;
        if (ext.equals("doc")) return imageDoc;
        if (ext.equals("docx")) return imageDoc;
        if (ext.equals("ppt")) return imagePpt;
        return null;
    }

    private ImageSizePack getImageResourcePack(String name) {
        ImageSizePack pack = new ImageSizePack();
        pack.setBig(getImageResource("icons/big/" + name, 100, 100));
        pack.setMedium(getImageResource("icons/big/" + name, 50, 50));
        pack.setSmall(getImageResource("icons/small/" + name, 20, 20));

        return pack;
    }

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