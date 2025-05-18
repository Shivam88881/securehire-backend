package com.curtin.securehire.controller;

import com.curtin.securehire.entity.db.Address;
import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.entity.db.Resume;
import com.curtin.securehire.entity.db.Skill;
import com.curtin.securehire.service.db.CandidateService;
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


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class CandidateController {

    private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

    @Autowired
    private CandidateService candidateService;


    @GetMapping("/{userId}")
    public ResponseEntity<Candidate> getProfile(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to get profile for user with ID: {}", userId);
        Candidate candidate = candidateService.getProfile(userId);
        logger.info("Profile fetched successfully for user with ID: {}", userId);
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Candidate> updateProfile(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Candidate candidate) {
        logger.info("Received request to update profile for user with ID: {}", userId);
        Candidate updatedCandidate = candidateService.updateProfile(userId, candidate);
        logger.info("Profile updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(updatedCandidate);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to delete user with ID: {}", userId);
        candidateService.deleteUser(userId);
        logger.info("User deleted successfully with ID: {}", userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Candidate> changePassword(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @NotBlank(message = "New password is required") String newPassword) {
        logger.info("Received request to change password for user with ID: {}", userId);
        Candidate candidate = candidateService.changePassword(userId, newPassword);
        logger.info("Password changed successfully for user with ID: {}", userId);
        return ResponseEntity.ok(candidate);
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> listAllUsers() {
        logger.info("Received request to list all users");
        List<Candidate> candidates = candidateService.listAllUsers();
        logger.info("Fetched {} users", candidates.size());
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<Candidate> updateUserRole(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @Min(value = 1, message = "Role ID must be positive") Integer newRoleId) {
        logger.info("Received request to update role to {} for user with ID: {}", newRoleId, userId);
        Candidate candidate = candidateService.updateUserRole(userId, newRoleId);
        logger.info("Role updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(candidate);
    }

    @PostMapping("/{userId}/address")
    public ResponseEntity<Address> addAddress(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Address address) {
        logger.info("Received request to add address for user with ID: {}", userId);
        Address savedAddress = candidateService.addAddress(userId, address);
        logger.info("Address added successfully for user with ID: {}", userId);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> updateAddress(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Address address) {
        logger.info("Received request to update address for user with ID: {}", userId);
        Address updatedAddress = candidateService.updateAddress(userId, address);
        logger.info("Address updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(updatedAddress);
    }

    @PostMapping("/{userId}/resume")
    public ResponseEntity<Resume> addResume(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Resume resume) {
        logger.info("Received request to add resume for user with ID: {}", userId);
        Resume savedResume = candidateService.addResume(userId, resume);
        logger.info("Resume added successfully for user with ID: {}", userId);
        return new ResponseEntity<>(savedResume, HttpStatus.CREATED);
    }

    @PutMapping("/change-default/{userId}/resume/{resumeId}")
    public ResponseEntity<Resume> changeDefaultResume(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @PathVariable @Min(value = 1, message = "Resume ID must be positive") Integer resumeId) {
        logger.info("Received request to change default resume to ID {} for user with ID: {}", resumeId, userId);
        Resume resume = candidateService.changeDefaultResume(userId, resumeId);
        logger.info("Default resume changed successfully for user with ID: {}", userId);
        return ResponseEntity.ok(resume);
    }

    @PutMapping("/{userId}/salary-range")
    public ResponseEntity<String> updateExpectedSalaryRange(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @Min(value = 0, message = "Minimum salary must be non-negative") double minSalary,
            @RequestParam @Min(value = 0, message = "Maximum salary must be non-negative") double maxSalary) {
        logger.info("Received request to update salary range to min={}, max={} for user with ID: {}",
                minSalary, maxSalary, userId);
        candidateService.updateExpectedSalaryRange(userId, minSalary, maxSalary);
        logger.info("Salary range updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok("Salary range updated");
    }

    @PostMapping("/{userId}/skill")
    public ResponseEntity<Skill> addSkill(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Skill skill) {
        logger.info("Received request to add skill '{}' for user with ID: {}", skill.getName(), userId);
        Skill savedSkill = candidateService.addSkill(userId, skill);
        logger.info("Skill added successfully for user with ID: {}", userId);
        return new ResponseEntity<>(savedSkill, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/skills")
    public ResponseEntity<List<Skill>> updateSkill(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestBody ArrayList<Integer> updatedSkillList) {
        logger.info("Received request to update skill list for user with ID: {}", userId);
        List<Skill> skills = candidateService.updateSkill(userId, updatedSkillList);
        logger.info("Skills updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(skills);
    }

    @PutMapping("/{userId}/premium-status")
    public ResponseEntity<Void> updatePremiumUserStatus(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam boolean isPremium) {
        logger.info("Received request to update premium status to {} for user with ID: {}", isPremium, userId);
        candidateService.updatePremiumUserStatus(userId, isPremium);
        logger.info("Premium status updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<Candidate> activateUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to activate user with ID: {}", userId);
        Candidate candidate = candidateService.activateUser(userId);
        logger.info("User activated successfully with ID: {}", userId);
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Candidate> deactivateUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to deactivate user with ID: {}", userId);
        Candidate candidate = candidateService.deactivateUser(userId);
        logger.info("User deactivated successfully with ID: {}", userId);
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<Candidate> resetPassword(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @NotBlank(message = "New password is required") String newPassword) {
        logger.info("Received request to reset password for user with ID: {}", userId);
        Candidate candidate = candidateService.resetPassword(userId, newPassword);
        logger.info("Password reset successfully for user with ID: {}", userId);
        return ResponseEntity.ok(candidate);
    }


}
