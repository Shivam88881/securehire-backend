// src/main/java/com/curtin/securehire/service/impl/ApplicationServiceImpl.java
package com.curtin.securehire.service.impl;

import com.curtin.securehire.constant.AplicationStatus;
import com.curtin.securehire.entity.Aplication;
import com.curtin.securehire.entity.Recruiter;
import com.curtin.securehire.entity.User;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.ApplicationRepository;
import com.curtin.securehire.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Aplication findById(Integer applicationId) {
        logger.info("Finding application by ID: {}", applicationId);

        Optional<Aplication> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        logger.info("Found application with ID: {}", applicationId);
        return applicationOpt.get();
    }

    @Override
    public List<Aplication> findAll() {
        logger.info("Fetching all applications");

        try {
            List<Aplication> applications = applicationRepository.findAll();
            logger.info("Fetched {} applications", applications.size());
            return applications;
        } catch (Exception e) {
            logger.error("Error fetching all applications: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch applications: " + e.getMessage());
        }
    }

    @Override
    public Aplication save(Aplication application) {
        logger.info("Creating new application");

        try {
            Aplication savedApplication = applicationRepository.save(application);
            logger.info("Application created successfully with ID: {}", savedApplication.getId());
            return savedApplication;
        } catch (Exception e) {
            logger.error("Error creating application: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create application: " + e.getMessage());
        }
    }

    @Override
    public Aplication update(Integer applicationId, Aplication application) {
        logger.info("Updating application with ID: {}", applicationId);

        if (!applicationRepository.existsById(applicationId)) {
            logger.error("Cannot update application: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            application.setId(applicationId);
            Aplication updatedApplication = applicationRepository.save(application);
            logger.info("Application updated successfully with ID: {}", applicationId);
            return updatedApplication;
        } catch (Exception e) {
            logger.error("Error updating application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to update application: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer applicationId) {
        logger.info("Deleting application with ID: {}", applicationId);

        if (!applicationRepository.existsById(applicationId)) {
            logger.error("Cannot delete application: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            applicationRepository.deleteById(applicationId);
            logger.info("Application deleted successfully with ID: {}", applicationId);
        } catch (Exception e) {
            logger.error("Error deleting application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete application: " + e.getMessage());
        }
    }

    @Override
    public List<Aplication> findByUserID(Integer userId) {
        logger.info("Finding applications for user with ID: {}", userId);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Aplication> applications = applicationRepository.findByUser(userId);
            logger.info("Found {} applications for user with ID: {}", applications.size(), userId);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by user: " + e.getMessage());
        }
    }

    @Override
    public List<Aplication> findByRecruiterId(Integer recruiterId) {
        logger.info("Finding applications for recruiter with ID: {}", recruiterId);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Aplication> applications = applicationRepository.findByRecruiter(recruiterId);
            logger.info("Found {} applications for recruiter with ID: {}", applications.size(), recruiterId);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications for recruiter with ID {}: {}", recruiterId, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by recruiter: " + e.getMessage());
        }
    }

    @Override
    public List<Aplication> findByJobId(Integer jobId) {
        logger.info("Finding applications for job with ID: {}", jobId);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Aplication> applications = applicationRepository.findByJobId(jobId);
            logger.info("Found {} applications for job with ID: {}", applications.size(), jobId);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications for job with ID {}: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by job ID: " + e.getMessage());
        }
    }

    @Override
    public List<Aplication> findByStatus(AplicationStatus status) {
        logger.info("Finding applications with status: {}", status);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Aplication> applications = applicationRepository.findByStatus(status);
            logger.info("Found {} applications with status: {}", applications.size(), status);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications with status {}: {}", status, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by status: " + e.getMessage());
        }
    }

    @Override
    public Aplication updateStatus(Integer applicationId, AplicationStatus status) {
        logger.info("Updating status to {} for application with ID: {}", status, applicationId);

        Optional<Aplication> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Cannot update status: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            Aplication application = applicationOpt.get();
            application.setStatus(status);
            Aplication updatedApplication = applicationRepository.save(application);
            logger.info("Status updated successfully for application with ID: {}", applicationId);
            return updatedApplication;
        } catch (Exception e) {
            logger.error("Error updating status for application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to update application status: " + e.getMessage());
        }
    }

    @Override
    public Aplication assignRecruiter(Integer applicationId, Recruiter recruiter) {
        logger.info("Assigning recruiter with ID {} to application with ID: {}", recruiter.getId(), applicationId);

        Optional<Aplication> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Cannot assign recruiter: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            Aplication application = applicationOpt.get();
            application.setRecruiter(recruiter);
            Aplication updatedApplication = applicationRepository.save(application);
            logger.info("Recruiter assigned successfully to application with ID: {}", applicationId);
            return updatedApplication;
        } catch (Exception e) {
            logger.error("Error assigning recruiter to application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to assign recruiter to application: " + e.getMessage());
        }
    }

    @Override
    public Aplication toggleChatAccess(Integer applicationId, boolean canChat) {
        logger.info("Setting chat access to {} for application with ID: {}", canChat, applicationId);

        Optional<Aplication> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Cannot toggle chat access: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            Aplication application = applicationOpt.get();
            application.setCanChat(canChat);
            Aplication updatedApplication = applicationRepository.save(application);
            logger.info("Chat access updated successfully for application with ID: {}", applicationId);
            return updatedApplication;
        } catch (Exception e) {
            logger.error("Error updating chat access for application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to update chat access: " + e.getMessage());
        }
    }
}
