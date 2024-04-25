-- --liquibase formatted sql

--changeset artem:1
alter table users
    add column address text
