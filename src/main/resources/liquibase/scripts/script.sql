  -- liquibase formatted sql
  -- changeset jrembo:1
  CREATE TABLE client
(
    id          BIGSERIAL primary key,
    chat_id     BIGSERIAL,
    last_name   VARCHAR,
    first_name  VARCHAR,
    phone       VARCHAR
);