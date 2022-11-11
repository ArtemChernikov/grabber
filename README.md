# Агрегатор Java Вакансий.

## О проекте

Система парсинга вакансий Java с сайта career.habr.com, запускается по расписанию - раз в минуту. 
Программа считывает все вакансии относящиеся к Java и записывает их в базу данных. 
Доступ к интерфейсу через REST API.

## Расширение.

1. В проект можно добавить новые сайты без изменения кода.
2. В проекте можно сделать параллельный парсинг сайтов.

## Технологии.
- Java 17
- PostgreSql
- JDBC
- Quartz-Scheduler
- Jsoup
- Log4j + slf4j
- Maven
- Git

## Сборка и запуск<br>
для сборки `mvn install`<br>
для запуска`java -jar target/job4j_grabber-1.0-jar-with-dependencies.jar`

## Как использовать.<br>
Использовать для поиска Java вакансий на сайтах

## Контакты для связи<br>
<a href="https://t.me/OvercomingJunk" target="blank"><img src="https://img.icons8.com/clouds/50/000000/telegram-app.png"/></a>

