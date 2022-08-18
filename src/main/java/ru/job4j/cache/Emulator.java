package ru.job4j.cache;

import java.io.File;
import java.util.Scanner;

public class Emulator {
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
    private String pathDir;
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

    public String validateFileName(String name) {
        String fullName = String.format("%s%s%s%s", pathDir, File.separator, name, ".txt");
        if (!new File(fullName).exists()) {
            throw new IllegalArgumentException("Такого файла не существует!");
        }
        return String.format("%s%s", name, ".txt");
    }

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
                    case 1:
                        System.out.println(ENTER_FILENAME);
                        emulator.setFileName(emulator.validateFileName(scanner.nextLine()));
                        break;
                    case 2:
                        dirFileCache.put(emulator.fileName, dirFileCache.get(emulator.getFileName()));
                        System.out.println(FILE_IS_LOAD);
                        break;
                    case 3:
                        System.out.printf(FILE_CONTENT, emulator.getFileName());
                        System.out.println(dirFileCache.get(emulator.getFileName()));
                        break;
                    default:
                        run = false;
                }
            }
        }
    }


}
