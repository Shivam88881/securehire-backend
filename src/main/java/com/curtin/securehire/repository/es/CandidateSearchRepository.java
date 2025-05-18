package com.curtin.securehire.repository.es;

import com.curtin.securehire.entity.es.CandidateDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateSearchRepository extends ElasticsearchRepository<CandidateDocument, Integer> {
    List<CandidateDocument> findByFirstNameContainingOrLastNameContainingOrEmailContainingOrSkillsContaining(
            String firstName, String lastName, String email, String skills);

    List<CandidateDocument> findByFirstNameStartingWithOrLastNameStartingWith(String firstName, String lastName);
}
