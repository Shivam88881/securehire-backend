package com.curtin.securehire.service.es;

import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.entity.es.RecruiterDocument;
import com.curtin.securehire.repository.es.RecruiterSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecruiterSearchService {

    @Autowired
    private RecruiterSearchRepository recruiterSearchRepository;

    public void indexRecruiter(Recruiter recruiter) {
        RecruiterDocument document = convertToDocument(recruiter);
        recruiterSearchRepository.save(document);
        log.info("Indexed recruiter with ID: {}", recruiter.getId());
    }

    public void indexRecruiters(List<Recruiter> recruiters) {
        List<RecruiterDocument> documents = recruiters.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        recruiterSearchRepository.saveAll(documents);
        log.info("Indexed {} recruiters", recruiters.size());
    }

    public List<RecruiterDocument> searchRecruiters(String query) {
        log.info("Searching recruiters with query: {}", query);
        return recruiterSearchRepository.findByCompanyNameContainingOrEmailContaining(query, query);
    }

    public List<RecruiterDocument> suggestRecruiters(String prefix) {
        log.info("Getting recruiter suggestions with prefix: {}", prefix);
        return recruiterSearchRepository.findByCompanyNameStartingWith(prefix);
    }

    private RecruiterDocument convertToDocument(Recruiter recruiter) {
        RecruiterDocument document = new RecruiterDocument();
        Integer id = recruiter.getId();
        document.setId(id.toString());
        document.setCompanyName(recruiter.getCompanyName());
        document.setEmail(recruiter.getEmail());
        document.setCompanyType(recruiter.getCompanyType() != null ? recruiter.getCompanyType().name() : null);
        document.setBusinessSector(recruiter.getBusinessSecTor() != null ? recruiter.getBusinessSecTor().name()  : null);
        return document;
    }
}
