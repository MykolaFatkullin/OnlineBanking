package com.example.onlinebanking.service;

import static com.example.onlinebanking.data.UserDataReceiver.getUserEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.onlinebanking.exceptions.NotFoundException;
import com.example.onlinebanking.model.entity.UserEntity;
import com.example.onlinebanking.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserServiceTest {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserServiceTest() {
        userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }

    @Test
    void getUserById_ok() {
        UUID userId = UUID.randomUUID();

        var userOptional = Optional.of(getUserEntity(userId));

        when(userRepository.findById(userId)).thenReturn(userOptional);

        UserEntity userEntity = userService.getUserById(userId);

        assertNotNull(userEntity);
        assertEquals(userId, userEntity.getId());
    }

    @Test
    void getUserById_throwException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenThrow(new NotFoundException("User not found by id: " + userId));

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }
}
