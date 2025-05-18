package com.curtin.securehire.service.db.impl;



import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.entity.db.JobEmbedding;
import com.curtin.securehire.entity.db.UserEmbedding;
import com.curtin.securehire.repository.db.JobRepository;
import com.curtin.securehire.repository.db.JobEmbeddingRepository;
import com.curtin.securehire.repository.db.UserEmbeddingRepository;
import com.curtin.securehire.service.db.RecommendationService;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.embedding.EmbeddingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobEmbeddingRepository jobEmbeddingRepository;

    @Autowired
    private UserEmbeddingRepository userEmbeddingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Job> getRecommendedJobsForUser(Integer userId, int limit) {
        logger.info("Getting job recommendations for user ID: {}", userId);

        try {
            Optional<UserEmbedding> userEmbeddingOpt = userEmbeddingRepository.findById(userId);
            if (userEmbeddingOpt.isEmpty()) {
                logger.error("User embedding not found for user ID: {}", userId);
                throw new NotFoundException("User embedding not found for user ID: " + userId);
            }

            double[] userEmbedding = userEmbeddingOpt.get().getEmbedding();
            List<Object[]> similarityResults = jobEmbeddingRepository.findSimilarJobsByEmbedding(userEmbedding, limit);

            List<Integer> jobIds = similarityResults.stream()
                    .map(result -> ((Number) result[0]).intValue())
                    .collect(Collectors.toList());

            List<Job> recommendedJobs = jobRepository.findAllById(jobIds);

            // Maintain the order of the similarity results
            List<Job> orderedJobs = new ArrayList<>(recommendedJobs.size());
            for (Integer jobId : jobIds) {
                recommendedJobs.stream()
                        .filter(job -> job.getId() == jobId)
                        .findFirst()
                        .ifPresent(orderedJobs::add);
            }

            logger.info("Found {} recommended jobs for user ID: {}", orderedJobs.size(), userId);
            return orderedJobs;
        } catch (Exception e) {
            logger.error("Error getting job recommendations for user ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to get job recommendations: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Candidate> getRecommendedCandidatesForJob(Integer jobId, int limit) {
        // Similar implementation for candidate recommendations
        // ...
        return null; // Placeholder
    }

    @Override
    @Transactional
    public void generateEmbeddingsForJob(Job job) {
        logger.info("Generating embeddings for job ID: {}", job.getId());

        try {
            // Create text representation of job for embedding
            StringBuilder jobText = new StringBuilder();
            jobText.append(job.getTitle()).append(" ");
            jobText.append(job.getDescription()).append(" ");

            // Add skills
            if (job.getTechnicalSkills() != null) {
                job.getTechnicalSkills().forEach(skill ->
                        jobText.append(skill.getName()).append(" "));
            }

            if (job.getSoftSkills() != null) {
                job.getSoftSkills().forEach(skill ->
                        jobText.append(skill.getName()).append(" "));
            }

            // Add job type, salary range, etc.
            jobText.append(job.getJobType()).append(" ");
            jobText.append(job.getSalaryRange()).append(" ");
            jobText.append(job.getLocation()).append(" ");

            // Generate embedding using OpenAI API
            OpenAiService openAiService = new OpenAiService(openAiApiKey);
            EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                    .model("text-embedding-ada-002")
                    .input(List.of(jobText.toString()))
                    .build();

            double[] embedding = openAiService.createEmbeddings(embeddingRequest)
                    .getData().get(0).getEmbedding()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .toArray();

            // Save or update embedding
            JobEmbedding jobEmbedding = jobEmbeddingRepository.findById(job.getId())
                    .orElse(new JobEmbedding());
            jobEmbedding.setJobId(job.getId());
            jobEmbedding.setEmbedding(embedding);

            jobEmbeddingRepository.save(jobEmbedding);
            logger.info("Successfully generated and saved embedding for job ID: {}", job.getId());
        } catch (Exception e) {
            logger.error("Error generating embedding for job ID {}: {}", job.getId(), e.getMessage(), e);
            throw new BadRequestException("Failed to generate job embedding: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void generateEmbeddingsForUser(Candidate candidate) {
        // Similar implementation for generating user embeddings
        // ...
    }
}

