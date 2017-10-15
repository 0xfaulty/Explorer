package com.defaulty.explorer.control.rescontrol;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class TypeSetter {

    private static HashMap<String, String> typeHash = new HashMap<>();

    public static String getFileType(File file) {
        String ext = FilenameUtils.getExtension(file.getPath());
        if (ext.equals("")){
            if(file.isDirectory()){
                if(file.getName().equals(""))
                    ext = "rootFolderExt";
                else
                    ext = "directoryExt";
            }
        }

        String s = typeHash.get(ext);
        if (s == null) {
            String type = new JFileChooser().getTypeDescription(file);
            typeHash.put(ext, type);
            return type;
        } else return s;
    }
}
