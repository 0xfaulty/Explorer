package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.model.tree.ModelOperations;
import com.defaulty.explorer.panels.center.ViewType;

/**
 * Контракт описывающий объект управляющий всеми включенными в него
 * классами наблюдателями, использующие контракт {@code ViewObserver}
 * по средством которого описываемый класс отправляет наблюдателям события системы.
 */
public interface ViewConnector {

    /**
     * Добавить класс поддерживающий контракт {@code ViewObserver} в список наблюдателей.
     *
     * @param outlet - класс наблюдатель.
     */
    void register(ViewObserver outlet);

    /**
     * Вернуть класс предоставляющий контракт {@code ModelOperations}
     * в котором описываются методы взаимодействия и операции с
     * элементами.
     *
     * @return - класс {@code ModelOperations}.
     */
    ModelOperations getModelCRUD();

    /**
     * Вызов изменения стилей (тем).
     *
     * @param t - новый стиль.
     */
    void changeTheme(ThemeType t);

    /**
     * Переключения вида представления наблюдателем файловой системы.
     *
     * @param t - вид представления {@code ViewType}.
     */
    void changeRightView(ViewType t);

}
