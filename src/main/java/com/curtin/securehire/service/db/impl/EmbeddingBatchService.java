package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.service.db.RecommendationService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EmbeddingBatchService {
    @Autowired
    private RecommendationService recommendationService;
    @Async
    public CompletableFuture<Void> batchProcessJobs(List<Job> jobs) {
        // Process in smaller batches
        for (List<Job> batch : Lists.partition(jobs, 100)) {
            batch.forEach(job -> recommendationService.generateEmbeddingsForJob(job));
        }
        return CompletableFuture.completedFuture(null);
    }
}

