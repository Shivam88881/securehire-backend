package com.curtin.securehire.controller;

import com.curtin.securehire.entity.db.Skill;
import com.curtin.securehire.constant.SkillType;
import com.curtin.securehire.constant.SkillSubType;
import com.curtin.securehire.service.db.SkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping("/{skillId}")
    public ResponseEntity<Skill> findById(@PathVariable Integer skillId) {
        log.info("Entering findById method with skillId: {}", skillId);
        Skill skill = skillService.findById(skillId);
        log.info("Exiting findById method. Retrieved skill: {}", skill);
        return ResponseEntity.ok(skill);
    }

    @GetMapping
    public ResponseEntity<List<Skill>> findAll() {
        log.info("Entering findAll method.");
        List<Skill> skills = skillService.findAll();
        log.info("Exiting findAll method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @PostMapping
    public ResponseEntity<Skill> save(@RequestBody Skill skill) {
        log.info("Entering save method with skill details: {}", skill);
        Skill savedSkill = skillService.save(skill);
        log.info("Exiting save method. Saved skill: {}", savedSkill);
        return ResponseEntity.ok(savedSkill);
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<Skill> update(@PathVariable Integer skillId, @RequestBody Skill skill) {
        log.info("Entering update method with skillId: {} and updated details: {}", skillId, skill);
        Skill updatedSkill = skillService.update(skillId, skill);
        log.info("Exiting update method. Updated skill: {}", updatedSkill);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> delete(@PathVariable Integer skillId) {
        log.info("Entering delete method with skillId: {}", skillId);
        skillService.delete(skillId);
        log.info("Exiting delete method. Skill deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Skill>> findByNameContaining(@RequestParam String name) {
        log.info("Entering findByNameContaining method with name fragment: {}", name);
        List<Skill> skills = skillService.findByNameContaining(name);
        log.info("Exiting findByNameContaining method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Skill>> findByType(@PathVariable SkillType type) {
        log.info("Entering findByType method with type: {}", type);
        List<Skill> skills = skillService.findByType(type);
        log.info("Exiting findByType method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/subtype/{subType}")
    public ResponseEntity<List<Skill>> findBySubType(@PathVariable SkillSubType subType) {
        log.info("Entering findBySubType method with subType: {}", subType);
        List<Skill> skills = skillService.findBySubType(subType);
        log.info("Exiting findBySubType method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/type-subtype")
    public ResponseEntity<List<Skill>> findByTypeAndSubType(@RequestParam SkillType type, @RequestParam SkillSubType subType) {
        log.info("Entering findByTypeAndSubType method with type: {} and subType: {}", type, subType);
        List<Skill> skills = skillService.findByTypeAndSubType(type, subType);
        log.info("Exiting findByTypeAndSubType method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Skill> findByName(@PathVariable String name) {
        log.info("Entering findByName method with skill name: {}", name);
        Skill skill = skillService.findByName(name);
        log.info("Exiting findByName method. Retrieved skill: {}", skill);
        return ResponseEntity.ok(skill);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Skill> addSkillToUser(@PathVariable Integer userId, @RequestBody Skill skill) {
        log.info("Entering addSkillToUser method with userId: {} and skill details: {}", userId, skill);
        Skill addedSkill = skillService.addSkillToUser(userId, skill);
        log.info("Exiting addSkillToUser method. Added skill: {}", addedSkill);
        return ResponseEntity.ok(addedSkill);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<Skill> updateUserSkill(@PathVariable Integer userId, @RequestBody Skill skill) {
        log.info("Entering updateUserSkill method with userId: {} and skill details: {}", userId, skill);
        Skill updatedSkill = skillService.updateUserSkill(userId, skill);
        log.info("Exiting updateUserSkill method. Updated skill: {}", updatedSkill);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/user/{userId}/{skillId}")
    public ResponseEntity<Void> removeSkillFromUser(@PathVariable Integer userId, @PathVariable Integer skillId) {
        log.info("Entering removeSkillFromUser method with userId: {} and skillId: {}", userId, skillId);
        skillService.removeSkillFromUser(userId, skillId);
        log.info("Exiting removeSkillFromUser method. Skill removed successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Skill>> getUserSkills(@PathVariable Integer userId) {
        log.info("Entering getUserSkills method with userId: {}", userId);
        List<Skill> skills = skillService.getUserSkills(userId);
        log.info("Exiting getUserSkills method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/job/{jobId}/technical")
    public ResponseEntity<List<Skill>> getJobTechnicalSkills(@PathVariable Integer jobId) {
        log.info("Entering getJobTechnicalSkills method with jobId: {}", jobId);
        List<Skill> skills = skillService.getJobTechnicalSkills(jobId);
        log.info("Exiting getJobTechnicalSkills method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/job/{jobId}/soft")
    public ResponseEntity<List<Skill>> getJobSoftSkills(@PathVariable Integer jobId) {
        log.info("Entering getJobSoftSkills method with jobId: {}", jobId);
        List<Skill> skills = skillService.getJobSoftSkills(jobId);
        log.info("Exiting getJobSoftSkills method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Skill>> findByFilters(@RequestParam(required = false) String name, @RequestParam(required = false) SkillType type, @RequestParam(required = false) SkillSubType subType) {
        log.info("Entering findByFilters method with parameters - Name: {}, Type: {}, SubType: {}", name, type, subType);
        List<Skill> skills = skillService.findByFilters(name, type, subType);
        log.info("Exiting findByFilters method. Retrieved {} skills.", skills.size());
        return ResponseEntity.ok(skills);
    }
}
