package com.curtin.securehire.service.impl;

import com.curtin.securehire.entity.Resume;
import com.curtin.securehire.entity.User;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.ResumeRepository;
import com.curtin.securehire.repository.UserRepository;
import com.curtin.securehire.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeServiceImpl.class);

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Resume findById(Integer resumeId) {
        logger.info("Finding resume by ID: {}", resumeId);

        Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
        if (resumeOpt.isEmpty()) {
            logger.error("Resume not found with ID: {}", resumeId);
            throw new NotFoundException("Resume not found with ID: " + resumeId);
        }

        logger.info("Found resume with ID: {}", resumeId);
        return resumeOpt.get();
    }

    @Override
    public List<Resume> findAll() {
        logger.info("Fetching all resumes");

        try {
            List<Resume> resumes = resumeRepository.findAll();
            logger.info("Fetched {} resumes", resumes.size());
            return resumes;
        } catch (Exception e) {
            logger.error("Error fetching all resumes: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch resumes: " + e.getMessage());
        }
    }

    @Override
    public Resume save(Resume resume) {
        logger.info("Creating new resume: {}", resume.getName());

        try {
            Resume savedResume = resumeRepository.save(resume);
            logger.info("Resume created successfully with ID: {}", savedResume.getId());
            return savedResume;
        } catch (Exception e) {
            logger.error("Error creating resume: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create resume: " + e.getMessage());
        }
    }

    @Override
    public Resume update(Integer resumeId, Resume resume) {
        logger.info("Updating resume with ID: {}", resumeId);

        if (!resumeRepository.existsById(resumeId)) {
            logger.error("Cannot update resume: Resume not found with ID: {}", resumeId);
            throw new NotFoundException("Resume not found with ID: " + resumeId);
        }

        try {
            resume.setId(resumeId);
            Resume updatedResume = resumeRepository.save(resume);
            logger.info("Resume updated successfully with ID: {}", resumeId);
            return updatedResume;
        } catch (Exception e) {
            logger.error("Error updating resume with ID {}: {}", resumeId, e.getMessage(), e);
            throw new BadRequestException("Failed to update resume: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer resumeId) {
        logger.info("Deleting resume with ID: {}", resumeId);

        if (!resumeRepository.existsById(resumeId)) {
            logger.error("Cannot delete resume: Resume not found with ID: {}", resumeId);
            throw new NotFoundException("Resume not found with ID: " + resumeId);
        }

        try {
            resumeRepository.deleteById(resumeId);
            logger.info("Resume deleted successfully with ID: {}", resumeId);
        } catch (Exception e) {
            logger.error("Error deleting resume with ID {}: {}", resumeId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete resume: " + e.getMessage());
        }
    }


    @Override
    public List<Resume> findByUserId(Integer userId) {
        logger.info("Finding resumes for user with ID: {}", userId);

        try {
            List<Resume> resumes = resumeRepository.findByUserId(userId);
            logger.info("Found {} resumes for user with UserID: {}", resumes.size(), userId);
            return resumes;
        } catch (Exception e) {
            logger.error("Error finding resumes for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to find resumes by user ID: " + e.getMessage());
        }
    }

    @Override
    public Resume addResumeForUser(Integer userId, Resume resume) {
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

            // Update user's resume list and set as default if none exists
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
    public Resume setDefaultResume(Integer userId, Integer resumeId) {
        logger.info("Setting resume with ID {} as default for user with ID: {}", resumeId, userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot set default resume: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
        if (resumeOpt.isEmpty()) {
            logger.error("Cannot set default resume: Resume not found with ID: {}", resumeId);
            throw new NotFoundException("Resume not found with ID: " + resumeId);
        }

        Resume resume = resumeOpt.get();
        if (resume.getUser() == null || !resume.getUser().getId().equals(userId)) {
            logger.error("Cannot set default resume: Resume with ID {} does not belong to user with ID {}", resumeId, userId);
            throw new BadRequestException("This resume does not belong to the specified user");
        }

        try {
            User user = userOpt.get();
            user.setSelectedResume(resume);
            userRepository.save(user);

            logger.info("Default resume set successfully for user with ID: {}", userId);
            return resume;
        } catch (Exception e) {
            logger.error("Error setting default resume for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to set default resume: " + e.getMessage());
        }
    }

    @Override
    public List<Resume> findByNameContaining(String name) {
        logger.info("Finding resumes with name containing: {}", name);

        try {
            List<Resume> resumes = resumeRepository.findByNameContaining(name);
            logger.info("Found {} resumes with name containing: {}", resumes.size(), name);
            return resumes;
        } catch (Exception e) {
            logger.error("Error finding resumes with name containing {}: {}", name, e.getMessage(), e);
            throw new BadRequestException("Failed to find resumes by name: " + e.getMessage());
        }
    }

    @Override
    public List<Resume> findByUploadedDateAfter(Date date) {
        logger.info("Finding resumes uploaded after date: {}", date);

        try {
            List<Resume> resumes = resumeRepository.findByUploadedDateAfter(date);
            logger.info("Found {} resumes uploaded after date: {}", resumes.size(), date);
            return resumes;
        } catch (Exception e) {
            logger.error("Error finding resumes uploaded after date {}: {}", date, e.getMessage(), e);
            throw new BadRequestException("Failed to find resumes by upload date: " + e.getMessage());
        }
    }

    @Override
    public Long countByUserId(Integer userId) {
        logger.info("Counting resumes for user with ID: {}", userId);

        try {
            Long count = resumeRepository.countByUserId(userId);
            logger.info("Found {} resumes for user with ID: {}", count, userId);
            return count;
        } catch (Exception e) {
            logger.error("Error counting resumes for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to count resumes: " + e.getMessage());
        }
    }

    @Override
    public List<Resume> findByFilters(Integer userId, String name, Date startDate) {
        logger.info("Finding resumes with filters - userId: {}, name: {}, startDate: {}", userId, name, startDate);

        try {
            List<Resume> resumes = resumeRepository.findResumesByFilters(userId, name, startDate);
            logger.info("Found {} resumes matching filters", resumes.size());
            return resumes;
        } catch (Exception e) {
            logger.error("Error finding resumes with filters: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to find resumes with filters: " + e.getMessage());
        }
    }
}
