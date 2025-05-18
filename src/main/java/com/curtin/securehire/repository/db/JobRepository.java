package com.curtin.securehire.repository.db;

import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.constant.JobType;
import com.curtin.securehire.entity.db.Job;

import java.sql.Date;
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

    @Query("SELECT j FROM Job j WHERE j.salaryRange.min >= :min AND j.salaryRange.max <= :max")
    List<Job> findBySalaryRange(@Param("min") double min, @Param("max") double max);


//    List<Job> findBySalaryRange(Range salaryRange);

    List<Job> findByEmployementType(EmployementType employementType);

    /**
     * A custom query to find jobs by various filters. The filters are optional
     * and can be null. The query will return all jobs if no filters are
     * provided.
     *
     * @param locationId the id of the location
     * @param recruiterId the id of the recruiter
     * @param jobType the type of job
     * @param minSalary and maxSalary the salary range
     * @param employementType the employment type
     * @param startDate the start date for posted jobs
     * @param endDate the end date for posted jobs
     * @return a list of jobs that match the filters
     */
    @Query(value = """
    WITH RECURSIVE location_hierarchy AS (
        SELECT id FROM locations WHERE id = :locationId
        UNION ALL
        SELECT l.id FROM locations l
        INNER JOIN location_hierarchy lh ON l.parent = lh.id
    )
    SELECT * FROM jobs j
    WHERE (:locationId IS NULL OR j.location_id IN (SELECT id FROM location_hierarchy))
      AND (:recruiterId IS NULL OR j.recruiter_id = :recruiterId)
      AND (:jobType IS NULL OR j.job_type = CAST(:jobType AS TEXT))
      AND (:minSalary IS NULL OR j.salary_min >= :minSalary)
      AND (:maxSalary IS NULL OR j.salary_max <= :maxSalary)
      AND (:employementType IS NULL OR j.employement_type = CAST(:employementType AS TEXT))
      AND (:startDate IS NULL OR j.posted_date >= :startDate)
      AND (:endDate IS NULL OR j.posted_date <= :endDate)
    """, nativeQuery = true)
    List<Job> findByFilter(@Param("locationId") Integer locationId,
                           @Param("recruiterId") Integer recruiterId,
                           @Param("jobType") String jobType,
                           @Param("minSalary") double minSalary,
                           @Param("maxSalary") double maxSalary,
                           @Param("employementType") String employementType,
                           @Param("startDate") Date startDate,
                           @Param("endDate") Date endDate);


    @Query(value = """
    WITH RECURSIVE location_hierarchy AS (
        SELECT id FROM locations WHERE id = :locationId
        UNION ALL
        SELECT l.id FROM locations l
        JOIN location_hierarchy lh ON l.parent = lh.id
    )
    SELECT j.*, (SELECT COUNT(*) FROM job_technical_skills js WHERE js.job_id = j.id AND js.skill_id IN (:skillIds)) AS match_count
    FROM jobs j
    WHERE (:locationId IS NULL OR j.location_id IN (SELECT id FROM location_hierarchy))
      AND (:recruiterId IS NULL OR j.recruiter_id = :recruiterId)
      AND (:jobType IS NULL OR j.job_type = :jobType)
      AND ((:minSalary IS NULL AND :maxSalary IS NULL) OR 
           (j.salary_min >= :minSalary AND j.salary_max <= :maxSalary))
      AND (:employementType IS NULL OR j.employement_type = :employementType)
      AND (:startDate IS NULL OR j.deadline >= :startDate)
      AND (:endDate IS NULL OR j.deadline <= :endDate)
      AND (:skillIds IS NULL OR EXISTS (
           SELECT 1 FROM job_technical_skills js WHERE js.job_id = j.id AND js.skill_id IN (:skillIds)))
      AND (:minMatches IS NULL OR 
           (SELECT COUNT(*) FROM job_technical_skills js WHERE js.job_id = j.id AND js.skill_id IN (:skillIds)) >= :minMatches)
    ORDER BY match_count DESC
    """,
            nativeQuery = true)
    List<Object[]> findByFilterWithSkillsSorted(
            @Param("locationId") Integer locationId,
            @Param("recruiterId") Integer recruiterId,
            @Param("jobType") String jobType,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary,
            @Param("employementType") String employementType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("skillIds") List<Integer> skillIds,
            @Param("requiredAll") boolean requiredAll,
            @Param("minMatches") Integer minMatches);




    /**
     * Finds all jobs at a specified location and all its descendent locations (any depth).
     * Uses a PostgreSQL recursive query to handle hierarchies of any depth.
     *
     * @param locationId The ID of the parent location to search from
     * @return A list of all jobs at the specified location and its descendents
     */
    @Query(value = "WITH RECURSIVE location_hierarchy AS (" +
            "  SELECT id FROM locations WHERE id = :locationId " +
            "  UNION ALL " +
            "  SELECT l.id FROM locations l " +
            "  JOIN location_hierarchy lh ON l.parent = lh.id" +
            ") " +
            "SELECT j.* FROM jobs j " +
            "WHERE j.location_id IN (SELECT id FROM location_hierarchy)",
            nativeQuery = true)
    List<Job> findJobsByLocationHierarchy(@Param("locationId") Integer locationId);

}
