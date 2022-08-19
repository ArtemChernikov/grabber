package ru.job4j.cache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Класс описывает модель для загрузки содержимого txt файлов в кэш
 * Класс наследуется от {@link AbstractCache}
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class DirFileCache extends AbstractCache<String, String> {
    /**
     * Поле путь к директории с файлами
     */
    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    /**
     * Метод используется для получения содержимого файла
     *
     * @param key - ключ
     * @return - возвращает содержимое файла в виде строки
     */
    @Override
    protected String load(String key) {
        StringBuilder value = new StringBuilder();
        try {
            value.append(Files.readString(Path.of(String.format("%s%s%s", cachingDir, File.separator, key))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value.toString();
    }
}
