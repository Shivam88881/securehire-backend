package com.curtin.securehire.service.es;

import com.curtin.securehire.entity.db.Resume;
import com.curtin.securehire.entity.es.ResumeDocument;
import com.curtin.securehire.repository.es.ResumeSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResumeSearchService {

    @Autowired
    private ResumeSearchRepository resumeSearchRepository;

    public void indexResume(Resume resume) {
        ResumeDocument document = convertToDocument(resume);
        resumeSearchRepository.save(document);
        log.info("Indexed resume with ID: {}", resume.getId());
    }

    public void indexResumes(List<Resume> resumes) {
        List<ResumeDocument> documents = resumes.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        resumeSearchRepository.saveAll(documents);
        log.info("Indexed {} resumes", resumes.size());
    }

    public ResumeDocument convertToDocument(Resume resume) {
        ResumeDocument document = new ResumeDocument();
        Integer id = resume.getId();
        document.setId(id.toString());
        document.setName(resume.getName());
        Integer userId = resume.getCandidate().getId();
        document.setUserId(userId.toString());
        document.setUploadedDate(resume.getUploadedDate().toLocalDate());

        return document;
    }

    public List<ResumeDocument> searchResumes(String query) {
        log.info("Searching resumes with query: {}", query);
        return resumeSearchRepository.findByContentContainingOrNameContainingOrSkillsContaining(
                query, query, query);
    }

    public List<ResumeDocument> suggestResumes(String prefix) {
        log.info("Getting resume suggestions with prefix: {}", prefix);
        return resumeSearchRepository.findByNameStartingWith(prefix);
    }
}
