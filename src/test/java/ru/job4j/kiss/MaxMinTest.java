package ru.job4j.kiss;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MaxMinTest {

    @Test
    void whenFindMaxElement() {
        MaxMin maxMin = new MaxMin();
        Comparator<String> comparator = Comparator.comparingInt(String::length);
        List<String> list = List.of("where", "zoo-york", "southpole", "russia");
        String expected = maxMin.max(list, comparator);
        assertThat(expected).isEqualTo("southpole");
    }

    @Test
    void whenFindMinElement() {
        MaxMin maxMin = new MaxMin();
        Comparator<String> comparator = Comparator.comparingInt(String::length);
        List<String> list = List.of("where", "zoo-york", "southpole", "russia");
        String expected = maxMin.min(list, comparator);
        assertThat(expected).isEqualTo("where");
    }

    @Test
    void whenListIsEmpty() {
        MaxMin maxMin = new MaxMin();
        Comparator<String> comparator = Comparator.comparingInt(String::length);
        List<String> list = List.of();
        String expected = maxMin.min(list, comparator);
        assertThat(expected).isNull();
    }
}