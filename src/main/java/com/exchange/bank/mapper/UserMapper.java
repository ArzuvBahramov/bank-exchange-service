package com.exchange.bank.mapper;

import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto dto);

    User toEntity(RegisterRequest request);
}
