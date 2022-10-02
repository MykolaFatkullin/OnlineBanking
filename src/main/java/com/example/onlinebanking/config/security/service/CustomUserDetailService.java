package com.example.onlinebanking.config.security.service;

import com.example.onlinebanking.config.security.model.MyUserPrincipal;
import com.example.onlinebanking.exceptions.NotFoundException;
import com.example.onlinebanking.model.entity.AuthorityEntity;
import com.example.onlinebanking.model.entity.UserEntity;
import com.example.onlinebanking.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isEmpty()) {
            throw new NotFoundException("User was not found by username: " + username);
        }

        var userEntity = userEntityOptional.get();

        var authorities = getAuthorities(userEntity.getAuthorities());

        return MyUserPrincipal.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .username(userEntity.getUsername())
                .authorities(authorities)
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            List<AuthorityEntity> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().name()))
                .toList();
    }
}
