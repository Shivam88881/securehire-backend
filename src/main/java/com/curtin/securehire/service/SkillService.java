package com.curtin.securehire.service;

import com.curtin.securehire.constant.SkillSubType;
import com.curtin.securehire.constant.SkillType;
import com.curtin.securehire.entity.Skill;
import java.util.List;

public interface SkillService {
    // Basic CRUD operations
    Skill findById(Integer skillId);
    List<Skill> findAll();
    Skill save(Skill skill);
    Skill update(Integer skillId, Skill skill);
    void delete(Integer skillId);

    // Specialized queries
    List<Skill> findByNameContaining(String name);
    List<Skill> findByType(SkillType type);
    List<Skill> findBySubType(SkillSubType subType);
    List<Skill> findByTypeAndSubType(SkillType type, SkillSubType subType);
    Skill findByName(String name);

    // User-related operations
    Skill addSkillToUser(Integer userId, Skill skill);
    Skill updateUserSkill(Integer userId, Skill skill);
    void removeSkillFromUser(Integer userId, Integer skillId);
    List<Skill> getUserSkills(Integer userId);

    // Job-related operations
    List<Skill> getJobTechnicalSkills(Integer jobId);
    List<Skill> getJobSoftSkills(Integer jobId);

    // Advanced operations
    List<Skill> findByFilters(String name, SkillType type, SkillSubType subType);
    boolean existsByName(String name);
    Skill findOrCreate(String name, SkillType type, SkillSubType subType);
}
