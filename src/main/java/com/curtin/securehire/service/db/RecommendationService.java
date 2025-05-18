package com.curtin.securehire.service.db;


import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.entity.db.Candidate;
import java.util.List;

public interface RecommendationService {
    List<Job> getRecommendedJobsForUser(Integer userId, int limit);
    List<Candidate> getRecommendedCandidatesForJob(Integer jobId, int limit);
    void generateEmbeddingsForJob(Job job);
    void generateEmbeddingsForUser(Candidate candidate);
}

