package com.umc.withme.security;

import com.umc.withme.domain.constant.RoleType;
import com.umc.withme.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WithMeAppPrinciple implements UserDetails {

    private MemberDto memberDto;

    public static WithMeAppPrinciple of(MemberDto memberDto) {
        return new WithMeAppPrinciple(memberDto);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<RoleType> roleTypes = Set.of(RoleType.values());

        return roleTypes.stream()
                .map(RoleType::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Principle에 저장된 회원의 id(PK)를 조회한다.
     *
     * @return Principle에 저장된 회원의 id(PK)
     */
    public Long getMemberId() {
        return memberDto.getId();
    }

    @Override
    public String getUsername() {
        return memberDto.getEmail();
    }

    @Override
    public String getPassword() {
        return memberDto.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
