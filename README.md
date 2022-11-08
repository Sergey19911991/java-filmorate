# java-filmorate
Template repository for Filmorate project.
 ![](https://github.com/Sergey19911991/java-filmorate/blob/main/Untitled.png)
 
 // получение всех фильмов
SELECT f.name,<br/>
       >f.description,<br/>
       >f.releaseDate,<br/>
       >f.duration,<br/>
       >fr.name_rating,<br/>
       >fg.name_genre<br/>
FROM films AS f<br/>
LEFT OUTER JOIN films_rating AS fr ON f.id_rating=fr.id_rating<br/>
LEFT OUTER JOIN films_genre AS fg ON f.id_genre=fg.id_genre<br/>
GROUP BY f.name<br/>
ORDER BY f.name DESC;<br/>



// получение всех пользователей<br/>
SELECT u.name_user,<br/>
       u.login,<br/>
       u.email,<br/>
       u.birthday,<br/>
       uf.id_user_friend,<br/>
       sf.status<br/>
FROM users AS u<br/>
LEFT OUTER JOIN user_friends AS uf ON u.id_user=uf.id_user<br/>
LEFT OUTER JOIN status_friends AS sf ON uf.id_status=sf.id_status<br/>
GROUP BY u.name_user<br/>
ORDER BY u.name_user DESC;<br/>


//получение фильма по id=1<br/>
SELECT f.name,<br/>
       f.description,<br/>
       f.releaseDate,<br/>
       f.duration,<br/>
       fr.name_rating,<br/>
       fg.name_genre<br/>
FROM films AS f<br/>
LEFT OUTER JOIN films_rating AS fr ON f.id_rating=fr.id_rating<br/>
LEFT OUTER JOIN films_genre AS fg ON f.id_genre=fg.id_genre<br/>
WHERE f.id_film = 1<br/>
GROUP BY f.name<br/>
ORDER BY f.name DESC;<br/>


//получение пользователя по id=1<br/>
SELECT u.name_user,<br/>
       u.login,<br/>
       u.email,<br/>
       u.birthday,<br/>
       uf.id_user,<br/>
       uf.id_user_friend,<br/>
       sf.status<br/>
FROM users AS f<br/>
LEFT OUTER JOIN user_friends AS uf ON u.id_user=uf.id_user<br/>
LEFT OUTER JOIN status_friends AS sf ON uf.id_status=sf.id_status<br/>
WHERE u.id_film = 1<br/>
GROUP BY u.name<br/>
ORDER BY u.name DESC;<br/>


//получение друзей пользователя с id=1<br/>
SELECT u.name_user<br/>
FROM user_friends AS uf<br/>
LEFT OUTER JOIN users AS u ON u.id_user=uf.id_user<br/>
WHERE uf.id_user=1<br/>
      AND uf.id.status ='подтвержденная'<br/>
GROUP BY u.name_user<br/>
ORDER BY u.name_user DESC;<br/>


//список общих друзей пользователя id = 1 и пользователя id = 2<br/>
SELECT u.name_user<br/>
FROM user_friends AS uf<br/>
LEFT OUTER JOIN user_friends AS uf ON u.id_user=uf.id_user<br/>
WHERE  uf.id_user=1 <br/>
       AND uf.id.status ='подтвержденная'<br/>
       AND u.name_user IN (SELECT u.name_user<br/>
                           FROM user_friends AS uf<br/>
                           LEFT OUTER JOIN users AS u ON u.id_user=uf.id_user<br/>
                           WHERE uf.id_user=2<br/>
                           AND uf.id.status ='подтвержденная'<br/>
                           GROUP BY u.name_user<br/>
                           ORDER BY u.name_user DESC<br/>
                           )<br/>        
GROUP BY u.name_user<br/>
ORDER BY u.name_user DESC;<br/>



//список первых 10 фильмов по количеству лайков<br/>
SELECT f.name<br/>
FROM films AS f<br/>
LEFT OUTER JOIN film_likes AS fl ON f.id_film=fl.id_film<br/>
GROUP BY f.name<br/>
ORDER BY COUNT (fl.id_film) DESC;<br/>
LIMIT 10;<br/>
