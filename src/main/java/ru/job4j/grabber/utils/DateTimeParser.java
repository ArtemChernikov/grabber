package ru.job4j.grabber.utils;

import java.time.LocalDateTime;

/**
 * Реализации этого интерфейса будут использоваться для преобразования даты и времени
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public interface DateTimeParser {
    LocalDateTime parse(String parse);
}
