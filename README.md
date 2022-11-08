# java-filmorate
Template repository for Filmorate project.
 ![](https://github.com/Sergey19911991/java-filmorate/blob/main/Untitled.png)
 
 // получение всех фильмов
SELECT f.name,<br/>
       f.description,<br/>
       f.releaseDate,<br/>
       f.duration,<br/>
       fr.name_rating,<br/>
       fg.name_genre<br/>
FROM films AS f<br/>
LEFT OUTER JOIN films_rating AS fr ON f.id_rating=fr.id_rating<br/>
LEFT OUTER JOIN films_genre AS fg ON f.id_genre=fg.id_genre<br/>
GROUP BY f.name<br/>
ORDER BY f.name DESC;<br/>



// получение всех пользователей
SELECT u.name_user,
       u.login,
       u.email,
       u.birthday,
       uf.id_user_friend,
       sf.status
FROM users AS u
LEFT OUTER JOIN user_friends AS uf ON u.id_user=uf.id_user
LEFT OUTER JOIN status_friends AS sf ON uf.id_status=sf.id_status
GROUP BY u.name_user
ORDER BY u.name_user DESC;


//получение фильма по id=1
SELECT f.name,
       f.description,
       f.releaseDate,
       f.duration,
       fr.name_rating,
       fg.name_genre
FROM films AS f
LEFT OUTER JOIN films_rating AS fr ON f.id_rating=fr.id_rating
LEFT OUTER JOIN films_genre AS fg ON f.id_genre=fg.id_genre
WHERE f.id_film = 1
GROUP BY f.name
ORDER BY f.name DESC;


//получение пользователя по id=1
SELECT u.name_user,
       u.login,
       u.email,
       u.birthday,
       uf.id_user,
       uf.id_user_friend,
       sf.status
FROM users AS f
LEFT OUTER JOIN user_friends AS uf ON u.id_user=uf.id_user
LEFT OUTER JOIN status_friends AS sf ON uf.id_status=sf.id_status
WHERE u.id_film = 1
GROUP BY u.name
ORDER BY u.name DESC;


//получение друзей пользователя с id=1
SELECT u.name_user
FROM user_friends AS uf
LEFT OUTER JOIN users AS u ON u.id_user=uf.id_user
WHERE uf.id_user=1 
      AND uf.id.status ='подтвержденная'
GROUP BY u.name_user
ORDER BY u.name_user DESC;


//список общих друзей пользователя id = 1 и пользователя id = 2
SELECT u.name_user
FROM user_friends AS uf
LEFT OUTER JOIN user_friends AS uf ON u.id_user=uf.id_user
WHERE  uf.id_user=1 
       AND uf.id.status ='подтвержденная'
       AND u.name_user IN (SELECT u.name_user
                           FROM user_friends AS uf
                           LEFT OUTER JOIN users AS u ON u.id_user=uf.id_user
                           WHERE uf.id_user=2 
                           AND uf.id.status ='подтвержденная'
                           GROUP BY u.name_user
                           ORDER BY u.name_user DESC;                
GROUP BY u.name_user
ORDER BY u.name_user DESC;



//список первых 10 фильмов по количеству лайков
SELECT f.name
FROM films AS f
LEFT OUTER JOIN film_likes AS fl ON f.id_film=fl.id_film
GROUP BY f.name
ORDER BY COUNT (fl.id_film) DESC;
LIMIT 10;
