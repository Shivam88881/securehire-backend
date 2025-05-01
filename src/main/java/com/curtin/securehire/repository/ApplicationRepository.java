package com.curtin.securehire.repository;

import com.curtin.securehire.constant.AplicationStatus;
import com.curtin.securehire.entity.Aplication;
import com.curtin.securehire.entity.Recruiter;
import com.curtin.securehire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Aplication, Integer> {
    // Basic queries from existing repository
    List<Aplication> findByUser(Integer userId);
    List<Aplication> findByRecruiter(Integer recruiterId);
    List<Aplication> findByJobId(Integer jobId);
    List<Aplication> findByStatus(AplicationStatus status);

    // Additional queries for more functionality
    List<Aplication> findByUserIdOrderByCreatedAtDesc(Integer userId);
    List<Aplication> findByRecruiterIdOrderByCreatedAtDesc(Integer recruiterId);

    // Find by job and status
    List<Aplication> findByJobIdAndStatus(Integer jobId, AplicationStatus status);

    // Find by user and status
    List<Aplication> findByUserAndStatus(User user, AplicationStatus status);

    // Find by recruiter and status
    List<Aplication> findByRecruiterAndStatus(Recruiter recruiter, AplicationStatus status);

    // Find by date range
    List<Aplication> findByCreatedAtBetween(Date startDate, Date endDate);

    // Find by user and date range
    List<Aplication> findByUserAndCreatedAtBetween(User user, Date startDate, Date endDate);

    // Find applications that have chat enabled
    List<Aplication> findByCanChatTrue();

    // Find applications without assigned recruiter
    List<Aplication> findByRecruiterIsNull();

    // Count applications by job
    Long countByJobId(Integer jobId);

    // Count applications by status
    Long countByStatus(AplicationStatus status);

    // Count applications by job and status
    Long countByJobIdAndStatus(Integer jobId, AplicationStatus status);

    // Custom query with multiple filters
    @Query("SELECT a FROM Aplication a WHERE " +
            "(:jobId IS NULL OR a.jobId = :jobId) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:userId IS NULL OR a.user.id = :userId) AND " +
            "(:recruiterId IS NULL OR a.recruiter.id = :recruiterId)")
    List<Aplication> findApplicationsByFilters(
            @Param("jobId") Integer jobId,
            @Param("status") AplicationStatus status,
            @Param("userId") Integer userId,
            @Param("recruiterId") Integer recruiterId);

    // Find latest applications
    List<Aplication> findTop10ByOrderByCreatedAtDesc();

    // Find applications by status ordered by creation date
    List<Aplication> findByStatusOrderByCreatedAtDesc(AplicationStatus status);
}