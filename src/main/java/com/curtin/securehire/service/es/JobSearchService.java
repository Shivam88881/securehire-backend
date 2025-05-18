package com.curtin.securehire.service.es;

import com.curtin.securehire.entity.db.Job;
import com.curtin.securehire.entity.db.Skill;
import com.curtin.securehire.entity.es.JobDocument;
import com.curtin.securehire.repository.es.JobSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobSearchService {

    @Autowired
    private JobSearchRepository jobSearchRepository;

    /**
     * Index a single job in Elasticsearch
     *
     * @param job The job to index
     */
    public void indexJob(Job job) {
        JobDocument document = convertToDocument(job);
        jobSearchRepository.save(document);
        log.info("Indexed job with ID: {}", job.getId());
    }

    /**
     * Index multiple jobs in Elasticsearch
     *
     * @param jobs The list of jobs to index
     */
    public void indexJobs(List<Job> jobs) {
        List<JobDocument> documents = jobs.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        jobSearchRepository.saveAll(documents);
        log.info("Indexed {} jobs", jobs.size());
    }

    /**
     * Convert a Job entity to a JobDocument for Elasticsearch
     *
     * @param job The job entity to convert
     * @return The job document for Elasticsearch
     */
    public JobDocument convertToDocument(Job job) {
        JobDocument document = new JobDocument();
        Integer id = job.getId();
        document.setId(id.toString());
        document.setTitle(job.getTitle());
        document.setDescription(job.getDescription());
        document.setResponsibilities(job.getResponsibility());
        document.setRequirements(job.getRequirement());
        document.setCompanyName(job.getRecruiter().getCompanyName());
        document.setRecruiterId(job.getRecruiter().getId());

        // If job has location information
        if (job.getLocation() != null) {
            document.setLocation(job.getLocation().getName());
        }

        document.setEmploymentType(job.getEmployementType() != null ?
                job.getEmployementType().name() : null);

        List<Skill> allSkills =new ArrayList<>();

        if (job.getSoftSkills() != null && !job.getSoftSkills().isEmpty() ) {
            for(Skill skill : job.getSoftSkills()) {
                allSkills.add(skill);
            }
        }

        if(job.getTechnicalSkills() != null && !job.getTechnicalSkills().isEmpty()) {
            for(Skill skill : job.getTechnicalSkills()) {
                allSkills.add(skill);
            }
        }

        String skills = allSkills.stream()
                .map(Skill::getName)
                .collect(Collectors.joining(" "));
        document.setSkills(skills);

        document.setPostedDate(LocalDate.ofInstant(job.getPostedDate().toInstant(), null));
        document.setDeadlineDate(LocalDate.ofInstant(job.getDeadline().toInstant(), null));

        // Set salary range if available
        if (job.getSalaryRange() != null) {
            Double min = job.getSalaryRange().getMin();
            Double max = job.getSalaryRange().getMax();
            document.setSalaryRangeLow(min.intValue());
            document.setSalaryRangeHigh(max.intValue());
        }


        return document;
    }

    /**
     * Search for jobs based on a query string
     *
     * @param query The search query
     * @return List of matching job documents
     */
    public List<JobDocument> searchJobs(String query) {
        log.info("Searching jobs with query: {}", query);
        return jobSearchRepository.findByTitleContainingOrDescriptionContainingOrSkillsContaining(
                query, query, query);
    }

    /**
     * Get job suggestions based on title prefix
     *
     * @param prefix The title prefix to search for
     * @return List of matching job documents
     */
    public List<JobDocument> suggestJobs(String prefix) {
        log.info("Getting job suggestions with prefix: {}", prefix);
        return jobSearchRepository.findByTitleStartingWith(prefix);
    }
}
