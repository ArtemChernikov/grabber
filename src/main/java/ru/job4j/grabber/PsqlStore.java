package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Класс описывает модель для работы с БД со свойством <b>cnn</b>
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class PsqlStore implements Store, AutoCloseable {
    /**
     * Поле соединение с БД
     */
    private Connection cnn;

    /**
     * Конструктор, для корректного создания объекта требуется загруженный объект {@link Properties}
     * с данными для подключения к БД
     *
     * @param cfg - принимает параметр {@link Properties}
     */
    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Метод используется для сохранения вакансии {@link Post} в БД
     *
     * @param post - вакансия {@link Post}
     */
    @Override
    public void save(Post post) {
        try (PreparedStatement ps = cnn.prepareStatement("INSERT INTO posts (name, text, link, created) "
                + "VALUES (?, ?, ?, ?) ON CONFLICT (link) DO NOTHING;")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод используется для загрузки из БД всех вакансий {@link Post} в список
     *
     * @return - возвращает список {@link List<Post>}
     */
    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement ps = cnn.prepareStatement("SELECT * FROM posts;")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                list.add(setPost(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Метод используется для поиска вакансии {@link Post} в БД по id
     *
     * @param id - идентификатор вакансии
     * @return - возвращает вакансию {@link Post}
     */
    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement ps = cnn.prepareStatement("SELECT * FROM posts WHERE id = ?;")) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                post = setPost(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Метод используется для закрытия соединения {@link Connection} с БД
     *
     * @throws Exception - может выбросить исключение {@link Exception}
     */
    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    /**
     * Метод используется для создания объекта POJO {@link Post} из БД
     * с помощью {@link ResultSet}
     *
     * @param resultSet - объекты из БД {@link ResultSet}
     * @return - возвращает вакансию {@link Post}
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private Post setPost(ResultSet resultSet) throws SQLException {
        return new Post(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("link"),
                resultSet.getString("text"),
                resultSet.getTimestamp("created").toLocalDateTime());
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PsqlStore psqlStore = new PsqlStore(properties);
        psqlStore.save(new Post("test_name1", "test_link1", "test_description1", LocalDateTime.now()));
        psqlStore.save(new Post("test_name2", "test_link2", "test_description2", LocalDateTime.now()));
        psqlStore.save(new Post("test_name3", "test_link3", "test_description3", LocalDateTime.now()));
        System.out.println(psqlStore.getAll());
        System.out.println();
        System.out.println(psqlStore.findById(3));
    }
}
