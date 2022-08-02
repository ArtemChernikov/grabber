package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Класс описывает работу с планировщиком, чтение и запись данных с сайта в БД
 *
 * @author ARTEM CHERNIKOV
 * @version 1.1
 */
public class Grabber implements Grab {
    private final Properties cfg = new Properties();

    /**
     * Метод используется соединения с БД
     *
     * @return - возвращает {@link Store}
     */
    public Store store() {
        return new PsqlStore(cfg);
    }

    /**
     * Метод используется для работы планировщика
     *
     * @return - возвращает {@link Scheduler}
     * @throws SchedulerException - может выбросить исключение {@link SchedulerException}
     */
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * Метод используется для чтения файла с настройками
     *
     * @throws IOException - может выбросить исключение {@link IOException}
     */
    public void cfg() throws IOException {
        try (InputStream in = Grabber.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        }
    }

    /**
     * Метод используется для инициализации задания планировщика
     *
     * @param parse     - объект {@link Parse}
     * @param store     - объект {@link Store}
     * @param scheduler - объект {@link Scheduler}
     * @throws SchedulerException - может выбросить исключение {@link SchedulerException}
     */
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {
        /**
         * Метод используется для выполнения задания
         * 1) Собираем все вакансии в список
         * 2) Добавляем каждую вакансию в БД
         *
         * @param context - контекст
         * @throws JobExecutionException - может выбросить исключение {@link JobExecutionException}
         */
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            try {
                List<Post> rsl = parse.list("https://career.habr.com/vacancies/java_developer?page=");
                rsl.forEach(store::save);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод используется для передачи вакансий в строковом представлении в браузер
     * при переходе по ссылке "http://localhost:9000 "
     *
     * @param store - объект {@link Store}
     */
    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {
                            out.write(post.toString().getBytes("Windows-1251"));
                            out.write(System.lineSeparator().getBytes());
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new HabrCareerParse(new HabrCareerDateTimeParser()), store, scheduler);
        grab.web(store);
    }
}
