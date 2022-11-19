
/*Drop table USERS_FRIENDS;
Drop table FILMS_LIKES;
Drop table USERS;
Drop table FILMS_GENRE;
Drop table GENRE_NAME;
Drop table FILMS;
Drop table FILMS_RATING;*/




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
