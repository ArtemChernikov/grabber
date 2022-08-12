package ru.job4j.gc.prof;

/**
 * Класс используется для сортировки массива вставками
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class InsertSort implements Sort {

    @Override
    public boolean sort(Data data) {
        int[] array = data.getClone();
        sort(array);
        return true;
    }

    private void sort(int[] array) {
        int in, out;
        for (out = 1; out < array.length; out++) {
            int temp = array[out];
            in = out;
            while (in > 0 && array[in - 1] >= temp) {
                array[in] = array[in - 1];
                --in;
            }
            array[in] = temp;
        }
    }
}
