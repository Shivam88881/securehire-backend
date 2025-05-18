// Aplication.java
package com.curtin.securehire.entity.db;

import com.curtin.securehire.constant.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "applications")
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Temporal(TemporalType.DATE)
    @Column(name = "applied_date")
    private Date appliedDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private boolean canChat;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;
}
