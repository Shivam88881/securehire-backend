//Job.java
package com.curtin.securehire.entity.db;

import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.constant.JobType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String requirement;
    private String responsibility;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Enumerated(EnumType.STRING)
    private EmployementType employementType;

    @ManyToMany
    @JoinTable(
            name = "job_technical_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> technicalSkills = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "job_soft_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> softSkills = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "recruiter_id") // Assuming recruiter_id is the foreign key column
    @JsonBackReference
    private Recruiter recruiter;


    private int maxApplicants;

    @Embedded
    private Range salaryRange;

    @Enumerated(EnumType.STRING)
    private JobType jobType;
    private Date postedDate;
    private Date deadline;
}

