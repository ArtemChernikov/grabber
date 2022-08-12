package ru.job4j.gc.prof;

/**
 * Класс используется для сортировки массива пузырьком
 * (для сортировки испульзуется копия изначального массива)
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class BubbleSort implements Sort {

    @Override
    public boolean sort(Data data) {
        int[] array = data.getClone();
        sort(array);
        return true;
    }

    private void sort(int[] array) {
        int out, in;
        for (out = array.length - 1; out >= 1; out--) {
            for (in = 0; in < out; in++) {
                if (array[in] > array[in + 1]) {
                    swap(array, in, in + 1);
                }
            }
        }
    }

    public void swap(int[] array, int in, int in1) {
        int temp = array[in];
        array[in] = array[in1];
        array[in1] = temp;
    }
}
