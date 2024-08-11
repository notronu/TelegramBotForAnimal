  -- liquibase formatted sql
  -- changeset jrembo:1
  CREATE TABLE client
(
    id              BIGSERIAL primary key,
    chat_id         BIGSERIAL,
    first_name      VARCHAR,
    last_name       VARCHAR,
    phone_number    VARCHAR
);