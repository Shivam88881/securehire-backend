package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.entity.db.*;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.db.*;
import com.curtin.securehire.repository.es.CandidateSearchRepository;
import com.curtin.securehire.service.db.AddressService;
import com.curtin.securehire.service.db.CandidateService;
import com.curtin.securehire.service.es.CandidateSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);

    @Autowired
    private CandidateRepository candidateRepository;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CandidateSearchService candidateSearchService;

    @Autowired
    private CandidateSearchRepository candidateSearchRepository;

    @Override
    public Candidate getProfile(Integer userId) {
        logger.info("Fetching profile for user with ID: {}", userId);

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Profile not found for user with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        logger.info("Profile fetched successfully for user with ID: {}", userId);
        return userOpt.get();
    }

    @Override
    public Candidate updateProfile(Integer userId, Candidate candidate) {
        logger.info("Updating profile for user with ID: {}", userId);
        Optional<Candidate> existingUserOpt = candidateRepository.findById(userId);

        if (existingUserOpt.isEmpty()) {
            logger.error("Cannot update profile: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate existingCandidate = existingUserOpt.get();

            // Only update fields that are not null
            if (candidate.getEmail() != null) existingCandidate.setEmail(candidate.getEmail());
            if (candidate.getName() != null) existingCandidate.setName(candidate.getName());
            if (candidate.getPhone() != null) existingCandidate.setPhone(candidate.getPhone());
            if (candidate.getAvatar() != null) existingCandidate.setAvatar(candidate.getAvatar());
            if (candidate.getAddress() != null) {
                Address existingCandidateAddress = existingCandidate.getAddress();
                if(existingCandidateAddress == null) {
                    Address address = addressService.save(candidate.getAddress());
                    existingCandidate.setAddress(address);
                } else {
                    addressService.update(existingCandidateAddress.getId(), candidate.getAddress());
                }
            }
            if (candidate.getPreferredLocations() != null) existingCandidate.setPreferredLocations(candidate.getPreferredLocations());
            if (candidate.getSalaryRange() != null) existingCandidate.setSalaryRange(candidate.getSalaryRange());
            if (candidate.isBlocked() != existingCandidate.isBlocked()) existingCandidate.setBlocked(candidate.isBlocked());
            if (candidate.isPremiumUser() != existingCandidate.isPremiumUser()) existingCandidate.setPremiumUser(candidate.isPremiumUser());

            Candidate savedCandidate = candidateRepository.save(existingCandidate);
            logger.info("Profile updated successfully for user with ID: {}", userId);

            // Index the updated entity in Elasticsearch
            candidateSearchService.indexCandidate(savedCandidate);
            return savedCandidate;
        } catch (Exception e) {
            logger.error("Error updating profile for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update profile: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        logger.info("Deleting user with ID: {}", userId);

        if (!candidateRepository.existsById(userId)) {
            logger.error("Cannot delete user: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            candidateRepository.deleteById(userId);
            logger.info("User deleted successfully with ID: {}", userId);

            // Delete from Elasticsearch index

            candidateSearchRepository.deleteById(userId);
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete user: " + e.getMessage());
        }
    }

    @Override
    public Candidate changePassword(Integer userId, String newPassword) {
        logger.info("Changing password for user with ID: {}", userId);

        if (newPassword == null || newPassword.trim().isEmpty()) {
            logger.error("Cannot change password: New password is required");
            throw new BadRequestException("New password is required");
        }

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot change password: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate candidate = userOpt.get();
            String encodedPassword = passwordEncoder.encode(newPassword);
            candidate.setPassword(encodedPassword);
            Candidate savedCandidate = candidateRepository.save(candidate);
            logger.info("Password changed successfully for user with ID: {}", userId);
            return savedCandidate;
        } catch (Exception e) {
            logger.error("Error changing password for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to change password: " + e.getMessage());
        }
    }

    @Override
    public List<Candidate> listAllUsers() {
        logger.info("Fetching all users");
        try {
            List<Candidate> candidates = candidateRepository.findAll();
            logger.info("Fetched {} users", candidates.size());
            return candidates;
        } catch (Exception e) {
            logger.error("Error fetching all users: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch users: " + e.getMessage());
        }
    }

    @Override
    public Candidate findUserById(Integer userId) {
        logger.info("Finding user with ID: {}", userId);

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        logger.info("Found user with ID: {}", userId);
        return userOpt.get();
    }

    @Override
    public Candidate resetPassword(Integer userId, String newPassword) {
        logger.info("Resetting password for user with ID: {}", userId);
        return changePassword(userId, newPassword);
    }

    @Override
    public Candidate activateUser(Integer userId) {
        logger.info("Activating user with ID: {}", userId);

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot activate user: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate candidate = userOpt.get();
            candidate.setBlocked(false);
            Candidate savedCandidate = candidateRepository.save(candidate);
            logger.info("User activated successfully with ID: {}", userId);
            return savedCandidate;
        } catch (Exception e) {
            logger.error("Error activating user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to activate user: " + e.getMessage());
        }
    }

    @Override
    public Candidate deactivateUser(Integer userId) {
        logger.info("Deactivating user with ID: {}", userId);

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot deactivate user: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate candidate = userOpt.get();
            candidate.setBlocked(true);
            Candidate savedCandidate = candidateRepository.save(candidate);
            logger.info("User deactivated successfully with ID: {}", userId);
            return savedCandidate;
        } catch (Exception e) {
            logger.error("Error deactivating user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to deactivate user: " + e.getMessage());
        }
    }

    @Override
    public Candidate updateUserRole(Integer userId, Integer newRoleId) {
        logger.info("Updating role to role with ID '{}' for user with ID: {}", newRoleId, userId);

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
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
            Candidate candidate = userOpt.get();
            Role role = roleOpt.get();
            candidate.setRole(role);
            Candidate savedCandidate = candidateRepository.save(candidate);
            logger.info("Role updated successfully for user with ID: {}", userId);
            return savedCandidate;
        } catch (Exception e) {
            logger.error("Error updating role for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update user role: " + e.getMessage());
        }
    }

    @Override
    public List<Candidate> getUsersByRole(Role role) {
        logger.info("Fetching users with role: {}", role.getName());

        try {
            List<Candidate> candidates = candidateRepository.findByRole(role);
            logger.info("Found {} users with role: {}", candidates.size(), role.getName());
            return candidates;
        } catch (Exception e) {
            logger.error("Error fetching users by role {}: {}", role.getName(), e.getMessage(), e);
            throw new BadRequestException("Failed to fetch users by role: " + e.getMessage());
        }
    }

    @Override
    public Address addAddress(Integer userId, Address address) {
        logger.info("Adding address for user with ID: {}", userId);

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot add address: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Address savedAddress = addressRepository.save(address);

            Candidate candidate = userOpt.get();

            candidate.setAddress(savedAddress);
            candidateRepository.save(candidate);

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

        Optional<Candidate> candidateOpt = candidateRepository.findById(userId);
        if (candidateOpt.isEmpty()) {
            logger.error("Cannot update address: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate candidate = candidateOpt.get();
            Address savedAddress = addressService.update(candidate.getAddress().getId(), address);

            candidate.setAddress(savedAddress);
            candidateRepository.save(candidate);

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

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot add resume: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate candidate = userOpt.get();
            System.out.println("resume = ...................................."+resume);
            Resume savedResume = resumeRepository.save(resume);

            candidate.getResumes().add(savedResume);
            if (candidate.getSelectedResume() == null) {
                candidate.setSelectedResume(savedResume);
            }
            candidateRepository.save(candidate);

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

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
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

        try {
            Candidate candidate = userOpt.get();
            candidate.setSelectedResume(resume);
            candidateRepository.save(candidate);

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

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update salary range: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate candidate = userOpt.get();
            candidate.setSalaryRange(new Range(minSalary, maxSalary));
            candidateRepository.save(candidate);
            logger.info("Salary range updated successfully for user with ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error updating salary range for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update salary range: " + e.getMessage());
        }
    }

    @Override
    public Skill addSkill(Integer userId, Skill skill) {
        logger.info("Adding skill '{}' for user with ID: {}", skill.getName(), userId);

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot add skill: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Skill savedSkill = skillRepository.save(skill);

            Candidate candidate = userOpt.get();
            candidate.getSkills().add(savedSkill);
            candidateRepository.save(candidate);

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

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update skill: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        // check all the skills if that skills exist
        List<Skill> updatedSkill = skillRepository.findAllById(updatedSkillList);

        try {
            Candidate candidate = userOpt.get();
            candidate.setSkills(updatedSkill);
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

        Optional<Candidate> userOpt = candidateRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update premium status: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Candidate candidate = userOpt.get();
            candidate.setPremiumUser(isPremium);
            candidateRepository.save(candidate);
            logger.info("Premium status updated successfully for user with ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error updating premium status for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update premium status: " + e.getMessage());
        }
    }

    @Override
    public Candidate updateUserByRefreshToken(String refreshToken, Candidate candidate) {
        candidate.setRefreshToken(refreshToken);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setHours(timestamp.getHours() + 24);
        candidateRepository.save(candidate);
        return candidate;
    }

    @Override
    public Candidate findUserByRefreshToken(String refreshToken) {
        logger.info("Finding user with refresh token = "+refreshToken);
        System.out.println("refreshToken = "+refreshToken);
        Optional<Candidate> user = candidateRepository.findByRefreshToken(refreshToken);
        if(user.isPresent()) {
            logger.debug("User found "+user.get());
            return user.get();
        }else {
            logger.info("No user with refresh token = "+refreshToken);
        }
        return null;
    }
}
