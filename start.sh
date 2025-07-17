#!/bin/bash

echo  "<<<<<Запускаю проект>>>>>"

sudo docker compose up -d --build

if [ $? -eq 0 ]; then
    echo "Контейнеры успешно запущены. Открываю браузер..."

    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        xdg-open 'http://localhost:8080/'
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        open 'http://localhost:8080/'
    else
        echo "Не определена система. Откройте http://localhost:8080/ в браузере вручную."
    fi
else
    echo "Ошибка запуска контейнеров."
fi