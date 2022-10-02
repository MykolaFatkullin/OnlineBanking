package com.example.onlinebanking.repository;

import com.example.onlinebanking.model.entity.AuthorityEntity;
import com.example.onlinebanking.model.enums.AuthorityEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
    AuthorityEntity findByAuthority(AuthorityEnum authorityEnum);
}
