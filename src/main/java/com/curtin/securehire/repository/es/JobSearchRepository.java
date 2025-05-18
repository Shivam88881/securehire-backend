package com.curtin.securehire.repository.es;

import com.curtin.securehire.entity.es.JobDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSearchRepository extends ElasticsearchRepository<JobDocument, String> {

    List<JobDocument> findByTitleContainingOrDescriptionContainingOrSkillsContaining(
            String title, String description, String skills);

    List<JobDocument> findByTitleStartingWith(String prefix);

    List<JobDocument> findByRecruiterId(Integer recruiterId);

    List<JobDocument> findByEmploymentType(String employmentType);

    List<JobDocument> findByStatusAndDeadlineDateGreaterThanEqual(String status, java.time.LocalDate date);
}
