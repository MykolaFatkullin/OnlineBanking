package com.example.onlinebanking.data;

import com.example.onlinebanking.model.dto.request.LoginRequestDto;
import com.example.onlinebanking.model.dto.request.RegistrationUserDto;
import com.example.onlinebanking.model.dto.response.JwtResponse;
import java.util.UUID;

public class AuthenticationDataReceiver {

    public static RegistrationUserDto getRegistrationDto() {
        return RegistrationUserDto.builder()
                .username("myusername@register.com")
                .firstName("FirstName")
                .lastName("LastName")
                .password("MySecretP@ssword!")
                .build();
    }

    public static LoginRequestDto getLoginRequest() {
        return LoginRequestDto.builder()
                .username("username@username.com")
                .password("P@ssword!")
                .build();
    }

    public static JwtResponse getJwtResponse() {
        return JwtResponse.builder()
                .jwtToken("jwtToken")
                .userId(UUID.randomUUID())
                .refreshToken(UUID.randomUUID())
                .build();
    }
}
