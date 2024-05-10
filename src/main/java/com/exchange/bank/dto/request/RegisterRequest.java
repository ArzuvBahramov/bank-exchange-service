package com.exchange.bank.dto.request;

public record RegisterRequest (
        String firstname,
        String lastname,
        String username,
        String email,
        String password
){
}
