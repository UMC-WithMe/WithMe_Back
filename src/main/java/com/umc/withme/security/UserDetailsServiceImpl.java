package com.umc.withme.security;

import com.umc.withme.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsServiceImpl {

    @Bean
    public UserDetailsService userDetailsService(MemberService memberService) {
        return username -> WithMeAppPrinciple.of(memberService.findMemberByEmail(username));
    }
}
