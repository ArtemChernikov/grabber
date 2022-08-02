package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывает модель парсинга сайта career.habr.com со свойствами <b>SOURCE_LINK</b>, <b>PAGE_LINK</b> и
 * <b>dateTimeParser</b>
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class HabrCareerParse implements Parse {
    /**
     * Поле корневая ссылка сайта
     */
    private static final String SOURCE_LINK = "https://career.habr.com";
    /**
     * Поле ссылка сайта с вакансиями java developer
     */
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);
    /**
     * Поле с объектом {@link DateTimeParser} для парсинга даты появления вакансии
     */
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    /**
     * Метод используется для парсинга первых пяти страниц сайта с определенными вакансиями,
     * дальнейшим созданием объектов {@link Post} и собиранием их в коллекцию {@link ArrayList}
     *
     * @param homeLink - ссылка к странице с вакансиями
     * @return - возвращает список
     * @throws IOException - может выбросить {@link IOException}
     */
    @Override
    public List<Post> list(String homeLink) throws IOException {
        List<Post> list = new ArrayList<>();
        int id = 1;
        /* С каждой итерацией открывается следующая страница сайта */
        for (int page = 1; page <= 5; page++) {
            Connection connection = Jsoup.connect(homeLink + "?page=" + page);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            for (Element element : rows) {
                Element titleElement = element.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String dateTime = element.select(".vacancy-card__date").first().child(0).attr("datetime");
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                list.add(new Post(id++, vacancyName, link, retrieveDescription(link), dateTimeParser.parse(dateTime)));
            }
        }
        return list;
    }

    /**
     * Метод используется для сбора описания вакансии
     *
     * @param link - ссылка на вакансию
     * @return - возвращает описание вакансии в виде строки
     */
    private static String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements rows = document.select(".collapsible-description");
        return rows.text();
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        System.out.println(habrCareerParse.list(PAGE_LINK));
    }
}
