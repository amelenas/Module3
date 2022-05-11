DROP DATABASE IF EXISTS test;
CREATE DATABASE IF NOT EXISTS test;

USE test;

create table gift_certificates
(
    id               int auto_increment
        primary key,
    name             varchar(100) null,
    description      varchar(100) null,
    price            double       null,
    duration         int          null,
    create_date      varchar(45)  null,
    last_update_date varchar(45)  null,
    lock_certificate int          null
);

create table tags
(
    id       int auto_increment
        primary key,
    name     varchar(100) null,
    lock_tag int          null
);

create table gift_certificate_tags
(
    gift_certificate_id int not null,
    tag_id              int not null,
    primary key (gift_certificate_id, tag_id),
    constraint fk_gift_certificate_has_tag_gift_certificate1
        foreign key (gift_certificate_id) references gift_certificates (id)
            on update cascade on delete cascade,
    constraint fk_gift_certificate_has_tag_tag
        foreign key (tag_id) references tags (id)
            on update cascade on delete cascade
);

create index fk_gift_certificate_has_tag_gift_certificate_idx
    on gift_certificate_tags (gift_certificate_id);

create index fk_gift_certificate_has_tag_tag1_idx
    on gift_certificate_tags (tag_id);

create table users
(
    user_id   int auto_increment
        primary key,
    user_name varchar(200) null,
    lock_user int          null
);

create table users_orders
(
    order_id       int auto_increment
        primary key,
    cost           double      null,
    buy_date       datetime(6) null,
    certificate_id int         null,
    user_id        int         null,
    lock_order     int         null
);

INSERT INTO gift_certificates
(id,
 name,
 description,
 price,
 duration,
 create_date,
 last_update_date,
 lock_certificate)
VALUES (1, 'Cosmetics', 'Cosmetics description', 100.0, 12, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656', 0),
       (2, 'Music store', 'Music store description', 100.0, 12, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656', 0),
       (3, 'Fitness', 'Fitness description', 250.0, 3, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656', 0),
       (4, 'Sushi', 'Sushi store description', 50.0, 3, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656', 0),
       (5, 'Pizza', 'Pizza store description', 30.0, 3, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656', 0),
       (6, 'Music store', 'Music store description', 100.0, 12, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656', 0);

INSERT INTO tags (id, name, lock_tag)
values (1, 'Cosmetics', 0),
       (2, 'Music store', 0),
       (3, 'Fitness', 0),
       (4, 'Food', 0);

INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
values (1, 1),
       (2, 2),
       (6, 1),
       (3, 3),
       (4, 4),
       (5, 4);

INSERT INTO users (user_id, user_name, lock_user)
values (1, 'Valentin', 0),
       (2, 'Victor', 0),
       (3, 'Laura', 0),
       (4, 'Denchik', 0),
       (5, 'Stepan', 0);

INSERT INTO users_orders (order_id, cost, buy_date, certificate_id, user_id, lock_order)
values (1, 12, '2022-04-04 16:00:31.125656', 1, 1, 0),
       (2, 1, '2022-04-04 16:00:31.125656', 2, 2, 0),
       (3, 1200, '2022-04-04 16:00:31.125656', 3, 3, 0),
       (4, 1200, '2022-04-04 16:00:31.125656', 1, 1, 0),
       (5, 126, '2022-04-04 16:00:31.125656', 3, 5, 0)
