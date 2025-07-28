#!/bin/bash

echo "<<<<<Запуск dev режима>>>>>"

./gradlew clean
./gradlew bootRun -Dspring.profiles.active=dev

echo "<<<<<Проект остановлен>>>>>"