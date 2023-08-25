# Курсовой проект по модулю «Автоматизация тестирования»
Проект представляет собой автоматизацию тестирования сервиса покупки тура в путешествие. Возможна покупка двумя разными способами. После заполнения формы сервис заносит информацию о покупке тура в собственную БД.

## Документы проекта
- <a href="https://github.com/AzizShoev/DiplomQa58/blob/main/docs/Plan.md">План автоматизации</a>
- <a href="https://github.com/AzizShoev/DiplomQa58/blob/main/docs/Report.md">Отчет о тестировании</a>
- <a href="https://github.com/AzizShoev/DiplomQa58/blob/main/docs/Summary.md">Итоги</a>

## Порядок развертывания проекта и запуска автотестов на локальной машине
1. Сделать клон <a href="https://github.com/AzizShoev/DiplomQa58">Дипломного проекта</a> в локальный репозиторий  и открыть его в IntelliJ IDEA
2. Запустить Docker Desktop
3. В терминале IDEA запустить контейнеры с помощью команды:  
   `docker-compose up`
4. Порядок запуска SUT для разных СУБД через второй терминал:  
   для mySQL:  
   `java -jar .\artifacts\aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app
   `  
   для postgresgl:  
   `java -jar .\artifacts\aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app
   `
5. Открыть третий терминал и ввести команду для запуска тестов в зависимости от СУБД:  
   для mySQL:  
   `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`  
   для postgresgl:  
   `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
6. Для генерации отчета тестирования используется команда:  
   `./gradlew allureserve`
7. Для завершения работы allureServe используется сочетание клавиш:  
   `Ctrl+C`
9. Для остановки работы контейнеров используется команда:  
   `docker-compose down`