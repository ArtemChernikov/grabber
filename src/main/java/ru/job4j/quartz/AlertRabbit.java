package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 * Класс демонстрирует действия с периодичностью с помощью библиотеки Quartz
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class AlertRabbit {
    public static void main(String[] args) {
        try {
            /* Конфигурирование */
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            /* Создание задачи */
            JobDetail job = newJob(Rabbit.class).build();
            /* Создание расписания */
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(init())
                    .repeatForever();
            /* Задача выполняется через триггер */
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            /* Загрузка задачи в планировщик */
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    /**
     * Метод используется для считывание из файла "rabbit.properties" интервала запуска
     * Используется метод validate()
     *
     * @return - возвращает интервал запуска
     */
    public static int init() {
        int interval = 0;
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            interval = validate(properties.getProperty("rabbit.interval"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return interval;
    }

    /**
     * Метод используется для валидации содержимого файла
     *
     * @param interval - интервал в виде строки
     * @return - возвращает интервал в виде числа
     */
    public static int validate(String interval) {
        if ("".equals(interval)) {
            throw new IllegalArgumentException("Укажите интервал запуска!");
        }
        int rsl = Integer.parseInt(interval);
        if (rsl < 1) {
            throw new IllegalArgumentException("Интервал запуска должен быть не меньше 1!");
        }
        return rsl;
    }

    /**
     * Вложенный статический класс реализующий интерфейс {@link Job},
     * используется для описания требуемых действий с определенной периодичностью
     */
    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}
