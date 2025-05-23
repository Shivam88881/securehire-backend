package com.curtin.securehire.controller;

import com.curtin.securehire.constant.BusinessSecTor;
import com.curtin.securehire.constant.CompanyType;
import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.service.db.RecruiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@Validated
public class RecruiterController {

    private static final Logger logger = LoggerFactory.getLogger(RecruiterController.class);

    @Autowired
    private RecruiterService recruiterService;

    // Authentication endpoints
    @PostMapping("/signup")
    public ResponseEntity<Recruiter> signup(@Valid @RequestBody Recruiter recruiter) {
        logger.info("Received request to create new recruiter");
        Recruiter createdRecruiter = recruiterService.signup(recruiter);
        logger.info("Recruiter created successfully with ID: {}", createdRecruiter.getId());
        return new ResponseEntity<>(createdRecruiter, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<Recruiter> signin(
            @RequestParam @NotBlank(message = "Email is required") String email,
            @RequestParam @NotBlank(message = "Password is required") String password) {
        logger.info("Received login request for recruiter with email: {}", email);
        Recruiter recruiter = recruiterService.login(email, password);
        logger.info("Recruiter logged in successfully: {}", email);
        return ResponseEntity.ok(recruiter);
    }

    // CRUD operations
    @GetMapping("/{recruiterId}")
    public ResponseEntity<Recruiter> getRecruiterById(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId) {
        logger.info("Received request to get recruiter with ID: {}", recruiterId);
        Recruiter recruiter = recruiterService.findById(recruiterId);
        logger.info("Fetched recruiter with ID: {}", recruiterId);
        return ResponseEntity.ok(recruiter);
    }

    @GetMapping("/profile/{recruiterId}")
    public ResponseEntity<Recruiter> getProfile(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId) {
        logger.info("Received request to get profile for recruiter with ID: {}", recruiterId);
        Recruiter recruiter = recruiterService.findById(recruiterId); // Using findById as getProfile returns null
        logger.info("Profile fetched successfully for recruiter with ID: {}", recruiterId);
        return ResponseEntity.ok(recruiter);
    }

    @PutMapping("/{recruiterId}")
    public ResponseEntity<Recruiter> updateRecruiter(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @Valid @RequestBody Recruiter recruiter) {
        logger.info("Received request to update recruiter with ID: {}", recruiterId);
        Recruiter updatedRecruiter = recruiterService.update(recruiterId, recruiter);
        logger.info("Recruiter updated successfully with ID: {}", recruiterId);
        return ResponseEntity.ok(updatedRecruiter);
    }

    @PutMapping("/{recruiterId}/profile")
    public ResponseEntity<Recruiter> updateProfile(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @Valid @RequestBody Recruiter recruiter) {
        logger.info("Received request to update profile for recruiter with ID: {}", recruiterId);
        Recruiter updatedRecruiter = recruiterService.updateProfile(recruiterId, recruiter);
        logger.info("Profile updated successfully for recruiter with ID: {}", recruiterId);
        return ResponseEntity.ok(updatedRecruiter);
    }


    @DeleteMapping("/{recruiterId}")
    public ResponseEntity<Void> deleteRecruiter(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId) {
        logger.info("Received request to delete recruiter with ID: {}", recruiterId);
        recruiterService.delete(recruiterId);
        logger.info("Recruiter deleted successfully with ID: {}", recruiterId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Recruiter>> listAllRecruiters() {
        logger.info("Received request to list all recruiters");
        List<Recruiter> recruiters = recruiterService.findAll();
        logger.info("Fetched {} recruiters", recruiters.size());
        return ResponseEntity.ok(recruiters);
    }

    // Email operations
    @GetMapping("/email")
    public ResponseEntity<Recruiter> getRecruiterByEmail(
            @RequestParam @NotBlank(message = "Email is required") String email) {
        logger.info("Received request to get recruiter by email: {}", email);
        Recruiter recruiter = recruiterService.findByEmail(email);
        logger.info("Fetched recruiter with email: {}", email);
        return ResponseEntity.ok(recruiter);
    }

    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> checkEmailExists(
            @RequestParam @NotBlank(message = "Email is required") String email) {
        logger.info("Received request to check if email exists: {}", email);
        boolean exists = recruiterService.existsByEmail(email);
        logger.info("Email {} exists: {}", email, exists);
        return ResponseEntity.ok(exists);
    }

    // Company-related operations
    @GetMapping("/company-name")
    public ResponseEntity<List<Recruiter>> getRecruitersByCompanyName(
            @RequestParam @NotBlank(message = "Company name is required") String companyName) {
        logger.info("Received request to get recruiters by company name: {}", companyName);
        List<Recruiter> recruiters = recruiterService.findByCompanyName(companyName);
        logger.info("Fetched {} recruiters with company name containing: {}", recruiters.size(), companyName);
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping("/company-type/{companyType}")
    public ResponseEntity<List<Recruiter>> getRecruitersByCompanyType(
            @PathVariable CompanyType companyType) {
        logger.info("Received request to get recruiters by company type: {}", companyType);
        List<Recruiter> recruiters = recruiterService.findByCompanyType(companyType);
        logger.info("Fetched {} recruiters with company type: {}", recruiters.size(), companyType);
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping("/business-sector/{businessSector}")
    public ResponseEntity<List<Recruiter>> getRecruitersByBusinessSector(
            @PathVariable BusinessSecTor businessSector) {
        logger.info("Received request to get recruiters by business sector: {}", businessSector);
        List<Recruiter> recruiters = recruiterService.findByBusinessSector(businessSector);
        logger.info("Fetched {} recruiters with business sector: {}", recruiters.size(), businessSector);
        return ResponseEntity.ok(recruiters);
    }

    // Job-related operations
    @GetMapping("/{recruiterId}/jobs")
    public ResponseEntity<List<Job>> getRecruiterJobs(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId) {
        logger.info("Received request to get jobs for recruiter with ID: {}", recruiterId);
        List<Job> jobs = recruiterService.getRecruiterJobs(recruiterId);
        logger.info("Fetched {} jobs for recruiter with ID: {}", jobs.size(), recruiterId);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/{recruiterId}/jobs")
    public ResponseEntity<Job> postJob(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @Valid @RequestBody Job job) {
        logger.info("Received request to post job for recruiter with ID: {}", recruiterId);
        Job postedJob = recruiterService.postJob(recruiterId, job);
        logger.info("Job posted successfully for recruiter with ID: {}", recruiterId);
        return new ResponseEntity<>(postedJob, HttpStatus.CREATED);
    }

    // Account management
    @PutMapping("/{recruiterId}/change-password")
    public ResponseEntity<Boolean> changePassword(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @RequestParam @NotBlank(message = "Old password is required") String oldPassword,
            @RequestParam @NotBlank(message = "New password is required") String newPassword) {
        logger.info("Received request to change password for recruiter with ID: {}", recruiterId);
        boolean changed = recruiterService.changePassword(recruiterId, oldPassword, newPassword);
        logger.info("Password changed successfully for recruiter with ID: {}", recruiterId);
        return ResponseEntity.ok(changed);
    }

    @PutMapping("/{recruiterId}/block")
    public ResponseEntity<Void> blockRecruiter(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @RequestParam boolean blocked) {
        logger.info("Received request to {} recruiter with ID: {}", blocked ? "block" : "unblock", recruiterId);
        recruiterService.blockRecruiter(recruiterId, blocked);
        logger.info("Recruiter {} successfully with ID: {}", blocked ? "blocked" : "unblocked", recruiterId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{recruiterId}/refresh-token")
    public ResponseEntity<Void> updateRefreshToken(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @RequestParam @NotBlank(message = "Refresh token is required") String refreshToken) {
        logger.info("Received request to update refresh token for recruiter with ID: {}", recruiterId);
        recruiterService.updateRefreshToken(recruiterId, refreshToken);
        logger.info("Refresh token updated successfully for recruiter with ID: {}", recruiterId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{recruiterId}/verify-password")
    public ResponseEntity<Boolean> verifyPassword(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @RequestParam @NotBlank(message = "Password is required") String password) {
        logger.info("Received request to verify password for recruiter with ID: {}", recruiterId);
        boolean verified = recruiterService.verifyPassword(recruiterId, password);
        logger.info("Password verification result for recruiter with ID {}: {}", recruiterId, verified);
        return ResponseEntity.ok(verified);
    }

    // Search and filters
    @GetMapping("/search")
    public ResponseEntity<List<Recruiter>> searchRecruiters(
            @RequestParam(required = false) String query) {
        logger.info("Received request to search recruiters with query: {}", query);
        List<Recruiter> recruiters = recruiterService.searchRecruiters(query);
        logger.info("Found {} recruiters matching search query", recruiters.size());
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping("/filters")
    public ResponseEntity<List<Recruiter>> getRecruitersByFilters(
            @RequestParam(required = false) CompanyType companyType,
            @RequestParam(required = false) BusinessSecTor businessSector,
            @RequestParam(required = false) Integer minEmployees,
            @RequestParam(required = false) Integer maxEmployees) {
        logger.info("Received request to get recruiters by filters");
        List<Recruiter> recruiters = recruiterService.findByFilter(
                companyType, businessSector, minEmployees, maxEmployees);
        logger.info("Found {} recruiters matching filters", recruiters.size());
        return ResponseEntity.ok(recruiters);
    }
}