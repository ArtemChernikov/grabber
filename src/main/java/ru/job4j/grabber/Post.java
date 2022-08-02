package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс описывает модель поста со свойствами <b>id</b>, <b>title</b>, <b>link</b>,
 * <b>description</b> и <b>created</b>
 *
 * @author ARTEM CHERNIKOV
 * @version 1.0
 */
public class Post {
    /**
     * Поле идентификатор вакансии
     */
    private int id;
    /**
     * Поле название вакансии
     */
    private String title;
    /**
     * Поле ссылка на описание вакансии
     */
    private String link;
    /**
     * Поле описание вакансии
     */
    private String description;
    /**
     * Поле дата создания вакансии
     */
    private LocalDateTime created;

    public Post(int id, String title, String link, String description, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + '}';
    }
}
