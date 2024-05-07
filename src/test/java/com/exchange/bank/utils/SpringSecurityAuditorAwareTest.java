package com.exchange.bank.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig({SpringSecurityAuditorAware.class})
public class SpringSecurityAuditorAwareTest {
    @Autowired
    private SpringSecurityAuditorAware springSecurityAuditorAware;

    @Test
    public void whenGetCurrentAuditor_thenReturnUsername() {
        Authentication authentication = new TestingAuthenticationToken("testUser", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<String> currentAuditor = springSecurityAuditorAware.getCurrentAuditor();
        assertTrue(currentAuditor.isPresent());
        assertEquals("testUser", currentAuditor.get());

        SecurityContextHolder.clearContext();
    }

}
