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
 * Класс описывает модель парсинга сайта career.habr.com со свойствами <b>SOURCE_LINK</b>, <b>PAGE_LINK</b>,
 * <b>PAGES</b> и <b>dateTimeParser</b>
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
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    /**
     * Поле сколько считываем страниц с вакансиями
     */
    public static final int PAGES = 5;
    /**
     * Поле с объектом {@link DateTimeParser} для парсинга даты появления вакансии
     */
    private final DateTimeParser dateTimeParser;


    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    /**
     * Метод используется для парсинга первых пяти страниц сайта с определенными вакансиями,
     * и создания коллекции с каждой вакансией {@link ArrayList<Post>}
     *
     * @param homeLink - ссылка к странице с вакансиями
     * @return - возвращает список
     * @throws IOException - может выбросить {@link IOException}
     */
    @Override
    public List<Post> list(String homeLink) throws IOException {
        List<Post> list = new ArrayList<>();
        /* С каждой итерацией открывается следующая страница сайта */
        for (int i = 1; i <= PAGES; i++) {
            Connection connection = Jsoup.connect(homeLink + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            for (Element element : rows) {
                list.add(createPost(element));
            }
        }
        return list;
    }

    /**
     * Метод используется для парсинга конкретной вакансии сайта в объект {@link Post}
     *
     * @param element - элемент страницы
     * @return - возвращает объект {@link Post}
     */
    private Post createPost(Element element) {
        Element titleElement = element.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String dateTime = element.select(".vacancy-card__date").first().child(0).attr("datetime");
        String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        return new Post(vacancyName, link, retrieveDescription(link), dateTimeParser.parse(dateTime));
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
