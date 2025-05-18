// Recruiter.java
package com.curtin.securehire.entity.db;

import com.curtin.securehire.constant.BusinessSecTor;
import com.curtin.securehire.constant.CompanyType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recruiters")
@Data
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String companyName;
    private String email;
    private String password;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "recruiter") // 'recruiter_id' is a field in the Job entity
    @JsonManagedReference
    private List<Job> jobs = new ArrayList<>();

    private String refreshToken;
    private boolean isBlocked;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(EnumType.STRING)
    private BusinessSecTor businessSecTor;

    @Embedded // Or use @OneToOne if Range should be in a separate table
    private Range NoOfEmployee;

    private String website;
    private String description;
}

