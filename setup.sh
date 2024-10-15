#!/bin/bash

# Скачиваем архив проекта с GitHub
wget https://github.com/username/java-kanban/archive/main.zip

# Распаковываем архив
unzip main.zip

# Переходим в директорию проекта
cd java-kanban-main

# Запускаем скрипт настройки среды
./setup.sh

# Запускаем приложение
./app
