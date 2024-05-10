create table users
(
    id serial
        constraint PK_users
            primary key,
    firstName varchar(50),
    lastName varchar(50),
    email varchar(50),
    username varchar(50) not null,
    password  varchar(200) not null,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    varchar(50) not null,
    modified_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by   varchar(50) not null
);

create index users_username_index
    on users (username);

create index users_email_index
    on users (email);