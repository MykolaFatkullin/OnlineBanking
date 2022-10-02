package com.example.onlinebanking.service.security;

import com.example.onlinebanking.config.security.model.MyUserPrincipal;
import com.example.onlinebanking.exceptions.BusinessException;
import com.example.onlinebanking.exceptions.UnathorizedException;
import com.example.onlinebanking.mapper.UserMapper;
import com.example.onlinebanking.model.dto.request.LoginRequestDto;
import com.example.onlinebanking.model.dto.request.RegistrationUserDto;
import com.example.onlinebanking.model.dto.response.JwtResponse;
import com.example.onlinebanking.model.enums.AuthorityEnum;
import com.example.onlinebanking.repository.AuthorityRepository;
import com.example.onlinebanking.repository.UserRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthorityRepository authorityRepository;

    public String registerUser(RegistrationUserDto registrationUserDto) {
        var username = registrationUserDto.getUsername();

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("User already exists with username: " + username);
        }

        var userEntity = userMapper.registrationDtoToUser(registrationUserDto);
        var encodedPassword = passwordEncoder.encode(registrationUserDto.getPassword());
        userEntity.setPassword(encodedPassword);

        var authority = authorityRepository.findByAuthority(AuthorityEnum.ROLE_USER);
        userEntity.setAuthorities(List.of(authority));

        var storedUser = userRepository.save(userEntity);

        return storedUser.getId().toString();
    }

    public JwtResponse loginUser(LoginRequestDto requestDto) {
        var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(), requestDto.getPassword()));

        var principal = (MyUserPrincipal) authenticate.getPrincipal();

        boolean passwordsMatches = passwordEncoder.matches(
                requestDto.getPassword(), principal.getPassword());

        if (passwordsMatches) {
            var jwtToken = jwtService.generateJwtToken(principal);

            var userId = principal.getId();
            var refreshToken = refreshTokenService.createRefreshToken(userId);

            return JwtResponse.builder()
                    .userId(userId)
                    .jwtToken(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .build();
        }

        throw new UnathorizedException("Wrong username or password");
    }
}
