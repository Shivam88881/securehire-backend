package com.curtin.securehire.repository.es;

import com.curtin.securehire.entity.es.ResumeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeSearchRepository extends ElasticsearchRepository<ResumeDocument, String> {

    List<ResumeDocument> findByContentContainingOrNameContainingOrSkillsContaining(
            String contentText, String nameText, String skillsText);

    List<ResumeDocument> findByNameStartingWith(String prefix);

    List<ResumeDocument> findByUserId(String userId);
}
