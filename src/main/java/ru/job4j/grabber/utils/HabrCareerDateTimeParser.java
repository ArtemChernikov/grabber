package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class HabrCareerDateTimeParser implements DateTimeParser {
    /**
     * Метод используется для преобразования даты из строки в объект
     *
     * @param parse - строка с датой и временем
     * @return - возвращает {@link LocalDateTime}
     */
    @Override
    public LocalDateTime parse(String parse) {
        return ZonedDateTime.parse(parse).toLocalDateTime();
    }
}
