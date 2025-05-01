package com.curtin.securehire.controller;

import com.curtin.securehire.constant.AplicationStatus;
import com.curtin.securehire.entity.Aplication;
import com.curtin.securehire.entity.Recruiter;
import com.curtin.securehire.entity.User;
import com.curtin.securehire.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@Validated
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Aplication> createApplication(@Valid @RequestBody Aplication application) {
        logger.info("Received request to create new application");
        Aplication createdApplication = applicationService.save(application);
        logger.info("Application created successfully with ID: {}", createdApplication.getId());
        return new ResponseEntity<>(createdApplication, HttpStatus.CREATED);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<Aplication> getApplicationById(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId) {
        logger.info("Received request to get application with ID: {}", applicationId);
        Aplication application = applicationService.findById(applicationId);
        logger.info("Application fetched successfully with ID: {}", applicationId);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Aplication>> getApplicationsByUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to get applications for user with ID: {}", userId);
        List<Aplication> applications = applicationService.findByUserID(userId);
        logger.info("Fetched {} applications for user with ID: {}", applications.size(), userId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Aplication>> getApplicationsByRecruiter(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") int recruiterId) {
        logger.info("Received request to get applications for recruiter with ID: {}", recruiterId);
        List<Aplication> applications = applicationService.findByRecruiterId(recruiterId);
        logger.info("Fetched {} applications for recruiter with ID: {}", applications.size(), recruiterId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Aplication>> getApplicationsByJob(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId) {
        logger.info("Received request to get applications for job with ID: {}", jobId);
        List<Aplication> applications = applicationService.findByJobId(jobId);
        logger.info("Fetched {} applications for job with ID: {}", applications.size(), jobId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Aplication>> getApplicationsByStatus(
            @PathVariable @NotNull(message = "Status is required") AplicationStatus status) {
        logger.info("Received request to get applications with status: {}", status);
        List<Aplication> applications = applicationService.findByStatus(status);
        logger.info("Fetched {} applications with status: {}", applications.size(), status);
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Aplication> updateApplicationStatus(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId,
            @RequestParam @NotNull(message = "Status is required") AplicationStatus status) {
        logger.info("Received request to update status to {} for application with ID: {}", status, applicationId);
        Aplication application = applicationService.updateStatus(applicationId, status);
        logger.info("Status updated successfully for application with ID: {}", applicationId);
        return ResponseEntity.ok(application);
    }

    @PutMapping("/{applicationId}/chat-status")
    public ResponseEntity<Aplication> updateChatStatus(
            @PathVariable @Min(value = 1, message = "Application ID must be positive") int applicationId,
            @RequestParam boolean canChat) {
        logger.info("Received request to update chat status to {} for application with ID: {}", canChat, applicationId);
        Aplication application = applicationService.toggleChatAccess(applicationId, canChat);
        logger.info("Chat status updated successfully for application with ID: {}", applicationId);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Aplication>> getApplicationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        logger.info("Received request to get applications between dates: {} and {}", startDate, endDate);
        List<Aplication> applications = applicationService.getApplicationsByDateRange(startDate, endDate);
        logger.info("Fetched {} applications for date range", applications.size());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/chat-enabled")
    public ResponseEntity<List<Aplication>> getApplicationsWithChatEnabled() {
        logger.info("Received request to get applications with chat enabled");
        List<Aplication> applications = applicationService.getApplicationsWithChatEnabled();
        logger.info("Fetched {} applications with chat enabled", applications.size());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<Aplication>> getUnassignedApplications() {
        logger.info("Received request to get unassigned applications");
        List<Aplication> applications = applicationService.getUnassignedApplications();
        logger.info("Fetched {} unassigned applications", applications.size());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/count/job/{jobId}")
    public ResponseEntity<Long> countApplicationsByJob(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") int jobId) {
        logger.info("Received request to count applications for job with ID: {}", jobId);
        Long count = applicationService.countApplicationsByJob(jobId);
        logger.info("Counted {} applications for job with ID: {}", count, jobId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countApplicationsByStatus(
            @PathVariable @NotNull(message = "Status is required") AplicationStatus status) {
        logger.info("Received request to count applications with status: {}", status);
        Long count = applicationService.countApplicationsByStatus(status);
        logger.info("Counted {} applications with status: {}", count, status);
        return ResponseEntity.ok(count);
    }
