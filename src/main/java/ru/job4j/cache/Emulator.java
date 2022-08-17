package ru.job4j.cache;

import java.util.Scanner;

public class Emulator {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean run = true;
            System.out.println("Введите путь к директории с кэшем: ");
            DirFileCache dirFileCache = new DirFileCache(scanner.nextLine());
            System.out.println("Введите имя файла: ");
            String fileName = dirFileCache.validateFileName(scanner.nextLine());
            while (run) {
                System.out.printf("%s%n%s%n%s%n%s%n",
                        "1. Выбрать другой файл.",
                        "2. Загрузить содержимое файла в кэш.",
                        "3. Получить содержимое файла из кэша.",
                        "4. Введите любой символ для выхода из программы.");
                int rsl = Integer.parseInt(scanner.nextLine());
                switch (rsl) {
                    case 1:
                        System.out.println("Введите имя файла: ");
                        fileName = dirFileCache.validateFileName(scanner.nextLine());
                        break;
                    case 2:
                        dirFileCache.load(fileName);
                        System.out.println("Содержимое файла загружено.");
                        break;
                    case 3:
                        System.out.printf("Содержимое файла %s: %n", fileName);
                        System.out.println(dirFileCache.get(fileName));
                        break;
                    default:
                        run = false;
                }
            }
        }
    }
}
