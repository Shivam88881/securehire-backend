package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.constant.JobType;
import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.entity.db.Range;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.db.JobRepository;
import com.curtin.securehire.service.db.JobService;
import com.curtin.securehire.service.db.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RecommendationService recommendationService;

    @Override
    public Job createJob(Job job) {
        logger.info("Creating new job: {}", job.getTitle());
        if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
            logger.error("Failed to create job: Title is required");
            throw new BadRequestException("Job title is required");
        }
        try {
            Job savedJob =   jobRepository.save(job);
            // Trigger embedding generation asynchronously
            CompletableFuture.runAsync(() -> recommendationService.generateEmbeddingsForJob(savedJob));
            return savedJob;
        } catch (Exception e) {
            logger.error("Error creating job: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create job: " + e.getMessage());
        }
    }

    @Override
    public Job getJobById(int jobId) {
        logger.info("Fetching job with ID: {}", jobId);
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isEmpty()) {
            logger.error("Job not found with ID: {}", jobId);
            throw new NotFoundException("Job not found with ID: " + jobId);
        }
        return job.get();
    }

    @Override
    public List<Job> getAllJobs() {
        logger.info("Fetching all jobs");
        try {
            return jobRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching all jobs: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch all jobs: " + e.getMessage());
        }
    }

    @Override
    public Job updateJob(int jobId, Job updatedJob) {
        logger.info("Updating job with ID: {}", jobId);
        Optional<Job> existingJobOpt = jobRepository.findById(jobId);
        if (existingJobOpt.isEmpty()) {
            logger.error("Cannot update job: Job not found with ID: {}", jobId);
            throw new NotFoundException("Job not found with ID: " + jobId);
        }

        try {
            Job existingJob = existingJobOpt.get();

            // Update the fields
            if (updatedJob.getTitle() != null) existingJob.setTitle(updatedJob.getTitle());
            if (updatedJob.getDescription() != null) existingJob.setDescription(updatedJob.getDescription());
            if (updatedJob.getRequirement() != null) existingJob.setRequirement(updatedJob.getRequirement());
            if (updatedJob.getResponsibility() != null) existingJob.setResponsibility(updatedJob.getResponsibility());
            if (updatedJob.getLocation() != null) existingJob.setLocation(updatedJob.getLocation());
            if (updatedJob.getEmployementType() != null) existingJob.setEmployementType(updatedJob.getEmployementType());
            if (updatedJob.getTechnicalSkills() != null) existingJob.setTechnicalSkills(updatedJob.getTechnicalSkills());
            if (updatedJob.getSoftSkills() != null) existingJob.setSoftSkills(updatedJob.getSoftSkills());
            if (updatedJob.getRecruiter() != null) existingJob.setRecruiter(updatedJob.getRecruiter());
            if (updatedJob.getMaxApplicants() > 0) existingJob.setMaxApplicants(updatedJob.getMaxApplicants());
            if (updatedJob.getSalaryRange() != null) existingJob.setSalaryRange(updatedJob.getSalaryRange());
            if (updatedJob.getJobType() != null) existingJob.setJobType(updatedJob.getJobType());
            if (updatedJob.getPostedDate() != null) existingJob.setPostedDate(updatedJob.getPostedDate());
            if (updatedJob.getDeadline() != null) existingJob.setDeadline(updatedJob.getDeadline());

            return jobRepository.save(existingJob);
        } catch (Exception e) {
            logger.error("Error updating job with ID {}: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to update job: " + e.getMessage());
        }
    }

    @Override
    public void deleteJob(int jobId) {
        logger.info("Deleting job with ID: {}", jobId);
        if (!jobRepository.existsById(jobId)) {
            logger.error("Cannot delete job: Job not found with ID: {}", jobId);
            throw new NotFoundException("Job not found with ID: " + jobId);
        }

        try {
            jobRepository.deleteById(jobId);
            logger.info("Successfully deleted job with ID: {}", jobId);
        } catch (Exception e) {
            logger.error("Error deleting job with ID {}: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete job: " + e.getMessage());
        }
    }

    @Override
    public List<Job> getJobsByRecruiterId(int recruiterId) {
        logger.info("Fetching jobs for recruiter with ID: {}", recruiterId);
        try {
            List<Job> jobs = jobRepository.findByRecruiterId(recruiterId);
            logger.info("Found {} jobs for recruiter with ID: {}", jobs.size(), recruiterId);
            return jobs;
        } catch (Exception e) {
            logger.error("Error fetching jobs for recruiter with ID {}: {}", recruiterId, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch jobs by recruiter: " + e.getMessage());
        }
    }

    @Override
    public List<Job> getJobsByJobType(JobType jobType) {
        logger.info("Fetching jobs with job type: {}", jobType);
        try {
            List<Job> jobs = jobRepository.findByJobType(jobType);
            logger.info("Found {} jobs with job type: {}", jobs.size(), jobType);
            return jobs;
        } catch (Exception e) {
            logger.error("Error fetching jobs with job type {}: {}", jobType, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch jobs by job type: " + e.getMessage());
        }
    }

    @Override
    public List<Job> getJobsBySalaryRange(Range salaryRange) {
        logger.info("Fetching jobs with salary range: min={}, max={}", salaryRange.getMin(), salaryRange.getMax());
        try {
            List<Job> jobs = jobRepository.findBySalaryRange(salaryRange.getMin(), salaryRange.getMax());
            logger.info("Found {} jobs matching salary range", jobs.size());
            return jobs;
        } catch (Exception e) {
            logger.error("Error fetching jobs by salary range: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch jobs by salary range: " + e.getMessage());
        }
    }

    @Override
    public List<Job> getJobsByEmploymentType(EmployementType employmentType) {
        logger.info("Fetching jobs with employment type: {}", employmentType);
        try {
            List<Job> jobs = jobRepository.findByEmployementType(employmentType);
            logger.info("Found {} jobs with employment type: {}", jobs.size(), employmentType);
            return jobs;
        } catch (Exception e) {
            logger.error("Error fetching jobs with employment type {}: {}", employmentType, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch jobs by employment type: " + e.getMessage());
        }
    }

    @Override
    public List<Job> getJobsByFilter(int locationId, int recruiterId, JobType jobType, Range salaryRange, EmployementType employmentType, Date startDate, Date endDate) {
        logger.info("Fetching jobs with filters - locationId: {}, recruiterId: {}, jobType: {}, employmentType: {}",
                locationId, recruiterId, jobType, employmentType);
        try {
            List<Job> jobs = jobRepository.findByFilter(locationId, recruiterId, jobType.toString(), salaryRange.getMin(), salaryRange.getMax(), employmentType.toString(), startDate, endDate);
            logger.info("Found {} jobs matching filter criteria", jobs.size());
            return jobs;
        } catch (Exception e) {
            logger.error("Error fetching jobs with filters: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch jobs by filters: " + e.getMessage());
        }
    }

    @Override
    public List<Job> getJobsByFilterWithSkills(
            Integer locationId,
            Integer recruiterId,
            JobType jobType,
            Range salaryRange,
            EmployementType employementType,
            Date startDate,
            Date endDate,
            List<Integer> skillIds,
            boolean requiredAll,
            Integer minMatches) {

        logger.info("Filtering jobs with skills. requireAll: {}, skillIds: {}", minMatches, skillIds);

        Double minSalary = (salaryRange != null) ? salaryRange.getMin() : null;
        Double maxSalary = (salaryRange != null) ? salaryRange.getMax() : null;

        List<Object[]> results = jobRepository.findByFilterWithSkillsSorted(
                locationId, recruiterId, jobType.toString(), minSalary, maxSalary, employementType.toString(),
                startDate, endDate, skillIds, requiredAll,minMatches);


        // Extract just the Job entities from the results
        return results.stream()
                .map(result -> (Job) result[0])
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> getJobsByLocationHierarchy(Integer locationId) {
        logger.info("Fetching jobs for location hierarchy starting at ID: {}", locationId);

        if (locationId == null) {
            return getAllJobs();
        }

        return jobRepository.findJobsByLocationHierarchy(locationId);
    }
}
