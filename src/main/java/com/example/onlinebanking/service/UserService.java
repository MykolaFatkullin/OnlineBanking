package com.example.onlinebanking.service;

import com.example.onlinebanking.exceptions.NotFoundException;
import com.example.onlinebanking.model.entity.UserEntity;
import com.example.onlinebanking.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserById(UUID id) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if (userEntityOptional.isEmpty()) {
            throw new NotFoundException("User not found by id: " + id);
        }

        return userEntityOptional.get();
    }
}
