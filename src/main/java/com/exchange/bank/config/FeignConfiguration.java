package com.exchange.bank.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
        "com.exchange.bank.centralbankapi"
})
public class FeignConfiguration {
}
