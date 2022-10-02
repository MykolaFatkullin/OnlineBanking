package com.example.onlinebanking.mapper;

import com.example.onlinebanking.model.dto.request.RegistrationUserDto;
import com.example.onlinebanking.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface UserMapper {

    UserEntity registrationDtoToUser(RegistrationUserDto dto);
}
