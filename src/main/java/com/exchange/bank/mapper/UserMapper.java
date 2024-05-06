package com.exchange.bank.mapper;

import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto toDto(User user);
}
