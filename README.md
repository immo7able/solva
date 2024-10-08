# Solva Technology. Тестовое задание Java Junior Developer 
## Описание

Spring Boot приложение, которое предоставляет функционал микрсервиса для транзакций и лимитов по транзакциям.
Курсы валют обновляются при запуске приложения и каждые 24 часа.
Сервис позволяет совершать транзакции по двум категориям SERVICE и PRODUCT, на каждую категорию устанавливается свой лимит. Также можно менять лимит.
Примерное время выполнения задачи - 3 дня.
## Требования

- **Java 21** или выше
- **Maven 4.0.0** или выше
- **PostgreSQL** 

## Установка и запуск

### 1. Клонирование репозитория

Склонируйте этот репозиторий на ваш локальный компьютер:

```bash
git clone https://github.com/immo7able/solva
cd solva
```
### 2. Сборка проекта
После клонирования репозитория выполните сборку проекта с помощью Maven:

```bash
mvn clean install
```
### 3. Настройка окружения
В файле application.properties измените значения на свои у следующих строк:

```
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/your_db
spring.r2dbc.username=your_username 
spring.r2dbc.password=your_pass
spring.flyway.url=jdbc:postgresql://localhost:5432/your_db
spring.flyway.user=your_username 
spring.flyway.password=your_pass
```

### 4. Запуск приложения
Запустите приложение с помощью Maven:

```bash
mvn spring-boot:run
```
5. Доступ к сервису
После успешного запуска приложение будет доступно по следующему адресу:

```
http://localhost:8080
```

## Тестирование
Чтобы запустить тесты, используйте следующую команду:

```bash
mvn test
```
Swagger/OpenAPI
Сервис включает документацию API, доступную по следующему адресу после запуска:

```bash
http://localhost:8080/swagger-ui.html
```