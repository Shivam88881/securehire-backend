package com.curtin.securehire.controller;

import com.curtin.securehire.constant.ApplicationStatus;
import com.curtin.securehire.entity.db.Application;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.service.db.ApplicationService;
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

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@Validated
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationService applicationService;

    // Create a new application
    @PostMapping
    public ResponseEntity<Application> createApplication(@Valid @RequestBody Application application) {
        logger.info("Received request to create new application");
        Application createdApplication = applicationService.save(application);
        logger.info("Application created successfully with ID: {}", createdApplication.getId());
        return new ResponseEntity<>(createdApplication, HttpStatus.CREATED);
    }

    // Get all applications
    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        logger.info("Received request to get all applications");
        List<Application> applications = applicationService.findAll();
        logger.info("Fetched {} applications", applications.size());
        return ResponseEntity.ok(applications);
    }

    // Get application by ID
    @GetMapping("/{applicationId}")
    public ResponseEntity<Application> getApplicationById(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId) {
        logger.info("Received request to get application with ID: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        logger.info("Application fetched successfully with ID: {}", applicationId);
        return ResponseEntity.ok(application);
    }

    // Update an application
    @PutMapping("/{applicationId}")
    public ResponseEntity<Application> updateApplication(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId,
            @Valid @RequestBody Application application) {
        logger.info("Received request to update application with ID: {}", applicationId);
        Application updatedApplication = applicationService.update(applicationId, application);
        logger.info("Application updated successfully with ID: {}", applicationId);
        return ResponseEntity.ok(updatedApplication);
    }

    // Delete an application
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId) {
        logger.info("Received request to delete application with ID: {}", applicationId);
        applicationService.delete(applicationId);
        logger.info("Application deleted successfully with ID: {}", applicationId);
        return ResponseEntity.noContent().build();
    }

    // Get applications by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Application>> getApplicationsByUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to get applications for user with ID: {}", userId);
        List<Application> applications = applicationService.findByUserID(userId);
        logger.info("Fetched {} applications for user with ID: {}", applications.size(), userId);
        return ResponseEntity.ok(applications);
    }

    // Get applications by recruiter ID
    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Application>> getApplicationsByRecruiter(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") int recruiterId) {
        logger.info("Received request to get applications for recruiter with ID: {}", recruiterId);
        List<Application> applications = applicationService.findByRecruiterId(recruiterId);
        logger.info("Fetched {} applications for recruiter with ID: {}", applications.size(), recruiterId);
        return ResponseEntity.ok(applications);
    }

    // Get applications by job ID
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJob(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId) {
        logger.info("Received request to get applications for job with ID: {}", jobId);
        List<Application> applications = applicationService.findByJobId(jobId);
        logger.info("Fetched {} applications for job with ID: {}", applications.size(), jobId);
        return ResponseEntity.ok(applications);
    }

    // Get applications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Application>> getApplicationsByStatus(
            @PathVariable @NotNull(message = "Status is required") ApplicationStatus status) {
        logger.info("Received request to get applications with status: {}", status);
        List<Application> applications = applicationService.findByStatus(status);
        logger.info("Fetched {} applications with status: {}", applications.size(), status);
        return ResponseEntity.ok(applications);
    }

    // Update application status
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId,
            @RequestParam @NotNull(message = "Status is required") ApplicationStatus status) {
        logger.info("Received request to update status to {} for application with ID: {}", status, applicationId);
        Application application = applicationService.updateStatus(applicationId, status);
        logger.info("Status updated successfully for application with ID: {}", applicationId);
        return ResponseEntity.ok(application);
    }

    // Assign recruiter to application
    @PutMapping("/{applicationId}/recruiter")
    public ResponseEntity<Application> assignRecruiter(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId,
            @Valid @RequestBody Recruiter recruiter) {
        logger.info("Received request to assign recruiter to application with ID: {}", applicationId);
        Application application = applicationService.assignRecruiter(applicationId, recruiter);
        logger.info("Recruiter assigned successfully to application with ID: {}", applicationId);
        return ResponseEntity.ok(application);
    }

    // Toggle chat access for application
    @PutMapping("/{applicationId}/chat-status")
    public ResponseEntity<Application> updateChatStatus(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId,
            @RequestParam boolean canChat) {
        logger.info("Received request to update chat status to {} for application with ID: {}", canChat, applicationId);
        Application application = applicationService.toggleChatAccess(applicationId, canChat);
        logger.info("Chat status updated successfully for application with ID: {}", applicationId);
        return ResponseEntity.ok(application);
    }

    // Count applications by job ID
    @GetMapping("/count/job/{jobId}")
    public ResponseEntity<Integer> countApplicationsByJob(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId) {
        logger.info("Received request to count applications for job with ID: {}", jobId);
        Integer count = applicationService.countApplicationsByJob(jobId);
        logger.info("Counted {} applications for job with ID: {}", count, jobId);
        return ResponseEntity.ok(count);
    }

    // Get applications by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Application>> getApplicationsByDateRange(
            @RequestParam @NotNull(message = "Start date is required") Date startDate,
            @RequestParam @NotNull(message = "End date is required") Date endDate) {
        logger.info("Received request to get applications between dates: {} and {}", startDate, endDate);
        List<Application> applications = applicationService.findApplicationsByDateRange(startDate, endDate);
        logger.info("Fetched {} applications between dates: {} and {}", applications.size(), startDate, endDate);
        return ResponseEntity.ok(applications);
    }

    // Get applications by user ID (renamed from getApplicationsByUser to avoid conflict)
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Application>> getApplicationsByUserId(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to get applications for user with ID: {}", userId);
        List<Application> applications = applicationService.findByUserID(userId);
        logger.info("Fetched {} applications for user with ID: {}", applications.size(), userId);
        return ResponseEntity.ok(applications);
    }

    // Get applications by recruiter ID (renamed to avoid conflict)
    @GetMapping("/by-recruiter/{recruiterId}")
    public ResponseEntity<List<Application>> getApplicationsByRecruiterId(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") int recruiterId) {
        logger.info("Received request to get applications for recruiter with ID: {}", recruiterId);
        List<Application> applications = applicationService.findByRecruiterId(recruiterId);
        logger.info("Fetched {} applications for recruiter with ID: {}", applications.size(), recruiterId);
        return ResponseEntity.ok(applications);
    }

    // Get applications by job ID (renamed to avoid conflict)
    @GetMapping("/by-job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJobId(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId) {
        logger.info("Received request to get applications for job with ID: {}", jobId);
        List<Application> applications = applicationService.findByJobId(jobId);
        logger.info("Fetched {} applications for job with ID: {}", applications.size(), jobId);
        return ResponseEntity.ok(applications);
    }

    // Get applications by job ID and date range
    @GetMapping("/job/{jobId}/date-range")
    public ResponseEntity<List<Application>> getApplicationsByJobAndDateRange(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId,
            @RequestParam @NotNull(message = "Start date is required") Date startDate,
            @RequestParam @NotNull(message = "End date is required") Date endDate) {
        logger.info("Received request to get applications for job with ID: {} between dates: {} and {}",
                jobId, startDate, endDate);
        List<Application> applications = applicationService.findApplicationsByJobAndDateRange(jobId, startDate, endDate);
        logger.info("Fetched {} applications for job with ID: {} between dates: {} and {}",
                applications.size(), jobId, startDate, endDate);
        return ResponseEntity.ok(applications);
    }

    // Count applications by job ID and status
    @GetMapping("/count/job/{jobId}/status/{status}")
    public ResponseEntity<Integer> countApplicationsByJobAndStatus(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId,
            @PathVariable ApplicationStatus status) {
        logger.info("Received request to count applications for job with ID: {} by status", jobId);
        Integer count = applicationService.countApplicationsByJobAndStatus(jobId,status);
        logger.info("Counted {} applications for job with ID: {} by status", count, jobId);
        return ResponseEntity.ok(count);
    }

    // Count applications by user ID
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Integer> countApplicationsByUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to count applications for user with ID: {}", userId);
        Integer count = applicationService.countApplicationsByUser(userId);
        logger.info("Counted {} applications for user with ID: {}", count, userId);
        return ResponseEntity.ok(count);
    }

    // Count applications by recruiter ID
    @GetMapping("/count/recruiter/{recruiterId}")
    public ResponseEntity<Integer> countApplicationsByRecruiter(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") int recruiterId) {
        logger.info("Received request to count applications for recruiter with ID: {}", recruiterId);
        Integer count = applicationService.countApplicationsByRecruiter(recruiterId);
        logger.info("Counted {} applications for recruiter with ID: {}", count, recruiterId);
        return ResponseEntity.ok(count);
    }


}