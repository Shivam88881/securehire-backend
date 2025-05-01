package com.curtin.securehire.controller;

import com.curtin.securehire.entity.*;
import com.curtin.securehire.service.UserService;
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
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody User user) {
        logger.info("Received request to create new user");
        User createdUser = userService.signup(user);
        logger.info("User created successfully with ID: {}", createdUser.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signin(
            @RequestParam @NotBlank(message = "Email is required") String email,
            @RequestParam @NotBlank(message = "Password is required") String password) {
        logger.info("Received login request for user with email: {}", email);
        User user = userService.signin(email, password);
        logger.info("User logged in successfully: {}", email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getProfile(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to get profile for user with ID: {}", userId);
        User user = userService.getProfile(userId);
        logger.info("Profile fetched successfully for user with ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateProfile(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody User user) {
        logger.info("Received request to update profile for user with ID: {}", userId);
        User updatedUser = userService.updateProfile(userId, user);
        logger.info("Profile updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to delete user with ID: {}", userId);
        userService.deleteUser(userId);
        logger.info("User deleted successfully with ID: {}", userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<User> changePassword(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @NotBlank(message = "New password is required") String newPassword) {
        logger.info("Received request to change password for user with ID: {}", userId);
        User user = userService.changePassword(userId, newPassword);
        logger.info("Password changed successfully for user with ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        logger.info("Received request to list all users");
        List<User> users = userService.listAllUsers();
        logger.info("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @Min(value = 1, message = "Role ID must be positive") Integer newRoleId) {
        logger.info("Received request to update role to {} for user with ID: {}", newRoleId, userId);
        User user = userService.updateUserRole(userId, newRoleId);
        logger.info("Role updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/address")
    public ResponseEntity<Address> addAddress(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Address address) {
        logger.info("Received request to add address for user with ID: {}", userId);
        Address savedAddress = userService.addAddress(userId, address);
        logger.info("Address added successfully for user with ID: {}", userId);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> updateAddress(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Address address) {
        logger.info("Received request to update address for user with ID: {}", userId);
        Address updatedAddress = userService.updateAddress(userId, address);
        logger.info("Address updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(updatedAddress);
    }

    @PostMapping("/{userId}/resume")
    public ResponseEntity<Resume> addResume(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Resume resume) {
        logger.info("Received request to add resume for user with ID: {}", userId);
        Resume savedResume = userService.addResume(userId, resume);
        logger.info("Resume added successfully for user with ID: {}", userId);
        return new ResponseEntity<>(savedResume, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/resume/{resumeId}")
    public ResponseEntity<Resume> changeDefaultResume(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @PathVariable @Min(value = 1, message = "Resume ID must be positive") Integer resumeId) {
        logger.info("Received request to change default resume to ID {} for user with ID: {}", resumeId, userId);
        Resume resume = userService.changeDefaultResume(userId, resumeId);
        logger.info("Default resume changed successfully for user with ID: {}", userId);
        return ResponseEntity.ok(resume);
    }

    @PutMapping("/{userId}/salary-range")
    public ResponseEntity<Void> updateExpectedSalaryRange(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @Min(value = 0, message = "Minimum salary must be non-negative") double minSalary,
            @RequestParam @Min(value = 0, message = "Maximum salary must be non-negative") double maxSalary) {
        logger.info("Received request to update salary range to min={}, max={} for user with ID: {}",
                minSalary, maxSalary, userId);
        userService.updateExpectedSalaryRange(userId, minSalary, maxSalary);
        logger.info("Salary range updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/skill")
    public ResponseEntity<Skill> addSkill(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @Valid @RequestBody Skill skill) {
        logger.info("Received request to add skill '{}' for user with ID: {}", skill.getName(), userId);
        Skill savedSkill = userService.addSkill(userId, skill);
        logger.info("Skill added successfully for user with ID: {}", userId);
        return new ResponseEntity<>(savedSkill, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/skills")
    public ResponseEntity<List<Skill>> updateSkill(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestBody ArrayList<Integer> updatedSkillList) {
        logger.info("Received request to update skill list for user with ID: {}", userId);
        List<Skill> skills = userService.updateSkill(userId, updatedSkillList);
        logger.info("Skills updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok(skills);
    }

    @PutMapping("/{userId}/premium-status")
    public ResponseEntity<Void> updatePremiumUserStatus(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam boolean isPremium) {
        logger.info("Received request to update premium status to {} for user with ID: {}", isPremium, userId);
        userService.updatePremiumUserStatus(userId, isPremium);
        logger.info("Premium status updated successfully for user with ID: {}", userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<User> activateUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to activate user with ID: {}", userId);
        User user = userService.activateUser(userId);
        logger.info("User activated successfully with ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<User> deactivateUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to deactivate user with ID: {}", userId);
        User user = userService.deactivateUser(userId);
        logger.info("User deactivated successfully with ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<User> resetPassword(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam @NotBlank(message = "New password is required") String newPassword) {
        logger.info("Received request to reset password for user with ID: {}", userId);
        User user = userService.resetPassword(userId, newPassword);
        logger.info("Password reset successfully for user with ID: {}", userId);
        return ResponseEntity.ok(user);
    }
}
