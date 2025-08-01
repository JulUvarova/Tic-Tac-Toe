# 🎮 Tic-Tac-Toe Game

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![JWT](https://img.shields.io/badge/JWT-Authorization-yellow.svg)](https://jwt.io/)

Учебное веб-приложение для игры в крестики-нолики с минимакс алгоритмом или пользователем, поддержкой JWT авторизации, историей игр и таблицей лидеров.

## 🛠 Технологический стек

### Backend
- **Java 21** - современная версия Java с улучшенной производительностью
- **Spring Boot 3.5.3** - основной фреймворк для создания веб-приложений
- **Spring Security** - безопасность и авторизация
- **Spring Data JPA** - работа с базой данных
- **JWT (JSON Web Tokens)** - токены для аутентификации
- **PostgreSQL** - основная база данных
- **H2 Database** - база данных для разработки и тестирования

### Frontend (в соавторстве с ИИ)
- **Thymeleaf** - серверный шаблонизатор
- **HTML5/CSS3** - современная разметка и стили
- **JavaScript** - интерактивность на стороне клиента
- **Bootstrap** - адаптивный дизайн

### Инфраструктура
- **Docker & Docker Compose** - контейнеризация приложения
- **Gradle** - система сборки проекта
- **Swagger/OpenAPI** - документация API

## 📁 Структура проекта

```
src/
├── main/
│   ├── java/com/trumpecy/tictactoe/
│   │   ├── datasource/          # Слой доступа к данным
│   │   │   ├── mapper/          # Маппинг между слоями
│   │   │   ├── model/           # JPA сущности
│   │   │   └── repository/      # Репозитории для работы с БД
│   │   ├── di/                  # Конфигурация зависимостей
│   │   ├── domain/              # Бизнес-логика
│   │   │   ├── model/           # Доменные модели
│   │   │   └── service/         # Сервисы
│   │   ├── exception/           # Обработка исключений
│   │   ├── security/            # Безопасность
│   │   └── web/                 # Веб-слой
│   │       ├── controller/      # REST контроллеры
│   │       ├── mapper/          # Маппинг DTO
│   │       └── model/           # DTO модели
│   └── resources/
│       ├── static/              # Статические ресурсы
│       ├── templates/           # HTML шаблоны
│       └── application*.properties # Конфигурация
└── test/                        # Тесты
```

## 🚀 Быстрый старт

### Предварительные требования
- Java 21 или выше
- Docker и Docker Compose
- Git

### Запуск с помощью Docker (рекомендуется)

1. **Клонируйте репозиторий**
  
2. **Запустите приложение**
   ```bash
   ./gradlew clean
   ./gradlew build
   sudo docker compose up -d --build
   ```
   или для режима разработки:
   ```bash
   ./gradlew bootRun -Dspring.profiles.active=dev
   ```

3. **Откройте браузер**
   - Приложение: http://localhost:8080
   - База данных: jdbc:postgresql://localhost:5433/tic-tac-toe-db
   - Swagger UI: http://localhost:8080/swagger-ui.html

## 📚 API Документация

### Аутентификация
- `POST /auth/register` - Регистрация нового пользователя
- `POST /auth/login` - Вход в систему
- `POST /auth/token` - Обновление access токена
- `POST /auth/refresh` - Обновление refresh токена
- `GET /auth/me` - Информация о текущем пользователе

### Игры
- `POST /game` - Создание новой игры
- `GET /game/{id}` - Получение игры по ID
- `POST /game/{gameId}` - Сделать ход
- `POST /game/{gameId}/join` - Присоединиться к игре
- `GET /game` - Список доступных игр
- `GET /game/player/{userId}` - Игры пользователя
- `GET /game/player/{userId}/stats` - Статистика пользователя
- `GET /game/leaderboard` - Таблица лидеров

### Пользователи
- `GET /user/{id}` - Информация о пользователе

## 🎮 Как играть

1. **Регистрация/Вход**
   - Зарегистрируйтесь или войдите в систему
   - Получите JWT токен для доступа к игре

2. **Создание игры**
   - Выберите тип противника (компьютер или игрок)
   - Создайте новую игру

3. **Игровой процесс**
   - Делайте ходы, кликая по клеткам
   - Система автоматически определит победителя
   - Игра сохраняется в истории

4. **Статистика**
   - Просматривайте свою статистику
   - Изучайте таблицу лидеров
   - Анализируйте историю игр

## 👨‍💻 Автор

Юлия Уварова  - [Telegram](https://t.me/Jun_Uno)

---

⭐ Если этот проект вам понравился, поставьте звездочку!

