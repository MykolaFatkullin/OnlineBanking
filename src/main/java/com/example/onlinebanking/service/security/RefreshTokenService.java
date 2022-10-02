package com.example.onlinebanking.service.security;

import com.example.onlinebanking.model.entity.RefreshTokenEntity;
import com.example.onlinebanking.repository.RefreshTokenRepository;
import com.example.onlinebanking.service.UserService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.security.refresh-token.expiration-time}")
    private Long refreshTokenDurationMs;

    public RefreshTokenEntity createRefreshToken(UUID userId) {
        var tokenEntity = new RefreshTokenEntity();
        tokenEntity.setToken(UUID.randomUUID());
        tokenEntity.setExpiryDate(LocalDateTime.now()
                .plus(refreshTokenDurationMs, ChronoUnit.MILLIS));

        var userEntity = userService.getUserById(userId);

        tokenEntity.setUserEntity(userEntity);

        return refreshTokenRepository.save(tokenEntity);
    }
}
