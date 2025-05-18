package com.curtin.securehire.service.es;

import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.entity.es.CandidateDocument;
import com.curtin.securehire.repository.es.CandidateSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CandidateSearchService {

    @Autowired
    private CandidateSearchRepository candidateSearchRepository;

    public void indexCandidate(Candidate candidate) {
        CandidateDocument document = convertToDocument(candidate);
        candidateSearchRepository.save(document);
        log.info("Indexed candidate with ID: {}", candidate.getId());
    }

    public void indexCandidates(List<Candidate> candidates) {
        List<CandidateDocument> documents = candidates.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        candidateSearchRepository.saveAll(documents);
        log.info("Indexed {} candidates", candidates.size());
    }

    public List<CandidateDocument> searchCandidates(String query) {
        log.info("Searching candidates with query: {}", query);
        return candidateSearchRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingOrSkillsContaining(
                query, query, query, query);
    }

    public List<CandidateDocument> suggestCandidates(String prefix) {
        log.info("Getting candidate suggestions with prefix: {}", prefix);
        return candidateSearchRepository.findByFirstNameStartingWithOrLastNameStartingWith(prefix, prefix);
    }

    private CandidateDocument convertToDocument(Candidate candidate) {
        CandidateDocument document = new CandidateDocument();
        Integer id = candidate.getId();
        document.setId(id.toString());
        String[] names = candidate.getName().split(" ");
        if(names.length > 1) {
            document.setFirstName(names[0]);
            document.setLastName(names[1]);
        } if(names.length == 1) document.setLastName("");
        if(names.length == 0) document.setFirstName("");
        document.setEmail(candidate.getEmail());

        // Concatenate skills for search
        if (candidate.getSkills() != null && !candidate.getSkills().isEmpty()) {
            String skills = candidate.getSkills().stream()
                    .map(skill -> skill.getName())
                    .collect(Collectors.joining(" "));
            document.setSkills(skills);
        }

        document.setRole(candidate.getRole() != null ? candidate.getRole().getName().getValue() : null);
        return document;
    }
}
