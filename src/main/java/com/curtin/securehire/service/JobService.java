// JobService.java
package com.curtin.securehire.service;

import com.curtin.securehire.constant.EmployementType;
import com.curtin.securehire.constant.JobType;
import com.curtin.securehire.entity.Job;
import com.curtin.securehire.entity.Range;

import java.util.List;

public interface JobService {
    Job createJob(Job job);
    Job getJobById(int jobId);
    List<Job> getAllJobs();
    Job updateJob(int jobId, Job updatedJob);
    void deleteJob(int jobId);
    List<Job> getJobsByRecruiterId(int recruiterId); // get job posted by specefic recruiter
    List<Job> getJobsByLocationId(int locationId); // get job by location
    List<Job> getJobsByJobType(JobType jobType); // remote, onsite, hybrid, off-site
    List<Job> getJobsBySalaryRange(Range salaryRange);
    List<Job> getJobsByEmploymentType(EmployementType employmentType); // full-time, part-time, contract, internship
    // filter by location, recruiter, job type, salary range, employment type. any parameter can be null and will return filtered results based on the other parameters
    List<Job> getJobsByFilter(int locationId, int recruiterId,JobType jobType, Range salaryRange, EmployementType employmentType);
}
