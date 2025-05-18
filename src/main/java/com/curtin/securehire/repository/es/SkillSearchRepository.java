package com.curtin.securehire.repository.es;

import com.curtin.securehire.entity.es.SkillDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillSearchRepository extends ElasticsearchRepository<SkillDocument, Integer> {
    List<SkillDocument> findByNameContaining(String name);

    List<SkillDocument> findByNameStartingWith(String prefix);
}
