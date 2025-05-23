package com.curtin.securehire.controller;

import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.constant.JobType;
import com.curtin.securehire.entity.db.Range;
import com.curtin.securehire.service.db.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        log.info("Entering createJob method with request payload: {}", job);
        Job createdJob = jobService.createJob(job);
        log.info("Exiting createJob method. Created job: {}", createdJob);
        return ResponseEntity.ok(createdJob);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJobById(@PathVariable int jobId) {
        log.info("Entering getJobById method with jobId: {}", jobId);
        Job job = jobService.getJobById(jobId);
        log.info("Exiting getJobById method. Retrieved job: {}", job);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        log.info("Entering getAllJobs method.");
        List<Job> jobs = jobService.getAllJobs();
        log.info("Exiting getAllJobs method. Retrieved {} jobs.", jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(@PathVariable int jobId, @RequestBody Job updatedJob) {
        log.info("Entering updateJob method with jobId: {} and updated details: {}", jobId, updatedJob);
        Job job = jobService.updateJob(jobId, updatedJob);
        log.info("Exiting updateJob method. Updated job: {}", job);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable int jobId) {
        log.info("Entering deleteJob method with jobId: {}", jobId);
        jobService.deleteJob(jobId);
        log.info("Exiting deleteJob method. Job deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Job>> getJobsByRecruiterId(@PathVariable int recruiterId) {
        log.info("Entering getJobsByRecruiterId method with recruiterId: {}", recruiterId);
        List<Job> jobs = jobService.getJobsByRecruiterId(recruiterId);
        log.info("Exiting getJobsByRecruiterId method. Retrieved {} jobs.", jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobtype/{jobType}")
    public ResponseEntity<List<Job>> getJobsByJobType(@PathVariable JobType jobType) {
        log.info("Entering getJobsByJobType method with jobType: {}", jobType);
        List<Job> jobs = jobService.getJobsByJobType(jobType);
        log.info("Exiting getJobsByJobType method. Retrieved {} jobs.", jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/salary/{minSalary}/{maxSalary}")
    public ResponseEntity<List<Job>> getJobsBySalaryRange(@PathVariable double minSalary, @PathVariable double maxSalary) {
        Range salaryRange = new Range(minSalary, maxSalary);
        log.info("Entering getJobsBySalaryRange method with salary range: {}", salaryRange);
        List<Job> jobs = jobService.getJobsBySalaryRange(salaryRange);
        log.info("Exiting getJobsBySalaryRange method. Retrieved {} jobs.", jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/employment/{employmentType}")
    public ResponseEntity<List<Job>> getJobsByEmploymentType(@PathVariable EmployementType employmentType) {
        log.info("Entering getJobsByEmploymentType method with employmentType: {}", employmentType);
        List<Job> jobs = jobService.getJobsByEmploymentType(employmentType);
        log.info("Exiting getJobsByEmploymentType method. Retrieved {} jobs.", jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Job>> getJobsByFilter(
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) Integer recruiterId,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) double minSalary,
            @RequestParam(required = false) double maxSalary,
            @RequestParam(required = false) EmployementType employmentType,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {

        Range salaryRange = new Range(minSalary, maxSalary);

        log.info("Entering getJobsByFilter method with parameters - " +
                        "Location: {}, Recruiter: {}, JobType: {}, SalaryRange: {}, EmploymentType: {}, DateRange: {} to {}",
                locationId, recruiterId, jobType, salaryRange, employmentType, startDate, endDate);

        List<Job> jobs = jobService.getJobsByFilter(locationId, recruiterId, jobType, salaryRange, employmentType, startDate, endDate);

        log.info("Exiting getJobsByFilter method. Retrieved {} jobs.", jobs.size());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/filter-with-skills")
    public ResponseEntity<List<Job>> getJobsByFilterWithSkills(
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) Integer recruiterId,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) Range salaryRange,
            @RequestParam(required = false) EmployementType employementType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) List<Integer> skillIds,
            @RequestParam(required = false) Boolean requiredAll,
            @RequestParam(required = false) Integer minMatches) {

        log.info("Received request to filter jobs with skills");

        List<Job> jobs = jobService.getJobsByFilterWithSkills(
                locationId, recruiterId, jobType, salaryRange, employementType,
                startDate, endDate, skillIds, requiredAll ,minMatches);

        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/location-hierarchy/{locationId}")
    public ResponseEntity<List<Job>> getJobsByLocationHierarchy(
            @PathVariable("locationId") Integer locationId) {
        log.info("Received request to get jobs for location hierarchy ID: {}", locationId);
        List<Job> jobs = jobService.getJobsByLocationHierarchy(locationId);
        return ResponseEntity.ok(jobs);
    }
}