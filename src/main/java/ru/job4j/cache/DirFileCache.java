package ru.job4j.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DirFileCache extends AbstractCache<String, String> {
    private final String cachingDir;

    public DirFileCache(String cachingDir) {
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
        return value.toString();
    }
}
