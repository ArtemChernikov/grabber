package ru.job4j.gc.prof;

import java.util.Random;

/**
 * Класс используется для создания массива с рандомными значениями
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class RandomArray implements Data {
    public int[] array;
    private Random random;

    public RandomArray(Random random) {
        this.random = random;
    }

    @Override
    public void insert(int elements) {
        array = new int[elements];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(elements - 1) + 1;
        }
    }

    @Override
    public int[] getClone() {
        return array.clone();
    }
}
