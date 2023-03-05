-- DROP DATABASE short_selling;
CREATE DATABASE IF NOT EXISTS short_selling default character set utf8 collate utf8_general_ci;
use short_selling;

CREATE TABLE company
(
    id              bigint AUTO_INCREMENT,
    company_code    varchar(10),
    name            varchar(200),
    market_type     varchar(100),
    logo_image_name varchar(200),
    PRIMARY KEY (id),
    UNIQUE (company_code)
);

DROP TABLE IF EXISTS fetch_record;
CREATE TABLE fetch_record
(
    id                bigint AUTO_INCREMENT,
    stock_record_date date,
    executed_datetime datetime,
    PRIMARY KEY (id),
    UNIQUE (stock_record_date)
);

DROP TABLE IF EXISTS stock_record;
CREATE TABLE stock_record
(
    id                        bigint AUTO_INCREMENT,
    company_id                bigint,
    record_date               date,
    short_selling_share_count bigint,
    listed_share_count        bigint,
    short_selling_amount      bigint,
    listed_share_amount       bigint,
    short_selling_ratio       float,
    PRIMARY KEY (id),
    UNIQUE (company_id, record_date)
);

CREATE TABLE favorite_record
(
    id           bigint AUTO_INCREMENT,
    company_code varchar(10),
    count        int,
    PRIMARY KEY (id),
    UNIQUE (company_code)
);