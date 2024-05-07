package com.exchange.bank.dto;

public record UserDto (
        Long id,
        String firstname,
        String lastname,
        String username,
        String email
){

}
