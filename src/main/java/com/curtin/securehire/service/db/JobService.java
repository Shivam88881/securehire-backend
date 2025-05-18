// JobService.java
package com.curtin.securehire.service.db;

import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.constant.JobType;
import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.entity.db.Range;

import java.sql.Date;
import java.util.List;

public interface JobService {
    Job createJob(Job job);
    Job getJobById(int jobId);
    List<Job> getAllJobs();
    Job updateJob(int jobId, Job updatedJob);
    void deleteJob(int jobId);
    List<Job> getJobsByRecruiterId(int recruiterId); // get job posted by specefic recruiter
    List<Job> getJobsByJobType(JobType jobType); // remote, onsite, hybrid, off-site
    List<Job> getJobsBySalaryRange(Range salaryRange);
    List<Job> getJobsByEmploymentType(EmployementType employmentType); // full-time, part-time, contract, internship
    // filter by location, recruiter, job type, salary range, employment type and date range. any parameter can be null and will return filtered results based on the other parameters
    List<Job> getJobsByFilter(int locationId, int recruiterId,JobType jobType, Range salaryRange, EmployementType employmentType, Date startDate, Date endDate);
    List<Job> getJobsByFilterWithSkills(
            Integer locationId,
            Integer recruiterId,
            JobType jobType,
            Range salaryRange,
            EmployementType employementType,
            Date startDate,
            Date endDate,
            List<Integer> skillIds,
            boolean requireAll,
            Integer minMatches
    );

    // Location hierarchy search
    List<Job> getJobsByLocationHierarchy(Integer locationId);
}