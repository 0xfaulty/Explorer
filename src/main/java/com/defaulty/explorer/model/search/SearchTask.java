package com.defaulty.explorer.model.search;

/**
 * Интерфейс определяющий контракты класса задачи поиска.
 */
public interface SearchTask extends Runnable {
    /**
     * Получить число найденных элементов.
     *
     * @return - число.
     */
    int getDoneCount();

    /**
     * Получить ключевое слово поиска задачи.
     *
     * @return - слово.
     */
    String getKeyWord();

    /**
     * Остановить задачу поиска.
     */
    void stop();

    /**
     * Проверка задачи на завершенность.
     *
     * @return - завершёна ли задача.
     */
    boolean isFinished();

    /**
     * Добавить метод вызываемый при событии увеличения счётчика результатов поиска.
     *
     * @param counterInput - необходимый для выхова Runnable мотод.
     */
    void addCountListener(Runnable counterInput);

}
