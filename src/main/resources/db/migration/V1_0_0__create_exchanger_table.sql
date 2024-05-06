create table exchange_rate
(
    id    serial
        constraint PK_exchange_rate
            primary key,
    code  varchar(3)  not null,
    name  varchar(50) not null,
    rate decimal       not null,
    rate_date  date        not null,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    varchar(50) not null,
    modified_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by   varchar(50) not null,
    constraint exchange_rate_unique_pk2
        unique (code, rate_date)
);

create index exchanger_code_index
    on exchange_rate (code, rate_date);

