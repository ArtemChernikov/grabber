package ru.job4j.gc.prof;

import java.util.Random;
import java.util.Scanner;

/**
 * Класс для демонстрации работы сборщиков мусора в visualVM
 * на примере сортировок массива
 * Edit Configuration://
 * - Serial => -XX:+UseSerialGC -Xmx12m -Xms12m -Xlog:gc:log.txt.
 * - Parallel => -XX:+UseParallelGC -Xmx12m -Xms12m -Xlog:gc:log.txt.
 * - G1 => -XX:+UseG1GC -Xmx12m -Xms12m -Xlog:gc:log.txt.
 * - ZGC => -XX:+UseZGC -Xmx25m -Xms25m -Xlog:gc:log.txt.
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class App {
    RandomArray randomArray;

    public void createArray(Random random) {
        randomArray = new RandomArray(random);
    }

    public static void main(String[] args) {
        App app = new App();
        Scanner scanner = new Scanner(System.in);
        String separator = System.lineSeparator();
        boolean run = true;
        while (run) {
            System.out.println("1.Создание массива;"
                    + separator + "2.Сортировка пузырьком;"
                    + separator + "3.Сортировка вставками;"
                    + separator + "4.Сортировка слиянием;"
                    + separator + "5.Выход");
            int rsl = scanner.nextInt();
            switch (rsl) {
                case 1:
                    app.createArray(new Random());
                    System.out.println("Введите размер массива");
                    app.randomArray.insert(scanner.nextInt());
                    break;
                case 2:
                    BubbleSort bubbleSort = new BubbleSort();
                    bubbleSort.sort(app.randomArray);
                    break;
                case 3:
                    InsertSort insertSort = new InsertSort();
                    insertSort.sort(app.randomArray);
                    break;
                case 4:
                    MergeSort mergeSort = new MergeSort();
                    mergeSort.sort(app.randomArray);
                    break;
                default:
                    run = false;
                    scanner.close();
            }
        }
    }
}
