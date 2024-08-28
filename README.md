# Проект "Погода"
Пет-проект, выполненный в рамках roadmap Сергея Жукова (https://zhukovsd.github.io/java-backend-learning-course/)

## Интерфейс и функционал
Приложение представляет собой трекер погоды.

Функционал доступен только пользователям, вошедшим в свой аккаунт, в противном случае вам предложится это сделать, либо зарегистрироваться, если существующего аккаунта у вас еще нет.


На странице пользователя есть возможность поиска интересующих локаций по названию и список уже добавленных для отслеживания с их погодной информацией и возможностью удаления.


Результаты поиска выводятся списком до пяти локаций с уточнением страны и координат каждой, а также кнопкой "Добавить" для отслеживания. После добавления происходит переадресация на пользовательскую страницу, либо можно вернуться на нее вручную со страницы поиска.


## Используемые технологии
  <ul>Backend:
  <li>Java</li>
  <li>Maven</li>
  <li>Spring Boot</li>
  <li>Spring Security</li>
  <li>Spring Data JPA</li>
  </ul>
  <ul>DB:
  <li>PostgresQL</li>
  <li>Flyway</li>
  <li>Redis</li>
  </ul>
  <ul>Frontend:
  <li>HTML</li>
  <li>Thymeleaf</li>
  <li>Bootstrap5</li>
  </ul>
  <ul>Test:
  <li>JUnit5</li>
  <li>Mockito</li>
  <li>Testcontainers</li>
  </ul>
  <ul>Deploy:
  <li>Docker</li>
    <li>Docker-compose</li>
  </ul>
  
## Для локального запуска
Необходимо скачать проект, после чего в корне проекта найти файл .env и прописать свои свойства для подключения к базе данных.


Так как приложение обращается к сторонемму API для его корректной работы необходим личный ключ. Получить его можно, зарегистрировавшись на https://openweathermap.org


Свой ключ можно иниициализировать напрямую в /client/OpenWeatherClient, либо создать файл private.properties в ресурсах.


После чего можно произвести запуск командой docker-compose up -d --build.