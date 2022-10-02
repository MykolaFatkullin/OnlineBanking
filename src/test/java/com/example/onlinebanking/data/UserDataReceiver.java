package com.example.onlinebanking.data;

import com.example.onlinebanking.model.entity.UserEntity;
import java.util.Collections;
import java.util.UUID;

public class UserDataReceiver {

    public static UserEntity getUserEntity(UUID id) {
        return UserEntity.builder()
                .id(id)
                .username("username")
                .authorities(Collections.emptyList())
                .firstName("firstName")
                .lastName("lastName")
                .build();
    }
}
