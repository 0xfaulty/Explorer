package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.control.events.ViewEvent;

/**
 * Контракт для наблюдателей необходимый для включение в список
 * наблюдателей класса {@code ViewConnector}.
 */
public interface ViewObserver {
    /**
     * Метод получения наблюдателем нового события.
     *
     * @param event - новое событие.
     */
    void receiveEvent(ViewEvent event);
}
