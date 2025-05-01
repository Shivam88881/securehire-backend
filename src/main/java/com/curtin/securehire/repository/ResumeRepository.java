package com.curtin.securehire.repository;

import com.curtin.securehire.entity.Resume;
import com.curtin.securehire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    // Find resumes by user
    List<Resume> findByUser(User user);

    // Find resumes by user ID
    List<Resume> findByUserId(Integer userId);

    // Find resumes by name containing
    List<Resume> findByNameContaining(String name);

    // Find resumes uploaded after a certain date
    List<Resume> findByUploadedDateAfter(Date date);

    // Find resumes by user ID and name containing
    List<Resume> findByUserIdAndNameContaining(Integer userId, String name);

    // Count resumes by user ID
    Long countByUserId(Integer userId);

    // Custom query to find resumes with flexible filtering
    @Query("SELECT r FROM Resume r WHERE " +
            "(:userId IS NULL OR r.user.id = :userId) AND " +
            "(:name IS NULL OR r.name LIKE %:name%) AND " +
            "(:startDate IS NULL OR r.uploadedDate >= :startDate)")
    List<Resume> findResumesByFilters(
            @Param("userId") Integer userId,
            @Param("name") String name,
            @Param("startDate") Date startDate);
}
