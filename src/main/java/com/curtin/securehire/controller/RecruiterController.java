package com.curtin.securehire.controller;

import com.curtin.securehire.constant.BusinessSecTor;
import com.curtin.securehire.constant.CompanyType;
import com.curtin.securehire.entity.Recruiter;
import com.curtin.securehire.service.RecruiterService;
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

    @GetMapping("/{recruiterId}")
    public ResponseEntity<Recruiter> getProfile(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId) {
        logger.info("Received request to get profile for recruiter with ID: {}", recruiterId);
        Recruiter recruiter = recruiterService.getProfile(recruiterId);
        logger.info("Profile fetched successfully for recruiter with ID: {}", recruiterId);
        return ResponseEntity.ok(recruiter);
    }

    @PutMapping("/{recruiterId}")
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

    @PutMapping("/{recruiterId}/change-password")
    public ResponseEntity<Boolean> changePassword(
            @PathVariable @Min(value = 1, message = "Recruiter ID must be positive") Integer recruiterId,
            @RequestParam @NotBlank(message = "New password is required") String newPassword,
            @RequestParam @NotBlank(message = "Old password is required") String oldPassword) {
        logger.info("Received request to change password for recruiter with ID: {}", recruiterId);
        boolean recruiter = recruiterService.changePassword(recruiterId, oldPassword ,newPassword);
        logger.info("Password changed successfully for recruiter with ID: {}", recruiterId);
        return ResponseEntity.ok(recruiter);
    }

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