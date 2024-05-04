-- --liquibase formatted sql

--changeset artem:1
create table users
(
    id           bigserial primary key,
    first_name   text        not null,
    last_name    text        not null,
    email        text unique not null,
    phone_number text unique,
    birth_date   date        not null
);
