#!/bin/bash

echo "<<<<<Запуск dev режима>>>>>"

./gradlew bootRun -Dspring.profiles.active=dev

echo "<<<<<Проект остановлен>>>>>"