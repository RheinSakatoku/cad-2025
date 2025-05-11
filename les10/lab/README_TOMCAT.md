# Настройка Tomcat 11 и деплой WAR

## 📦 Сборка WAR
Из корня проекта `lab/app` запустить:
```
./gradlew war
```

Файл появится в:
```
build/libs/app.war
```

## ⚙️ Установка и настройка Apache Tomcat 11

1. Скачай с https://tomcat.apache.org/
2. Распакуй и перейди в директорию Tomcat
3. Отредактируй файл `conf/tomcat-users.xml`, добавив:

```xml
<tomcat-users>
  <role rolename="manager-gui"/>
  <role rolename="admin-gui"/>
  <user username="admin" password="admin" roles="manager-gui,admin-gui"/>
</tomcat-users>
```

4. Запусти сервер:
    - **Linux/macOS**: `bin/startup.sh`
    - **Windows**: `bin/startup.bat`

5. Открой браузер:
```
http://localhost:8080
```
Панель управления будет доступна по ссылке **Manager App**

## 🚀 Деплой
Скопируй файл `app.war` в директорию:
```
<TOMCAT_HOME>/webapps/
```
После этого приложение станет доступно по адресу:
```
http://localhost:8080/app/orders
```

## 🧪 Тестирование REST-сервиса

Используй Postman:
```
GET http://localhost:8080/app/api/products
```

Ожидаемый ответ:
```json
[
  {
    "name": "Ноутбук",
    "category": "Электроника",
    "stock": 12
  },
  ...
]
```