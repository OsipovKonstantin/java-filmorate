![image](https://github.com/OsipovKonstantin/java-filmorate/assets/98541812/217fd3c8-f52c-4a51-80f7-a687254316de)
# filmorate - сервис оценки фильмов и получения рекомендаций
[![Java](https://img.shields.io/badge/-Java%2011-F29111?style=for-the-badge&logo=java&logoColor=e38873)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/-Spring%202.7.1-6AAD3D?style=for-the-badge&logo=spring&logoColor=90fd87)](https://spring.io/projects/spring-framework) 
[![H2](https://img.shields.io/badge/-H2-0f1aa3?style=for-the-badge&logo=db&logoColor=FFFFFF)](https://www.postgresql.org/)
[![JDBCtemplate](https://img.shields.io/badge/-JDBC_template-000000?style=for-the-badge&logo=db&logoColor=FFFFFF)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/-Maven-7D2675?style=for-the-badge&logo=apache&logoColor=e38873)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit%205-6CA315?style=for-the-badge&logo=JUnit&logoColor=white)](https://junit.org/junit5/docs/current/user-guide/)
[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)

## Описание
## Архитектура
## Функциональность
## Диаграмма базы данных
![схема БД H2](filmorate_schema_DB.png)
## Как использовать




Написано на момент месяца SQL
# Запросы к базе данных на примере пользователей (users) и их дружбы (friends)
добавление пользователя
```sql
INSERT INTO users(email, login, name, birthday) 
VALUES (?, ?, ?);
```

обновление пользователя
```sql
UPDATE users 
SET email = ?, login = ?, name = ?, birthday = ? 
WHERE user_id = ?
```

получение пользователей
```sql
SELECT * 
FROM users
```

получение пользователя по id
```sql
SELECT * 
FROM users 
WHERE user_id = ?
```

удаление пользователя по id
```sql
DELETE FROM users 
WHERE user_id = ?
```

добавление друга
```sql
INSERT INTO friends (user_id, friend_id, status_id) 
VALUES (?, ?, ?)
```

удаление друга
```sql
DELETE FROM friends 
WHERE user_id = ? 
      AND friend_id = ?
```

получение друзей
```sql
SELECT f.friend_id, s.name 
FROM friends f 
LEFT JOIN status s ON f.status_id = s.status_id
WHERE f.user_id = ?
```

обновление статуса дружбы
```sql
UPDATE friends SET status_id = ? 
WHERE user_id = ? 
      AND friend_id = ?
```
