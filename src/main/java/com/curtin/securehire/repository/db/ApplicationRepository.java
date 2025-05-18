package com.curtin.securehire.repository.db;

import com.curtin.securehire.constant.ApplicationStatus;
import com.curtin.securehire.entity.db.Application;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.entity.db.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    // Basic queries from existing repository
    @Query("SELECT a FROM Application a WHERE a.candidate.id = :candidateId")
    List<Application> findByUser(Integer candidateId);
    @Query("SELECT a FROM Application a WHERE a.recruiter.id = :recruiterId")
    List<Application> findByRecruiter(Integer recruiterId);
    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId")
    List<Application> findByJob(@Param("jobId") Integer jobId);
    List<Application> findByStatus(ApplicationStatus status);

    // Additional queries for more functionality
    @Query("SELECT a FROM Application a WHERE a.candidate.id = :candidateId ORDER BY a.appliedDate DESC")
    List<Application> findByUserOrderByAppliedDateDesc(Integer candidateId);
    @Query("SELECT a FROM Application a WHERE a.recruiter.id = :recruiterId ORDER BY a.appliedDate DESC")
    List<Application> findByRecruiterOrderByAppliedDateDesc(Integer recruiterId);

    // Find by job and status
    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId AND a.status = :status")
    List<Application> findByJobAndStatus(Integer jobId, ApplicationStatus status);

    // Find by candidate and status
    @Query("SELECT a FROM Application a WHERE a.candidate.id = :candidate AND a.status = :status")
    List<Application> findByUserAndStatus(Candidate candidate, ApplicationStatus status);

    // Find by recruiter and status
    @Query("SELECT a FROM Application a WHERE a.recruiter.id = :recruiter AND a.status = :status")
    List<Application> findByRecruiterAndStatus(Recruiter recruiter, ApplicationStatus status);

    // Find by candidate and date range
    @Query("SELECT a FROM Application a WHERE a.candidate.id = :candidate AND a.appliedDate BETWEEN :startDate AND :endDate")
    List<Application> findByUserAndAppliedDateBetween(Candidate candidate, Date startDate, Date endDate);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.id = :jobId")
    Integer countByJob(@Param("jobId") Integer jobId);

    // Count applications by status
    Long countByStatus(ApplicationStatus status);

    // Count applications by job and status
    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.id = :jobId AND a.status = :status")
    Integer countByJobAndStatus(Integer jobId, ApplicationStatus status);

    // Custom query with multiple filters
    @Query("SELECT a FROM Application a WHERE " +
            "(:jobId IS NULL OR a.job.id = :jobId) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:candidateId IS NULL OR a.candidate.id = :candidateId) AND " +
            "(:recruiterId IS NULL OR a.recruiter.id = :recruiterId)")
    List<Application> findApplicationsByFilters(
            @Param("jobId") Optional<Integer> jobId,
            @Param("status") Optional<ApplicationStatus> status,
            @Param("candidateId") Optional<Integer> candidateId,
            @Param("recruiterId") Optional<Integer> recruiterId);

    List<Application> findByCanChatTrue();

    List<Application> findByAppliedDateBetween(Date startDate, Date endDate);

    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId AND a.appliedDate BETWEEN :startDate AND :endDate")
    List<Application> findByJobIdAndAppliedDateBetween(Integer jobId, Date startDate, Date endDate);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.id = :jobId AND a.status = :status")
    Integer countByJobAndStatus(Integer jobId, String status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.candidate.id = :candidateId")
    Integer countByUser(Integer candidateId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.recruiter.id = :recruiterId")
    Integer countByRecruiter(Integer recruiterId);

}