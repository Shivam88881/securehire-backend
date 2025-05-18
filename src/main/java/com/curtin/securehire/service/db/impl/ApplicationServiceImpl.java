package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.constant.ApplicationStatus;
import com.curtin.securehire.entity.db.Application;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.db.ApplicationRepository;
import com.curtin.securehire.service.db.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Application findById(Integer applicationId) {
        logger.info("Finding application by ID: {}", applicationId);

        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        logger.info("Found application with ID: {}", applicationId);
        return applicationOpt.get();
    }

    @Override
    public List<Application> findAll() {
        logger.info("Fetching all applications");

        try {
            List<Application> applications = applicationRepository.findAll();
            logger.info("Fetched {} applications", applications.size());
            return applications;
        } catch (Exception e) {
            logger.error("Error fetching all applications: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch applications: " + e.getMessage());
        }
    }

    @Override
    public Application save(Application application) {
        logger.info("Creating new application");

        try {
            Application savedApplication = applicationRepository.save(application);
            logger.info("Application created successfully with ID: {}", savedApplication.getId());
            return savedApplication;
        } catch (Exception e) {
            logger.error("Error creating application: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create application: " + e);
        }
    }

    @Override
    public Application update(Integer applicationId, Application application) {
        logger.info("Updating application with ID: {}", applicationId);
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Cannot update application: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            Application existingApplication = applicationOpt.get();
            if(application.getStatus() != null) {
                existingApplication.setStatus(application.getStatus());
            }
            if(application.isCanChat()) {
                existingApplication.setCanChat(true);
            }
            if (application.getStatus()!= null) {
                existingApplication.setStatus(application.getStatus());
            }
            if(application.getAppliedDate() != null) {
                existingApplication.setAppliedDate(application.getAppliedDate());
            }
            Application updatedApplication = applicationRepository.save(existingApplication);
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
    public List<Application> findByUserID(Integer userId) {
        logger.info("Finding applications for user with ID: {}", userId);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Application> applications = applicationRepository.findByUser(userId);
            logger.info("Found {} applications for user with ID: {}", applications.size(), userId);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by user: " + e.getMessage());
        }
    }

    @Override
    public List<Application> findByRecruiterId(Integer recruiterId) {
        logger.info("Finding applications for recruiter with ID: {}", recruiterId);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Application> applications = applicationRepository.findByRecruiter(recruiterId);
            logger.info("Found {} applications for recruiter with ID: {}", applications.size(), recruiterId);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications for recruiter with ID {}: {}", recruiterId, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by recruiter: " + e.getMessage());
        }
    }

    @Override
    public List<Application> findByJobId(Integer jobId) {
        logger.info("Finding applications for job with ID: {}", jobId);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Application> applications = applicationRepository.findByJob(jobId);
            logger.info("Found {} applications for job with ID: {}", applications.size(), jobId);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications for job with ID {}: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by job ID: " + e.getMessage());
        }
    }

    @Override
    public List<Application> findByStatus(ApplicationStatus status) {
        logger.info("Finding applications with status: {}", status);

        try {
            // You would need to add this method to your ApplicationRepository
            List<Application> applications = applicationRepository.findByStatus(status);
            logger.info("Found {} applications with status: {}", applications.size(), status);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications with status {}: {}", status, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by status: " + e.getMessage());
        }
    }

    @Override
    public Application updateStatus(Integer applicationId, ApplicationStatus status) {
        logger.info("Updating status to {} for application with ID: {}", status, applicationId);

        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Cannot update status: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            Application application = applicationOpt.get();
            application.setStatus(status);
            Application updatedApplication = applicationRepository.save(application);
            logger.info("Status updated successfully for application with ID: {}", applicationId);
            return updatedApplication;
        } catch (Exception e) {
            logger.error("Error updating status for application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to update application status: " + e.getMessage());
        }
    }

    @Override
    public Application assignRecruiter(Integer applicationId, Recruiter recruiter) {
        logger.info("Assigning recruiter with ID {} to application with ID: {}", recruiter.getId(), applicationId);

        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Cannot assign recruiter: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            Application application = applicationOpt.get();
            application.setRecruiter(recruiter);
            Application updatedApplication = applicationRepository.save(application);
            logger.info("Recruiter assigned successfully to application with ID: {}", applicationId);
            return updatedApplication;
        } catch (Exception e) {
            logger.error("Error assigning recruiter to application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to assign recruiter to application: " + e.getMessage());
        }
    }

    @Override
    public Application toggleChatAccess(Integer applicationId, boolean canChat) {
        logger.info("Setting chat access to {} for application with ID: {}", canChat, applicationId);

        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            logger.error("Cannot toggle chat access: Application not found with ID: {}", applicationId);
            throw new NotFoundException("Application not found with ID: " + applicationId);
        }

        try {
            Application application = applicationOpt.get();
            application.setCanChat(canChat);
            Application updatedApplication = applicationRepository.save(application);
            logger.info("Chat access updated successfully for application with ID: {}", applicationId);
            return updatedApplication;
        } catch (Exception e) {
            logger.error("Error updating chat access for application with ID {}: {}", applicationId, e.getMessage(), e);
            throw new BadRequestException("Failed to update chat access: " + e.getMessage());
        }
    }

    @Override
    public List<Application> findApplicationsWithChatEnabled() {
        logger.info("Finding applications with chat enabled");

        try {
            List<Application> applications = applicationRepository.findByCanChatTrue();
            logger.info("Found {} applications with chat enabled", applications.size());
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications with chat enabled: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to find applications with chat enabled: " + e.getMessage());
        }
    }


    @Override
    public Integer countApplicationsByJob(Integer jobId) {
        logger.info("Counting applications for job with ID: {}", jobId);

        try {
            Integer count = applicationRepository.countByJob(jobId);
            logger.info("Counted {} applications for job with ID: {}", count, jobId);
            return count;
        } catch (Exception e) {
            logger.error("Error counting applications for job with ID {}: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to count applications by job ID: " + e.getMessage());
        }
    }

    @Override
    public List<Application> findApplicationsByDateRange(Date startDate, Date endDate) {
        logger.info("Finding applications between dates: {} and {}", startDate, endDate);

        try {
            List<Application> applications = applicationRepository.findByAppliedDateBetween(startDate, endDate);
            logger.info("Found {} applications between dates: {} and {}", applications.size(), startDate, endDate);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications between dates: {} and {}: {}", startDate, endDate, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by date range: " + e.getMessage());
        }
    }

    @Override
    public List<Application> findApplicationsByJobAndDateRange(Integer jobId, Date startDate, Date endDate) {
        logger.info("Finding applications for job with ID: {} between dates: {} and {}", jobId, startDate, endDate);

        try {
            List<Application> applications = applicationRepository.findByJobIdAndAppliedDateBetween(jobId, startDate, endDate);
            logger.info("Found {} applications for job with ID: {} between dates: {} and {}",
                    applications.size(), jobId, startDate, endDate);
            return applications;
        } catch (Exception e) {
            logger.error("Error finding applications for job with ID {} between dates: {} and {}: {}",
                    jobId, startDate, endDate, e.getMessage(), e);
            throw new BadRequestException("Failed to find applications by job ID and date range: " + e.getMessage());
        }
    }

    @Override
    public Integer countApplicationsByJobAndStatus(Integer jobId, ApplicationStatus status) {
        logger.info("Counting applications for job with ID: {} by status", jobId);

        try {
            Integer count = applicationRepository.countByJobAndStatus(jobId, status); // or any default status
            logger.info("Counted {} applications for job with ID: {} with status", count, jobId);
            return count;
        } catch (Exception e) {
            logger.error("Error counting applications for job with ID {} by status: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to count applications by job ID and status: " + e.getMessage());
        }
    }

    @Override
    public Integer countApplicationsByUser(Integer userId) {
        logger.info("Counting applications for user with ID: {}", userId);

        try {
            Integer count = applicationRepository.countByUser(userId);
            logger.info("Counted {} applications for user with ID: {}", count, userId);
            return count;
        } catch (Exception e) {
            logger.error("Error counting applications for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to count applications by user ID: " + e.getMessage());
        }
    }

    @Override
    public Integer countApplicationsByRecruiter(Integer recruiterId) {
        logger.info("Counting applications for recruiter with ID: {}", recruiterId);

        try {
            Integer count = applicationRepository.countByRecruiter(recruiterId);
            logger.info("Counted {} applications for recruiter with ID: {}", count, recruiterId);
            return count;
        } catch (Exception e) {
            logger.error("Error counting applications for recruiter with ID {}: {}", recruiterId, e.getMessage(), e);
            throw new BadRequestException("Failed to count applications by recruiter ID: " + e.getMessage());
        }
    }

}