package com.exchange.bank.mapper;

import com.exchange.bank.dao.entity.User;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterRequest request);
}
