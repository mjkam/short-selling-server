-- DROP DATABASE short_selling;
CREATE DATABASE IF NOT EXISTS short_selling DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE short_selling;

DROP TABLE IF EXISTS company;
CREATE TABLE company
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    company_code    VARCHAR(10)                       NOT NULL,
    name            VARCHAR(200)                      NOT NULL,
    market_type     VARCHAR(100)                      NOT NULL,
    logo_image_name VARCHAR(200)                      NOT NULL,
    UNIQUE (company_code)
);

DROP TABLE IF EXISTS fetch_record;
CREATE TABLE fetch_record
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    stock_record_date DATE                              NOT NULL,
    executed_datetime DATETIME                          NOT NULL,
    UNIQUE (stock_record_date)
);

DROP TABLE IF EXISTS stock_record;
CREATE TABLE stock_record
(
    id                        BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    company_id                BIGINT                            NOT NULL,
    record_date               DATE                              NOT NULL,
    short_selling_share_count BIGINT                            NOT NULL,
    listed_share_count        BIGINT                            NOT NULL,
    short_selling_amount      BIGINT                            NOT NULL,
    listed_share_amount       BIGINT                            NOT NULL,
    short_selling_ratio       FLOAT                             NOT NULL,
    UNIQUE (company_id, record_date)
);

DROP TABLE IF EXISTS favorite_record;
CREATE TABLE favorite_record
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    company_code VARCHAR(10)                       NOT NULL,
    count        INT                               NOT NULL,
    UNIQUE (company_code)
);