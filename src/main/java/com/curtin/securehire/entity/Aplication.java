// Aplication.java
package com.curtin.securehire.entity;

import com.curtin.securehire.constant.AplicationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "applications")
@Data
public class Aplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Date appliedDate;

    @Enumerated(EnumType.STRING)
    private AplicationStatus status;

    private boolean canChat;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;
}
