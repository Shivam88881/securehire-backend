package com.curtin.securehire.controller;

import com.curtin.securehire.entity.es.CandidateDocument;
import com.curtin.securehire.entity.es.JobDocument;
import com.curtin.securehire.entity.es.SkillDocument;
import com.curtin.securehire.entity.es.LocationDocument;
import com.curtin.securehire.service.es.CandidateSearchService;
import com.curtin.securehire.service.es.JobSearchService;
import com.curtin.securehire.service.es.SkillSearchService;
import com.curtin.securehire.service.es.LocationSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for providing autocomplete and suggestion functionality
 * across different entity types in the application.
 */
@RestController
@RequestMapping("/api/suggestions")
@Slf4j
public class SuggestionController {

    @Autowired
    private CandidateSearchService candidateSearchService;

    @Autowired
    private SkillSearchService skillSearchService;

    @Autowired
    private LocationSearchService locationSearchService;

    @Autowired
    private JobSearchService jobSearchService;

    /**
     * Get candidate suggestions based on name prefix
     *
     * @param prefix The text prefix to search for
     * @return List of matching candidate documents
     */
    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateDocument>> suggestCandidates(@RequestParam String prefix) {
        log.info("Received request for candidate suggestions with prefix: {}", prefix);
        List<CandidateDocument> suggestions = candidateSearchService.suggestCandidates(prefix);
        log.info("Found {} candidate suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Get skill suggestions based on name prefix
     *
     * @param prefix The text prefix to search for
     * @return List of matching skill documents
     */
    @GetMapping("/skills")
    public ResponseEntity<List<SkillDocument>> suggestSkills(@RequestParam String prefix) {
        log.info("Received request for skill suggestions with prefix: {}", prefix);
        List<SkillDocument> suggestions = skillSearchService.suggestSkills(prefix);
        log.info("Found {} skill suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Get location suggestions based on name prefix
     *
     * @param prefix The text prefix to search for
     * @return List of matching location documents
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationDocument>> suggestLocations(@RequestParam String prefix) {
        log.info("Received request for location suggestions with prefix: {}", prefix);
        List<LocationDocument> suggestions = locationSearchService.suggestLocations(prefix);
        log.info("Found {} location suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Get job suggestions based on title prefix
     *
     * @param prefix The text prefix to search for
     * @return List of matching job documents
     */
    @GetMapping("/jobs")
    public ResponseEntity<List<JobDocument>> suggestJobs(@RequestParam String prefix) {
        log.info("Received request for job suggestions with prefix: {}", prefix);
        List<JobDocument> suggestions = jobSearchService.suggestJobs(prefix);
        log.info("Found {} job suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Get all types of suggestions in a single request
     *
     * @param prefix The text prefix to search for
     * @return Map containing suggestions for all entity types
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> suggestAll(@RequestParam String prefix) {
        log.info("Received request for all suggestions with prefix: {}", prefix);

        Map<String, Object> allSuggestions = new HashMap<>();

        List<CandidateDocument> candidateSuggestions = candidateSearchService.suggestCandidates(prefix);
        List<SkillDocument> skillSuggestions = skillSearchService.suggestSkills(prefix);
        List<LocationDocument> locationSuggestions = locationSearchService.suggestLocations(prefix);
        List<JobDocument> jobSuggestions = jobSearchService.suggestJobs(prefix);

        allSuggestions.put("candidates", candidateSuggestions);
        allSuggestions.put("skills", skillSuggestions);
        allSuggestions.put("locations", locationSuggestions);
        allSuggestions.put("jobs", jobSuggestions);

        log.info("Found suggestions for prefix '{}': {} candidates, {} skills, {} locations, {} jobs",
                prefix,
                candidateSuggestions.size(),
                skillSuggestions.size(),
                locationSuggestions.size(),
                jobSuggestions.size());

        return ResponseEntity.ok(allSuggestions);
    }
}
