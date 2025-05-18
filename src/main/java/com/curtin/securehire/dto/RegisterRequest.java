package com.curtin.securehire.dto;

import com.curtin.securehire.constant.RoleName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterRequest {
    private String name;         // For Candidate
    private String companyName;  // For Recruiter
    private String email;
    private String phone;
    private String password;
    private RoleName role;
}
