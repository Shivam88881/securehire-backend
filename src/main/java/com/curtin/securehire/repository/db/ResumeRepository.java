package com.curtin.securehire.repository.db;

import com.curtin.securehire.entity.db.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    // Find resumes by candidate ID
    List<Resume> findByCandidateId(Integer candidateId);

    // Find resumes by name containing
    List<Resume> findByNameContaining(String name);

    // Find resumes uploaded after a certain date
    List<Resume> findByUploadedDateAfter(Date date);

    // Find resumes by candidate ID and name containing
    List<Resume> findByCandidateIdAndNameContaining(Long candidateId, String name);

    // Count resumes by candidate ID
    Long countByCandidateId(Integer candidateId);

    // Custom query to find resumes with flexible filtering
    @Query("SELECT r FROM Resume r WHERE " +
            "(:candidateId IS NULL OR r.candidate.id = :candidateId) AND " +
            "(:name IS NULL OR r.name LIKE %:name%) AND " +
            "(:uploadedDate IS NULL OR r.uploadedDate >= :uploadedDate)")
    List<Resume> findResumesByFilters(
            @Param("candidateId") Integer candidateId,
            @Param("name") String name,
            @Param("startDate") Date uploadedDate);
}
