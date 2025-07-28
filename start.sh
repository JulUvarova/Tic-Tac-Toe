#!/bin/bash

echo  "<<<<<Запускаю проект>>>>>"

./gradlew clean
./gradlew build

sudo docker compose up -d --build

if [ $? -eq 0 ]; then
    echo "Контейнеры успешно запущены. Открывай localhost:8080"
    echo "Подключение к БД:  jdbc:postgresql://localhost:5433/tic-tac-toe-db"
else
    echo "Ошибка запуска контейнеров."
fi