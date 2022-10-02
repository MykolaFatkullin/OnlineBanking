package com.example.onlinebanking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("jwt_token")
    private String jwtToken;

    @JsonProperty("refresh_token")
    private UUID refreshToken;
}
