#!/bin/bash

echo  "<<<<<Запускаю проект>>>>>"

./gradlew clean
./gradlew build

sudo docker compose up -d --build

if [ $? -eq 0 ]; then
    echo "Контейнеры успешно запущены. Открываю браузер..."
else
    echo "Ошибка запуска контейнеров."
fi