package com.example.onlinebanking.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.onlinebanking.config.security.model.MyUserPrincipal;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    @Value("${app.security.jwt.expiration-time}")
    private int jwtExpirationMs;

    public String generateJwtToken(MyUserPrincipal userPrincipal) {

        var authorities = getAuthorities(userPrincipal.getAuthorities());

        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withClaim("authorities", authorities)
                .withClaim("userId", userPrincipal.getId().toString())
                .withNotBefore(new Date())
                .withExpiresAt(getTokenExpiresAt())
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    private Date getTokenExpiresAt() {
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(Calendar.MILLISECOND, jwtExpirationMs);

        return calendar.getTime();
    }

    private List<String> getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
