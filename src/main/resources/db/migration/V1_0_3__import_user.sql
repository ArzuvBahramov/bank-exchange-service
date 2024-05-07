insert into users (id,
                   firstname,
                   lastname,
                   email,
                   username,
                   password,
                   created_at,
                   created_by,
                   modified_at,
                   modified_by)
values (0, 'Michle', 'Parker', 'michle.parker@mail.com', 'user', '$2a$10$UuSs4Bn9WmFU9op21BPKmuHe6pSyjh2A3ZlwKyaAH6i3yY9PagHNm', current_timestamp, 'system_user', current_timestamp, 'system_user');