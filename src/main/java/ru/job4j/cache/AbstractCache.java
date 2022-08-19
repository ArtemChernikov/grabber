package ru.job4j.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс описывает абстрактную модель кэша с помощью {@link SoftReference}
 * (Класс реализован как абстрактный для возможности получения различных вариаций кэша)
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public abstract class AbstractCache<K, V> {
    /**
     * Поле хранилище кэша
     */
    protected final Map<K, SoftReference<V>> cache = new HashMap<>();

    /**
     * Метод используется для добавления значения в хранилище
     *
     * @param key   - ключ
     * @param value - значение в обертке {@link SoftReference}
     */
    public void put(K key, V value) {
        cache.put(key, new SoftReference<>(value));
    }

    /**
     * Метод используется для получения значения по ключу из хранилища
     * (Если значение по ключу равно null, то заново загружаем объект
     * в кэш с помощью метода {@link DirFileCache#load(String)})
     *
     * @param key - ключ
     * @return - возвращает значение
     */
    public V get(K key) {
        V rsl = cache.getOrDefault(key, new SoftReference<>(null)).get();
        if (rsl == null) {
            rsl = load(key);
            put(key, rsl);
        }
        return rsl;
    }

    /**
     * Абстрактный метод для загрузки значения в кэш
     *
     * @param key - ключ
     * @return - возвращает загруженное значение
     */
    protected abstract V load(K key);
}
