package com.defaulty.explorer.control.rescontrol.image;

/**
 * Перечисление типов состояния директории
 */
public enum FolderIcons {
    /**
     * Незагруженная директория.
     */
    UNLOAD_FOLDER(0),
    /**
     * Загружаемая директория.
     */
    LOADABLE_FOLDER(1),
    /**
     * Загруженная директория.
     */
    CLOSE_FOLDER(2),
    /**
     * Открытая директория.
     */
    OPEN_FOLDER(3);

    /**
     * Метод сравнения состояний.
     *
     * @param iconType - сравневаемое состояние
     * @return - больше ли заданный тип переданного.
     */
    public boolean moreThan(FolderIcons iconType) {
        return iconType.status < status;
    }

    /**
     * Метод сравнения состояний.
     *
     * @param iconType - сравневаемое состояние
     * @return - меньше ли заданный тип переданного.
     */
    public boolean lessThan(FolderIcons iconType) {
        return iconType.status > status;
    }

    private final int status;

    FolderIcons(int i) {
        this.status = i;
    }
}
