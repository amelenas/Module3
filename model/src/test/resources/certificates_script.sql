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
    last_update_date varchar(45)  null
);

create table role
(
    role_id   int auto_increment
        primary key,
    roleType  varchar(20)  null,
    name_role varchar(255) null
);

create table tags
(
    id   int auto_increment
        primary key,
    name varchar(100) null
);

create table gift_certificate_tags
(
    gift_certificate_id int not null,
    tag_id              int not null,
    primary key (gift_certificate_id, tag_id),
    constraint fk_gift_certificate_has_tag_gift_certificate
        foreign key (gift_certificate_id) references gift_certificates (id)
            on update cascade on delete cascade,
    constraint fk_gift_certificate_has_tag_tag1
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
    login     varchar(200) null,
    password  varchar(200) null,
    role_id   int          null,
    constraint users_login_uindex
        unique (login),
    constraint FK4qu1gr772nnf6ve5af002rwya
        foreign key (role_id) references role (role_id)
            on update cascade on delete cascade
);

create table users_orders
(
    order_id       int auto_increment
        primary key,
    cost           double      null,
    buy_date       datetime(6) null,
    certificate_id int         null,
    user_id        int         null
);

create index users_orders_user_id_index
    on users_orders (user_id);
INSERT INTO gift_certificates
(id,
 name,
 description,
 price,
 duration,
 create_date,
 last_update_date)
VALUES (1, 'Cosmetics', 'Cosmetics description', 100.0, 12, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656'),
       (2, 'Music store', 'Music store description', 100.0, 12, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656'),
       (3, 'Fitness', 'Fitness description', 250.0, 3, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656'),
       (4, 'Sushi', 'Sushi store description', 50.0, 3, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656'),
       (5, 'Pizza', 'Pizza store description', 30.0, 3, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656'),
       (6, 'Music store', 'Music store description', 100.0, 12, '2022-04-04 16:00:31.125656', '2022-04-04 16:00:31.125656');

INSERT INTO tags (id, name)
values (1, 'Cosmetics'),
       (2, 'Music store'),
       (3, 'Fitness'),
       (4, 'Food');

INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
values (1, 1),
       (2, 2),
       (6, 1),
       (3, 3),
       (4, 4),
       (5, 4);
INSERT INTO role (role_id, name_role, roleType) VALUES (1, 'USER', 'USER'), (2, 'ADMIN', 'ADMIN');

INSERT INTO users (user_id, user_name, login, password, role_id)
values (1, 'Valentin', 'Valentin', '12345',1),
       (2, 'Victor', 'Victor', '12345',1),
       (3, 'Laura', 'Laura', '12345',1),
       (4, 'Denchik', 'Denchik', '12345',1),
       (5, 'Stepan', 'Stepan', '12345',1);

INSERT INTO users_orders (order_id, cost, buy_date, certificate_id, user_id)
values (1, 12, '2022-04-04 16:00:31.125656', 1, 1),
       (2, 1, '2022-04-04 16:00:31.125656', 2, 2),
       (3, 1200, '2022-04-04 16:00:31.125656', 3, 3),
       (4, 1200, '2022-04-04 16:00:31.125656', 1, 1),
       (5, 126, '2022-04-04 16:00:31.125656', 3, 5);




