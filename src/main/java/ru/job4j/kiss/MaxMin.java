package ru.job4j.kiss;

import java.util.Comparator;
import java.util.List;

/**
 * Класс описывает модель поиска максимального и минимального элемента
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class MaxMin {
    public <T> T max(List<T> value, Comparator<T> comparator) {
        return search(value, comparator.reversed());
    }

    public <T> T min(List<T> value, Comparator<T> comparator) {
        return search(value, comparator);
    }

    private <T> T search(List<T> value, Comparator<T> comparator) {
        T rsl = null;
        if (!value.isEmpty()) {
            rsl = value.get(0);
            for (T element : value) {
                if (comparator.compare(rsl, element) > 0) {
                    rsl = element;
                }
            }
        }
        return rsl;
    }
}
