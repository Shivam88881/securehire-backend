package com.curtin.securehire.controller;

import com.curtin.securehire.entity.es.CandidateDocument;
import com.curtin.securehire.entity.es.LocationDocument;
import com.curtin.securehire.entity.es.RecruiterDocument;
import com.curtin.securehire.entity.es.SkillDocument;
import com.curtin.securehire.service.es.CandidateSearchService;
import com.curtin.securehire.service.es.LocationSearchService;
import com.curtin.securehire.service.es.RecruiterSearchService;
import com.curtin.securehire.service.es.SkillSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/search")
public class EsSearchController {

    @Autowired
    private CandidateSearchService candidateSearchService;

    @Autowired
    private RecruiterSearchService recruiterSearchService;

    @Autowired
    private SkillSearchService skillSearchService;

    @Autowired
    private LocationSearchService locationSearchService;

    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateDocument>> searchCandidates(@RequestParam String query) {
        log.info("Received request to search candidates with query: {}", query);
        List<CandidateDocument> results = candidateSearchService.searchCandidates(query);
        log.info("Found {} candidates matching query: {}", results.size(), query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/candidates/suggest")
    public ResponseEntity<List<CandidateDocument>> suggestCandidates(@RequestParam String prefix) {
        log.info("Received request for candidate suggestions with prefix: {}", prefix);
        List<CandidateDocument> suggestions = candidateSearchService.suggestCandidates(prefix);
        log.info("Found {} candidate suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/recruiters")
    public ResponseEntity<List<RecruiterDocument>> searchRecruiters(@RequestParam String query) {
        log.info("Received request to search recruiters with query: {}", query);
        List<RecruiterDocument> results = recruiterSearchService.searchRecruiters(query);
        log.info("Found {} recruiters matching query: {}", results.size(), query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/recruiters/suggest")
    public ResponseEntity<List<RecruiterDocument>> suggestRecruiters(@RequestParam String prefix) {
        log.info("Received request for recruiter suggestions with prefix: {}", prefix);
        List<RecruiterDocument> suggestions = recruiterSearchService.suggestRecruiters(prefix);
        log.info("Found {} recruiter suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/skills")
    public ResponseEntity<List<SkillDocument>> searchSkills(@RequestParam String query) {
        log.info("Received request to search skills with query: {}", query);
        List<SkillDocument> results = skillSearchService.searchSkills(query);
        log.info("Found {} skills matching query: {}", results.size(), query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/skills/suggest")
    public ResponseEntity<List<SkillDocument>> suggestSkills(@RequestParam String prefix) {
        log.info("Received request for skill suggestions with prefix: {}", prefix);
        List<SkillDocument> suggestions = skillSearchService.suggestSkills(prefix);
        log.info("Found {} skill suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDocument>> searchLocations(@RequestParam String query) {
        log.info("Received request to search locations with query: {}", query);
        List<LocationDocument> results = locationSearchService.searchLocations(query);
        log.info("Found {} locations matching query: {}", results.size(), query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/locations/suggest")
    public ResponseEntity<List<LocationDocument>> suggestLocations(@RequestParam String prefix) {
        log.info("Received request for location suggestions with prefix: {}", prefix);
        List<LocationDocument> suggestions = locationSearchService.suggestLocations(prefix);
        log.info("Found {} location suggestions for prefix: {}", suggestions.size(), prefix);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/all")
    public ResponseEntity<SearchResult> searchAll(@RequestParam String query) {
        log.info("Received request to search all entities with query: {}", query);

        List<CandidateDocument> candidates = candidateSearchService.searchCandidates(query);
        List<RecruiterDocument> recruiters = recruiterSearchService.searchRecruiters(query);
        List<SkillDocument> skills = skillSearchService.searchSkills(query);
        List<LocationDocument> locations = locationSearchService.searchLocations(query);

        SearchResult result = new SearchResult(candidates, recruiters, skills, locations);

        log.info("Found {} results across all entities for query: {}",
                (candidates.size() + recruiters.size() + skills.size() + locations.size()), query);

        return ResponseEntity.ok(result);
    }

    // Inner class to hold combined search results
    private static class SearchResult {
        private final List<CandidateDocument> candidates;
        private final List<RecruiterDocument> recruiters;
        private final List<SkillDocument> skills;
        private final List<LocationDocument> locations;

        public SearchResult(List<CandidateDocument> candidates, List<RecruiterDocument> recruiters,
                            List<SkillDocument> skills, List<LocationDocument> locations) {
            this.candidates = candidates;
            this.recruiters = recruiters;
            this.skills = skills;
            this.locations = locations;
        }

        public List<CandidateDocument> getCandidates() {
            return candidates;
        }

        public List<RecruiterDocument> getRecruiters() {
            return recruiters;
        }

        public List<SkillDocument> getSkills() {
            return skills;
        }

        public List<LocationDocument> getLocations() {
            return locations;
        }
    }
}