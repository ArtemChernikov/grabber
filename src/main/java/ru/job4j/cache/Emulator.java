package ru.job4j.cache;

import java.io.File;
import java.util.Scanner;

/**
 * Класс описывает модель приложения клиента
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class Emulator {
    /**
     * Поле меню
     */
    public static final String MENU = """
                1. Выбрать другой файл.
                2. Загрузить содержимое файла в кэш.
                3. Получить содержимое файла из кэша.
                4. Введите любой символ для выхода из программы.
            """;
    public static final String ENTER_DIRECTORY_PATH = "Введите путь к директории с кэшем: ";
    public static final String ENTER_FILENAME = "Введите имя файла: ";
    public static final String FILE_IS_LOAD = "Содержимое файла загружено.";
    public static final String FILE_CONTENT = "Содержимое файла %s: %n";
    /**
     * Поле пункт меню
     */
    public static final int CHANGE_FILE = 1;
    public static final int LOAD_FILE = 2;
    public static final int GET_CACHE = 3;
    /**
     * Поле путь к директории с файлами
     */
    private String pathDir;
    /**
     * Поле имя файла
     */
    private String fileName;

    public String getPathDir() {
        return pathDir;
    }

    public void setPathDir(String pathDir) {
        this.pathDir = pathDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Метод используется для валидации имени файла
     * (проверяет существует такой файл или нет)
     *
     * @param name - имя файла
     * @return - возвращает имя файла в необходимом формате
     */
    public String validateFileName(String name) {
        String fullName = String.format("%s%s%s%s", pathDir, File.separator, name, ".txt");
        if (!new File(fullName).exists()) {
            throw new IllegalArgumentException("Такого файла не существует!");
        }
        return String.format("%s%s", name, ".txt");
    }

    /**
     * Метод используется для валидации имени каталога с файлами
     * (проверяет существует такой каталог или нет)
     *
     * @param path - путь к каталогу
     * @return - возвращает путь к каталогу, если он существует
     */
    private String validateCacheDir(String path) {
        if (!new File(path).exists()) {
            throw new IllegalArgumentException("Такой директории не существует!");
        }
        return path;
    }

    public static void main(String[] args) {
        Emulator emulator = new Emulator();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean run = true;
            System.out.println(ENTER_DIRECTORY_PATH);
            emulator.setPathDir(emulator.validateCacheDir(scanner.nextLine()));
            DirFileCache dirFileCache = new DirFileCache(emulator.getPathDir());
            System.out.println(ENTER_FILENAME);
            emulator.setFileName(emulator.validateFileName(scanner.nextLine()));
            while (run) {
                System.out.println(MENU);
                int rsl = Integer.parseInt(scanner.nextLine());
                switch (rsl) {
                    case CHANGE_FILE -> {
                        System.out.println(ENTER_FILENAME);
                        emulator.setFileName(emulator.validateFileName(scanner.nextLine()));
                    }
                    case LOAD_FILE -> {
                        dirFileCache.put(emulator.fileName, dirFileCache.get(emulator.getFileName()));
                        System.out.println(FILE_IS_LOAD);
                    }
                    case GET_CACHE -> {
                        System.out.printf(FILE_CONTENT, emulator.getFileName());
                        System.out.println(dirFileCache.get(emulator.getFileName()));
                    }
                    default ->  {
                        run = false;
                    }
                }
            }
        }
    }
}
