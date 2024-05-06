create table conversion
(
    id serial
        constraint PK_conversion
            primary key,
    from_value decimal,
    to_value decimal,
    from_exchange_id integer,
    to_exchange_id integer,
    rate_date  date        not null,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    varchar(50) not null,
    modified_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by   varchar(50) not null,
    constraint FK_conversion_exchange_rate_from foreign key (from_exchange_id)
        REFERENCES exchange_rate(id),
    constraint FK_conversion_exchange_rate_to foreign key (to_exchange_id)
        REFERENCES exchange_rate(id)
);

