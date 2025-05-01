package com.curtin.securehire.controller;

import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.entity.Job;
import com.curtin.securehire.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@Validated
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        logger.info("Received request to get all jobs");
        List<Job> jobs = jobService.getAllJobs();
        logger.info("Fetched {} jobs", jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/employment-type/{employmentType}")
    public ResponseEntity<List<Job>> getJobsByEmploymentType(
            @PathVariable @NotNull(message = "Employment type is required") EmployementType employmentType) {
        logger.info("Received request to get jobs with employment type: {}", employmentType);
        List<Job> jobs = jobService.getJobsByEmploymentType(employmentType);
        logger.info("Fetched {} jobs with employment type: {}", jobs.size(), employmentType);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Job>> getJobsByRecruiterId(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") int recruiterId) {
        logger.info("Received request to get jobs for recruiter with ID: {}", recruiterId);
        List<Job> jobs = jobService.getJobsByRecruiterId(recruiterId);
        logger.info("Fetched {} jobs for recruiter with ID: {}", jobs.size(), recruiterId);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) {
        logger.info("Received request to create new job");
        Job createdJob = jobService.createJob(job);
        logger.info("Job created successfully with ID: {}", createdJob.getId());
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJobById(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId) {
        logger.info("Received request to get job with ID: {}", jobId);
        Job job = jobService.getJobById(jobId);
        logger.info("Job fetched successfully with ID: {}", jobId);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId,
            @Valid @RequestBody Job job) {
        logger.info("Received request to update job with ID: {}", jobId);
        Job updatedJob = jobService.updateJob(jobId, job);
        logger.info("Job updated successfully with ID: {}", jobId);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId) {
        logger.info("Received request to delete job with ID: {}", jobId);
        jobService.deleteJob(jobId);
        logger.info("Job deleted successfully with ID: {}", jobId);
        return ResponseEntity.noContent().build();
    }
}
