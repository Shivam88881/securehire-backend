package com.curtin.securehire.controller;

import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.service.db.CandidateService;
import com.curtin.securehire.service.db.JobService;
import com.curtin.securehire.service.db.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@Validated
public class RecommendationController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/users/{userId}/jobs")
    public ResponseEntity<List<Job>> getRecommendedJobsForUser(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId,
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("Received request to get recommended jobs for user with ID: {}, limit: {}", userId, limit);
        List<Job> recommendedJobs = recommendationService.getRecommendedJobsForUser(userId, limit);
        logger.info("Returning {} recommended jobs for user with ID: {}", recommendedJobs.size(), userId);
        return ResponseEntity.ok(recommendedJobs);
    }

    @GetMapping("/jobs/{jobId}/candidates")
    public ResponseEntity<List<Candidate>> getRecommendedCandidatesForJob(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") Integer jobId,
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("Received request to get recommended candidates for job with ID: {}, limit: {}", jobId, limit);
        List<Candidate> recommendedCandidates = recommendationService.getRecommendedCandidatesForJob(jobId, limit);
        logger.info("Returning {} recommended candidates for job with ID: {}", recommendedCandidates.size(), jobId);
        return ResponseEntity.ok(recommendedCandidates);
    }

    @PostMapping("/jobs/{jobId}/generate-embedding")
    public ResponseEntity<Void> generateJobEmbedding(
            @PathVariable @Min(value = 1, message = "Job ID must be positive") Integer jobId) {
        logger.info("Received request to generate embedding for job with ID: {}", jobId);
        // Get the job entity first
        Job job = jobService.getJobById(jobId);
        recommendationService.generateEmbeddingsForJob(job);
        logger.info("Generated embedding for job with ID: {}", jobId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/generate-embedding")
    public ResponseEntity<Void> generateUserEmbedding(
            @PathVariable @Min(value = 1, message = "User ID must be positive") Integer userId) {
        logger.info("Received request to generate embedding for user with ID: {}", userId);
        // Get the candidate entity first
        Candidate candidate = candidateService.getProfile(userId);
        recommendationService.generateEmbeddingsForUser(candidate);
        logger.info("Generated embedding for user with ID: {}", userId);
        return ResponseEntity.ok().build();
    }
}
