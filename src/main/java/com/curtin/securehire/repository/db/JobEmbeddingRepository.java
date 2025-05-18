package com.curtin.securehire.repository.db;

import com.curtin.securehire.entity.db.JobEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobEmbeddingRepository extends JpaRepository<JobEmbedding, Integer> {
    @Query(value =
            "SELECT j.job_id, j.embedding <=> :userEmbedding AS distance " +
                    "FROM job_embeddings j " +
                    "ORDER BY distance " +
                    "LIMIT :limit",
            nativeQuery = true)
    List<Object[]> findSimilarJobsByEmbedding(@Param("userEmbedding") double[] userEmbedding, @Param("limit") int limit);
}
