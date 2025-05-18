package com.curtin.securehire.repository.es;

import com.curtin.securehire.entity.es.RecruiterDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruiterSearchRepository extends ElasticsearchRepository<RecruiterDocument, Integer> {
    List<RecruiterDocument> findByCompanyNameContainingOrEmailContaining(String companyName, String email);

    List<RecruiterDocument> findByCompanyNameStartingWith(String companyName);
}
