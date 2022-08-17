package ru.job4j.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DirFileCache extends AbstractCache<String, String> {
    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        validateCacheDir(cachingDir);
        this.cachingDir = cachingDir;
    }

    @Override
    protected String load(String key) {
        StringBuilder value = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format("%s%s%s", cachingDir, File.separator, key)))) {
            while ((line = reader.readLine()) != null) {
                value.append(line);
                value.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rsl = value.toString();
        put(key, rsl);
        return rsl;
    }

    public String validateFileName(String name) {
        String fullName = String.format("%s%s%s%s", cachingDir, File.separator, name, ".txt");
        if (!new File(fullName).exists()) {
            throw new IllegalArgumentException("Такого файла не существует!");
        }
        return String.format("%s%s", name, ".txt");
    }

    private void validateCacheDir(String path) {
        if (!new File(path).exists()) {
            throw new IllegalArgumentException("Такой директории не существует!");
        }
    }
}
