package com.curtin.securehire.service.es;

import com.curtin.securehire.entity.db.Skill;
import com.curtin.securehire.entity.es.SkillDocument;
import com.curtin.securehire.repository.es.SkillSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SkillSearchService {

    @Autowired
    private SkillSearchRepository skillSearchRepository;

    public void indexSkill(Skill skill) {
        SkillDocument document = convertToDocument(skill);
        skillSearchRepository.save(document);
        log.info("Indexed skill with ID: {}", skill.getId());
    }

    public void indexSkills(List<Skill> skills) {
        List<SkillDocument> documents = skills.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        skillSearchRepository.saveAll(documents);
        log.info("Indexed {} skills", skills.size());
    }

    public List<SkillDocument> searchSkills(String query) {
        log.info("Searching skills with query: {}", query);
        return skillSearchRepository.findByNameContaining(query);
    }

    public List<SkillDocument> suggestSkills(String prefix) {
        log.info("Getting skill suggestions with prefix: {}", prefix);
        return skillSearchRepository.findByNameStartingWith(prefix);
    }

    private SkillDocument convertToDocument(Skill skill) {
        SkillDocument document = new SkillDocument();
        document.setId(skill.getId());
        document.setName(skill.getName());
        document.setType(skill.getType() != null ? skill.getType().name() : null);
        document.setSubType(skill.getSubType() != null ? skill.getSubType().name() : null);
        return document;
    }
}
