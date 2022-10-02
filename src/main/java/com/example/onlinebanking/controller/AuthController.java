package com.example.onlinebanking.controller;

import static com.example.onlinebanking.constants.ApiConstants.AUTH_API;

import com.example.onlinebanking.model.dto.request.LoginRequestDto;
import com.example.onlinebanking.model.dto.request.RegistrationUserDto;
import com.example.onlinebanking.model.dto.response.JwtResponse;
import com.example.onlinebanking.service.security.AuthenticationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AUTH_API)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService autenticationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
        return autenticationService.registerUser(registrationUserDto);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequestDto requestDto) {
        return autenticationService.loginUser(requestDto);
    }
}
