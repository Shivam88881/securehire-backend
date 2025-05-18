package com.curtin.securehire.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginResponse {
    private String jwtToken;
    private String refreshToken;
    private String role;
    private String email;

    public LoginResponse(String jwtToken, String refreshToken ,String role, String email) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.email = email;
    }
}

