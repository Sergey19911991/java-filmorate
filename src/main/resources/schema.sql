Drop table IF EXISTS USERS_FRIENDS;
Drop table IF EXISTS FILMS_LIKES;
Drop TABLE IF EXISTS REVIEWSBYlIKES;
DROP TABLE IF EXISTS REVIEWS;
DROP TABLE IF EXISTS EVENTS;
-----------------------------------------
Drop table IF EXISTS USERS;
Drop table IF EXISTS FILMS_GENRE;
Drop table IF EXISTS GENRE_NAME;
Drop table IF EXISTS FILMS_DIRECTORS;
Drop table IF EXISTS DIRECTORS;
Drop table IF EXISTS FILMS;
Drop table IF EXISTS FILMS_RATING;

create table IF NOT EXISTS FILMS_RATING
(
    RATING_ID   INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING not null,
    constraint FILMS_RATING_PK
        primary key (RATING_ID)
);

create table IF NOT EXISTS FILMS
(
    FILMS_ID           INTEGER auto_increment,
    FILMS_NAME         CHARACTER VARYING,
    FILMS_DESCRIPTION  CHARACTER VARYING not null,
    FILMS_DURATION     DOUBLE PRECISION  not null,
    FILMS_RELEASE_DATE DATE              not null,
    RATING_ID          INTEGER,
    constraint FILMS_PK
        primary key (FILMS_ID),
    constraint FILMS__RATING_ID_FK
        foreign key (RATING_ID) references FILMS_RATING
);

create table IF NOT EXISTS GENRE_NAME
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING not null,
    constraint GENRE_NAME_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILMS_GENRE
(
    FILMS_ID INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILMS_GENRE_FILMS_FILMS_ID_FK
        foreign key (FILMS_ID) references FILMS,
    constraint FILMS_GENRE_GENRE_NAME_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE_NAME
);

create table IF NOT EXISTS DIRECTORS
(
    DIRECTORS_ID   INTEGER auto_increment,
    DIRECTORS_NAME CHARACTER VARYING,
    constraint DIRECTORS_PK
        primary key (DIRECTORS_ID)
);

create table IF NOT EXISTS FILMS_DIRECTORS
(
    FILMS_ID     INTEGER,
    DIRECTORS_ID INTEGER,
    constraint FILMS_DIRECTORS_DIRECTORS_DIRECTORS_ID_FK
        foreign key (DIRECTORS_ID) references DIRECTORS,
    constraint FILMS_DIRECTORS_FILMS_FILMS_ID_FK
        foreign key (FILMS_ID) references FILMS
);

create table IF NOT EXISTS USERS
(
    USERS_ID        INTEGER auto_increment,
    USERS_NAME      CHARACTER VARYING,
    USERS_LOGIN     CHARACTER VARYING not null,
    USERS_EMAIL     CHARACTER VARYING not null,
    USERS_BIRTHDAY  DATE              not null,
    constraint USERS_PK
        primary key (USERS_ID)
);

create table IF NOT EXISTS FILMS_LIKES
(
    FILMS_ID INTEGER not null,
    USERS_ID INTEGER not null,
    constraint FILMS_LIKES_FILMS_FILMS_ID_FK
        foreign key (FILMS_ID) references FILMS,
    constraint FILMS_LIKES_USERS_USERS_ID_FK
        foreign key (USERS_ID) references USERS
);

create table IF NOT EXISTS USERS_FRIENDS
(
    USERS_ID        INTEGER not null,
    USERS_FRIEND_ID INTEGER not null,
    STATUS          BOOLEAN,
    constraint USERS_FRIENDS_USERS_USERS_ID_FK
        foreign key (USERS_ID) references USERS,
    constraint USERS_FRIENDS_USERS_USERS_ID_FK_2
        foreign key (USERS_FRIEND_ID) references USERS
);

create table IF NOT EXISTS REVIEWS
(
    id          integer      not null primary key,
    content     varchar(500) not null,
    is_positive boolean      not null,
    user_id     integer      not null references users (USERS_ID),
    film_id     integer      not null references films (FILMS_id),
    useful integer default 0
);

CREATE TABLE IF NOT EXISTS REVIEWSBYlIKES
(
    REVIEW_ID INTEGER REFERENCES REVIEWS(ID),
    IS_LIKE   BOOLEAN NOT NULL,
    USER_ID   INTEGER REFERENCES USERS(USERS_ID),
    PRIMARY KEY (REVIEW_ID, IS_LIKE, USER_ID)

);

create table IF NOT EXISTS EVENTS
(
    EVENT_ID        INTEGER auto_increment,
    USERS_ID        INTEGER REFERENCES USERS(USERS_ID),
    TIME_STAMP      TIMESTAMP,
    EVENT_TYPE      VARCHAR(10),
    OPERATION       VARCHAR(10),
    ENTITY_ID       INTEGER,
    constraint EVENTS_PK primary key (EVENT_ID)
    );

