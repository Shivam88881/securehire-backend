// Resume.java
package com.curtin.securehire.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "resumes")
@Data
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date uploadedDate;
    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

