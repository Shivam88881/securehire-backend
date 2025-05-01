package com.curtin.securehire.service.impl;

import com.curtin.securehire.entity.*;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.exception.ValidationException;
import com.curtin.securehire.repository.*;
import com.curtin.securehire.service.AddressService;
import com.curtin.securehire.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressService addressService;

    @Override
    public User signup(User user) {
        logger.info("Creating new user with email: {}", user.getEmail());

        // Validate required fields
        List<String> validationErrors = new ArrayList<>();
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            validationErrors.add("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            validationErrors.add("Password is required");
        }

        if (!validationErrors.isEmpty()) {
            logger.error("Validation errors during user signup: {}", validationErrors);
            throw new ValidationException("Invalid user data", validationErrors);
        }

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            logger.error("User signup failed: Email already exists: {}", user.getEmail());
            throw new BadRequestException("User with email " + user.getEmail() + " already exists");
        }

        try {
            logger.info("Saving new user to database");
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create user: " + e.getMessage());
        }
    }

    @Override
    public User signin(String email, String password) {
        logger.info("User login attempt: {}", email);

        if (email == null || email.trim().isEmpty()) {
            logger.error("Login failed: Email is required");
            throw new BadRequestException("Email is required");
        }

        if (password == null || password.trim().isEmpty()) {
            logger.error("Login failed: Password is required");
            throw new BadRequestException("Password is required");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            logger.error("Login failed: User not found with email: {}", email);
            throw new NotFoundException("User not found with email: " + email);
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            logger.error("Login failed: Invalid password for user: {}", email);
            throw new BadRequestException("Invalid credentials");
        }

        if (user.isBlocked()) {
            logger.error("Login failed: User account is blocked: {}", email);
            throw new BadRequestException("Your account has been blocked. Please contact support.");
        }

        logger.info("User logged in successfully: {}", email);
        return user;
    }

    @Override
    public User getProfile(Integer userId) {
        logger.info("Fetching profile for user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Profile not found for user with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        logger.info("Profile fetched successfully for user with ID: {}", userId);
        return userOpt.get();
    }

    @Override
    public User updateProfile(Integer userId, User user) {
        logger.info("Updating profile for user with ID: {}", userId);
        Optional<User> existingUserOpt = userRepository.findById(userId);

        if (!userRepository.existsById(userId)) {
            logger.error("Cannot update profile: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            User existingUser = existingUserOpt.get();

            // Only update fields that are not null
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if (user.getName() != null) existingUser.setName(user.getName());
            if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
            if (user.getAvatar() != null) existingUser.setAvatar(user.getAvatar());
            if (user.getRole() != null) existingUser.setRole(user.getRole());
            if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
            if (user.getSalaryRange() != null) existingUser.setSalaryRange(user.getSalaryRange());
            if (user.isBlocked() != existingUser.isBlocked()) existingUser.setBlocked(user.isBlocked());
            if (user.isPremiumUser() != existingUser.isPremiumUser()) existingUser.setPremiumUser(user.isPremiumUser());

            User savedUser = userRepository.save(existingUser);
            logger.info("Profile updated successfully for user with ID: {}", userId);
            return savedUser;
        } catch (Exception e) {
            logger.error("Error updating profile for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update profile: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        logger.info("Deleting user with ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            logger.error("Cannot delete user: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            userRepository.deleteById(userId);
            logger.info("User deleted successfully with ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete user: " + e.getMessage());
        }
    }

    @Override
    public User changePassword(Integer userId, String newPassword) {
        logger.info("Changing password for user with ID: {}", userId);

        if (newPassword == null || newPassword.trim().isEmpty()) {
            logger.error("Cannot change password: New password is required");
            throw new BadRequestException("New password is required");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot change password: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            User user = userOpt.get();
            user.setPassword(newPassword);
            User savedUser = userRepository.save(user);
            logger.info("Password changed successfully for user with ID: {}", userId);
            return savedUser;
        } catch (Exception e) {
            logger.error("Error changing password for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to change password: " + e.getMessage());
        }
    }

    @Override
    public List<User> listAllUsers() {
        logger.info("Fetching all users");
        try {
            List<User> users = userRepository.findAll();
            logger.info("Fetched {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Error fetching all users: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch users: " + e.getMessage());
        }
    }

    @Override
    public User findUserById(Integer userId) {
        logger.info("Finding user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        logger.info("Found user with ID: {}", userId);
        return userOpt.get();
    }

    @Override
    public User resetPassword(Integer userId, String newPassword) {
        logger.info("Resetting password for user with ID: {}", userId);
        return changePassword(userId, newPassword);
    }

    @Override
    public User activateUser(Integer userId) {
        logger.info("Activating user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot activate user: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            User user = userOpt.get();
            user.setBlocked(false);
            User savedUser = userRepository.save(user);
            logger.info("User activated successfully with ID: {}", userId);
            return savedUser;
        } catch (Exception e) {
            logger.error("Error activating user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to activate user: " + e.getMessage());
        }
    }

    @Override
    public User deactivateUser(Integer userId) {
        logger.info("Deactivating user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot deactivate user: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            User user = userOpt.get();
            user.setBlocked(true);
            User savedUser = userRepository.save(user);
            logger.info("User deactivated successfully with ID: {}", userId);
            return savedUser;
        } catch (Exception e) {
            logger.error("Error deactivating user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to deactivate user: " + e.getMessage());
        }
    }

    @Override
    public User updateUserRole(Integer userId, Integer newRoleId) {
        logger.info("Updating role to role with ID '{}' for user with ID: {}", newRoleId, userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update role: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        logger.info("Finding role with ID: {}", newRoleId);
        Optional<Role> roleOpt = roleRepository.findById(newRoleId);

        if(roleOpt.isEmpty()) {
            logger.error("Cannot update role: Role not found with ID: {}", newRoleId);
            throw new NotFoundException("Role not found with ID: " + newRoleId);
        }

        // Note: This method needs to be modified to include code to fetch role by name

        try {
            User user = userOpt.get();
            Role role = roleOpt.get();
            user.setRole(role);
            User savedUser = userRepository.save(user);
            logger.info("Role updated successfully for user with ID: {}", userId);
            return savedUser;
        } catch (Exception e) {
            logger.error("Error updating role for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update user role: " + e.getMessage());
        }
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        logger.info("Fetching users with role: {}", role.getName());

        try {
            List<User> users = userRepository.findByRole(role);
            logger.info("Found {} users with role: {}", users.size(), role.getName());
            return users;
        } catch (Exception e) {
            logger.error("Error fetching users by role {}: {}", role.getName(), e.getMessage(), e);
            throw new BadRequestException("Failed to fetch users by role: " + e.getMessage());
        }
    }

    @Override
    public Address addAddress(Integer userId, Address address) {
        logger.info("Adding address for user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot add address: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Address savedAddress = addressRepository.save(address);

            User user = userOpt.get();

            user.setAddress(savedAddress);
            userRepository.save(user);

            logger.info("Address added successfully for user with ID: {}", userId);
            return savedAddress;
        } catch (Exception e) {
            logger.error("Error adding address for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to add address: " + e.getMessage());
        }
    }

    @Override
    public Address updateAddress(Integer userId, Address address) {
        logger.info("Updating address for user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update address: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Address savedAddress = addressService.update(address.getId(), address);

            User user = userOpt.get();
            user.setAddress(savedAddress);
            userRepository.save(user);

            logger.info("Address updated successfully for user with ID: {}", userId);
            return savedAddress;
        } catch (Exception e) {
            logger.error("Error updating address for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update address: " + e.getMessage());
        }
    }

    @Override
    public Resume addResume(Integer userId, Resume resume) {
        logger.info("Adding resume for user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot add resume: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            User user = userOpt.get();
            resume.setUser(user);
            Resume savedResume = resumeRepository.save(resume);

            user.getResumes().add(savedResume);
            if (user.getSelectedResume() == null) {
                user.setSelectedResume(savedResume);
            }
            userRepository.save(user);

            logger.info("Resume added successfully for user with ID: {}", userId);
            return savedResume;
        } catch (Exception e) {
            logger.error("Error adding resume for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to add resume: " + e.getMessage());
        }
    }

    @Override
    public Resume changeDefaultResume(Integer userId, Integer resumeId) {
        logger.info("Changing default resume to ID {} for user with ID: {}", resumeId, userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot change default resume: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
        if (resumeOpt.isEmpty()) {
            logger.error("Cannot change default resume: Resume not found with ID: {}", resumeId);
            throw new NotFoundException("Resume not found with ID: " + resumeId);
        }

        Resume resume = resumeOpt.get();
        if (resume.getUser() == null || !resume.getUser().getId().equals(userId)) {
            logger.error("Cannot change default resume: Resume with ID {} does not belong to user with ID {}", resumeId, userId);
            throw new BadRequestException("This resume does not belong to the specified user");
        }

        try {
            User user = userOpt.get();
            user.setSelectedResume(resume);
            userRepository.save(user);

            logger.info("Default resume changed successfully for user with ID: {}", userId);
            return resume;
        } catch (Exception e) {
            logger.error("Error changing default resume for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to change default resume: " + e.getMessage());
        }
    }

    @Override
    public void updateExpectedSalaryRange(Integer userId, double minSalary, double maxSalary) {
        logger.info("Updating salary range to min={}, max={} for user with ID: {}", minSalary, maxSalary, userId);

        if (minSalary > maxSalary) {
            logger.error("Cannot update salary range: Minimum salary cannot be greater than maximum salary");
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update salary range: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            User user = userOpt.get();
            user.setSalaryRange(new Range(minSalary, maxSalary));
            userRepository.save(user);
            logger.info("Salary range updated successfully for user with ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error updating salary range for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update salary range: " + e.getMessage());
        }
    }

    @Override
    public Skill addSkill(Integer userId, Skill skill) {
        logger.info("Adding skill '{}' for user with ID: {}", skill.getName(), userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot add skill: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Skill savedSkill = skillRepository.save(skill);

            User user = userOpt.get();
            user.getSkills().add(savedSkill);
            userRepository.save(user);

            logger.info("Skill added successfully for user with ID: {}", userId);
            return savedSkill;
        } catch (Exception e) {
            logger.error("Error adding skill for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to add skill: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> updateSkill(Integer userId, ArrayList<Integer> updatedSkillList) {
        logger.info("Updating skill list for user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update skill: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        // check all the skills if that skills exist
        List<Skill> updatedSkill = skillRepository.findAllById(updatedSkillList);

        try {
            User user = userOpt.get();
            user.setSkills(updatedSkill);
            logger.info("Skill updated successfully for user with ID: {}", userId);
            return updatedSkill;
        } catch (Exception e) {
            logger.error("Error updating skill for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update skill: " + e.getMessage());
        }
    }

    @Override
    public void updatePremiumUserStatus(Integer userId, boolean isPremium) {
        logger.info("Updating premium status to {} for user with ID: {}", isPremium, userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update premium status: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            User user = userOpt.get();
            user.setPremiumUser(isPremium);
            userRepository.save(user);
            logger.info("Premium status updated successfully for user with ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error updating premium status for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update premium status: " + e.getMessage());
        }
    }
}
