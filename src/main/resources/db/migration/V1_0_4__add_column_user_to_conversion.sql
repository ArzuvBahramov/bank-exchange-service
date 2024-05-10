alter table conversion
    add user_id integer;

alter table conversion
    add constraint conversion_users_id_fk
        foreign key (user_id) references users;