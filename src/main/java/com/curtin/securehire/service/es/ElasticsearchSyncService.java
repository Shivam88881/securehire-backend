package com.curtin.securehire.service.es;

import com.curtin.securehire.entity.db.*;
import com.curtin.securehire.repository.db.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.List;

@Slf4j
@Service
public class ElasticsearchSyncService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CandidateSearchService candidateSearchService;

    @Autowired
    private RecruiterSearchService recruiterSearchService;

    @Autowired
    private SkillSearchService skillSearchService;

    @Autowired
    private LocationSearchService locationSearchService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSearchService jobSearchService;

    @PostConstruct
    public void initialIndex() {
        log.info("Starting initial Elasticsearch indexing");
        syncCandidates();
        syncRecruiters();
        syncSkills();
        syncLocations();
        log.info("Completed initial Elasticsearch indexing");
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run every midnight
    public void syncJobs() {
        List<Job> jobs = jobRepository.findAll();
        jobSearchService.indexJobs(jobs);
        log.info("Synced {} jobs to Elasticsearch", jobs.size());
    }


    @Scheduled(cron = "0 0 0 * * ?") // Run every midnight
    public void scheduledSync() {
        log.info("Starting scheduled Elasticsearch sync");
        syncCandidates();
        syncRecruiters();
        syncSkills();
        syncLocations();
        log.info("Completed scheduled Elasticsearch sync");
    }

    public void syncCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        candidateSearchService.indexCandidates(candidates);
        log.info("Synced {} candidates to Elasticsearch", candidates.size());
    }

    public void syncRecruiters() {
        List<Recruiter> recruiters = recruiterRepository.findAll();
        recruiterSearchService.indexRecruiters(recruiters);
        log.info("Synced {} recruiters to Elasticsearch", recruiters.size());
    }

    public void syncSkills() {
        List<Skill> skills = skillRepository.findAll();
        skillSearchService.indexSkills(skills);
        log.info("Synced {} skills to Elasticsearch", skills.size());
    }

    public void syncLocations() {
        List<Location> locations = locationRepository.findAll();
        locationSearchService.indexLocations(locations);
        log.info("Synced {} locations to Elasticsearch", locations.size());
    }
}
