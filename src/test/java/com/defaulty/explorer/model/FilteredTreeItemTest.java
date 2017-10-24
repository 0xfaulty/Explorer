package com.defaulty.explorer.model;

import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FilteredTreeItemTest {

    @Test
    public void moreThan() throws Exception {
        FolderIcons type = FolderIcons.CLOSE_FOLDER;

        assertTrue(type.moreThan(FolderIcons.LOADABLE_FOLDER));

    }

}