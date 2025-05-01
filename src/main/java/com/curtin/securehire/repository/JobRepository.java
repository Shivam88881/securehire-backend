package com.curtin.securehire.repository;

import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.constant.JobType;
import com.curtin.securehire.entity.Job;
import com.curtin.securehire.entity.Range;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {


    List<Job> findByRecruiterId(int recruiterId);

    List<Job> findByLocationId(int locationId);

    List<Job> findByJobType(JobType jobType);

    List<Job> findBySalaryRange(Range salaryRange);

    List<Job> findByEmployementType(EmployementType employmentType);



    /**
     * A custom query to find jobs by various filters. The filters are optional
     * and can be null. The query will return all jobs if no filters are
     * provided.
     *
     * @param locationId the id of the location
     * @param recruiterId the id of the recruiter
     * @param jobType the type of job
     * @param salaryRange the salary range
     * @param employmentType the employment type
     * @return a list of jobs that match the filters
     */
    @Query("SELECT j FROM Job j " +
            "WHERE (:locationId IS NULL OR j.location.id = :locationId) " +
            "AND (:recruiterId IS NULL OR j.recruiter.id = :recruiterId) " +
            "AND (:jobType IS NULL OR j.jobType = :jobType) " +
            "AND (:salaryRange IS NULL OR j.salaryRange = :salaryRange) " +
            "AND (:employmentType IS NULL OR j.employementType = :employmentType)")
    List<Job> findByFilter(@Param("locationId") Integer locationId,
                         @Param("recruiterId") Integer recruiterId,
                         @Param("jobType") JobType jobType,
                         @Param("salaryRange") Range salaryRange,
                         @Param("employmentType") EmployementType employmentType);

}
