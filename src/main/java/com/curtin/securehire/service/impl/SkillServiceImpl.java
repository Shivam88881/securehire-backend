package com.curtin.securehire.service.impl;

import com.curtin.securehire.constant.SkillSubType;
import com.curtin.securehire.constant.SkillType;
import com.curtin.securehire.entity.Job;
import com.curtin.securehire.entity.Skill;
import com.curtin.securehire.entity.User;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.JobRepository;
import com.curtin.securehire.repository.SkillRepository;
import com.curtin.securehire.repository.UserRepository;
import com.curtin.securehire.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillServiceImpl.class);

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public Skill findById(Integer skillId) {
        logger.info("Finding skill by ID: {}", skillId);

        Optional<Skill> skillOpt = skillRepository.findById(skillId);
        if (skillOpt.isEmpty()) {
            logger.error("Skill not found with ID: {}", skillId);
            throw new NotFoundException("Skill not found with ID: " + skillId);
        }

        logger.info("Found skill with ID: {}", skillId);
        return skillOpt.get();
    }

    @Override
    public List<Skill> findAll() {
        logger.info("Fetching all skills");

        try {
            List<Skill> skills = skillRepository.findAll();
            logger.info("Fetched {} skills", skills.size());
            return skills;
        } catch (Exception e) {
            logger.error("Error fetching all skills: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch skills: " + e.getMessage());
        }
    }

    @Override
    public Skill save(Skill skill) {
        logger.info("Creating new skill: {}", skill.getName());

        try {
            Skill savedSkill = skillRepository.save(skill);
            logger.info("Skill created successfully with ID: {}", savedSkill.getId());
            return savedSkill;
        } catch (Exception e) {
            logger.error("Error creating skill: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create skill: " + e.getMessage());
        }
    }

    @Override
    public Skill update(Integer skillId, Skill skill) {
        logger.info("Updating skill with ID: {}", skillId);

        if (!skillRepository.existsById(skillId)) {
            logger.error("Cannot update skill: Skill not found with ID: {}", skillId);
            throw new NotFoundException("Skill not found with ID: " + skillId);
        }

        try {
            skill.setId(skillId);
            Skill updatedSkill = skillRepository.save(skill);
            logger.info("Skill updated successfully with ID: {}", skillId);
            return updatedSkill;
        } catch (Exception e) {
            logger.error("Error updating skill with ID {}: {}", skillId, e.getMessage(), e);
            throw new BadRequestException("Failed to update skill: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer skillId) {
        logger.info("Deleting skill with ID: {}", skillId);

        if (!skillRepository.existsById(skillId)) {
            logger.error("Cannot delete skill: Skill not found with ID: {}", skillId);
            throw new NotFoundException("Skill not found with ID: " + skillId);
        }

        try {
            skillRepository.deleteById(skillId);
            logger.info("Skill deleted successfully with ID: {}", skillId);
        } catch (Exception e) {
            logger.error("Error deleting skill with ID {}: {}", skillId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete skill: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> findByNameContaining(String name) {
        logger.info("Finding skills with name containing: {}", name);

        try {
            List<Skill> skills = skillRepository.findByNameContaining(name);
            logger.info("Found {} skills with name containing: {}", skills.size(), name);
            return skills;
        } catch (Exception e) {
            logger.error("Error finding skills with name containing {}: {}", name, e.getMessage(), e);
            throw new BadRequestException("Failed to find skills by name: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> findByType(SkillType type) {
        logger.info("Finding skills with type: {}", type);

        try {
            List<Skill> skills = skillRepository.findByType(type);
            logger.info("Found {} skills with type: {}", skills.size(), type);
            return skills;
        } catch (Exception e) {
            logger.error("Error finding skills with type {}: {}", type, e.getMessage(), e);
            throw new BadRequestException("Failed to find skills by type: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> findBySubType(SkillSubType subType) {
        logger.info("Finding skills with sub-type: {}", subType);

        try {
            List<Skill> skills = skillRepository.findBySubType(subType);
            logger.info("Found {} skills with sub-type: {}", skills.size(), subType);
            return skills;
        } catch (Exception e) {
            logger.error("Error finding skills with sub-type {}: {}", subType, e.getMessage(), e);
            throw new BadRequestException("Failed to find skills by sub-type: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> findByTypeAndSubType(SkillType type, SkillSubType subType) {
        logger.info("Finding skills with type: {} and sub-type: {}", type, subType);

        try {
            List<Skill> skills = skillRepository.findByTypeAndSubType(type, subType);
            logger.info("Found {} skills with type: {} and sub-type: {}", skills.size(), type, subType);
            return skills;
        } catch (Exception e) {
            logger.error("Error finding skills with type {} and sub-type {}: {}", type, subType, e.getMessage(), e);
            throw new BadRequestException("Failed to find skills by type and sub-type: " + e.getMessage());
        }
    }

    @Override
    public Skill findByName(String name) {
        logger.info("Finding skill with exact name: {}", name);

        try {
            Skill skill = skillRepository.findByName(name);
            if (skill != null) {
                logger.info("Found skill with name: {}", name);
            } else {
                logger.info("No skill found with name: {}", name);
            }
            return skill;
        } catch (Exception e) {
            logger.error("Error finding skill with name {}: {}", name, e.getMessage(), e);
            throw new BadRequestException("Failed to find skill by name: " + e.getMessage());
        }
    }

    @Override
    public Skill addSkillToUser(Integer userId, Skill skill) {
        logger.info("Adding skill '{}' for user with ID: {}", skill.getName(), userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot add skill: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            // Check if skill already exists
            Skill existingSkill = null;
            if (skill.getId() != 0) {
                existingSkill = skillRepository.findById(skill.getId()).orElse(null);
            } else if (skill.getName() != null && !skill.getName().trim().isEmpty()) {
                existingSkill = skillRepository.findByName(skill.getName());
            }

            // If skill doesn't exist, create a new one
            Skill savedSkill = existingSkill != null ? existingSkill : skillRepository.save(skill);

            User user = userOpt.get();
            // Check if user already has this skill
            boolean skillExists = user.getSkills().stream()
                    .anyMatch(s -> s.getId() == savedSkill.getId());

            if (!skillExists) {
                user.getSkills().add(savedSkill);
                userRepository.save(user);
            }

            logger.info("Skill added successfully for user with ID: {}", userId);
            return savedSkill;
        } catch (Exception e) {
            logger.error("Error adding skill for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to add skill: " + e.getMessage());
        }
    }

    @Override
    public Skill updateUserSkill(Integer userId, Skill skill) {
        logger.info("Updating skill with ID {} for user with ID: {}", skill.getId(), userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot update skill: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            Skill savedSkill = skillRepository.save(skill);
            logger.info("Skill updated successfully for user with ID: {}", userId);
            return savedSkill;
        } catch (Exception e) {
            logger.error("Error updating skill for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to update skill: " + e.getMessage());
        }
    }

    @Override
    public void removeSkillFromUser(Integer userId, Integer skillId) {
        logger.info("Removing skill with ID {} from user with ID: {}", skillId, userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot remove skill: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        Optional<Skill> skillOpt = skillRepository.findById(skillId);
        if (skillOpt.isEmpty()) {
            logger.error("Cannot remove skill: Skill not found with ID: {}", skillId);
            throw new NotFoundException("Skill not found with ID: " + skillId);
        }

        try {
            User user = userOpt.get();
            user.getSkills().removeIf(s -> s.getId() == skillId);
            userRepository.save(user);
            logger.info("Skill removed successfully from user with ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error removing skill from user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to remove skill: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> getUserSkills(Integer userId) {
        logger.info("Fetching skills for user with ID: {}", userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Cannot fetch skills: User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        try {
            List<Skill> skills = userOpt.get().getSkills();
            logger.info("Fetched {} skills for user with ID: {}", skills.size(), userId);
            return skills;
        } catch (Exception e) {
            logger.error("Error fetching skills for user with ID {}: {}", userId, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch user skills: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> getJobTechnicalSkills(Integer jobId) {
        logger.info("Fetching technical skills for job with ID: {}", jobId);

        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            logger.error("Cannot fetch skills: Job not found with ID: {}", jobId);
            throw new NotFoundException("Job not found with ID: " + jobId);
        }

        try {
            List<Skill> skills = jobOpt.get().getTechnicalSkills();
            logger.info("Fetched {} technical skills for job with ID: {}", skills.size(), jobId);
            return skills;
        } catch (Exception e) {
            logger.error("Error fetching technical skills for job with ID {}: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch job technical skills: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> getJobSoftSkills(Integer jobId) {
        logger.info("Fetching soft skills for job with ID: {}", jobId);

        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            logger.error("Cannot fetch skills: No job found with ID: {}", jobId);
            throw new NotFoundException("Job not found with ID: " + jobId);
        }

        try {
            List<Skill> skills = jobOpt.get().getSoftSkills();
            logger.info("Fetched {} soft skills for job with ID: {}", skills.size(), jobId);
            return skills;
        } catch (Exception e) {
            logger.error("Error fetching soft skills for job with ID {}: {}", jobId, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch job soft skills: " + e.getMessage());
        }
    }

    @Override
    public List<Skill> findByFilters(String name, SkillType type, SkillSubType subType) {
        logger.info("Finding skills with filters - name: {}, type: {}, subType: {}", name, type, subType);

        try {
            List<Skill> skills = skillRepository.findSkillsByFilters(name, type, subType);
            logger.info("Found {} skills matching filters", skills.size());
            return skills;
        } catch (Exception e) {
            logger.error("Error finding skills with filters: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to find skills with filters: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByName(String name) {
        logger.info("Checking if skill exists with name: {}", name);

        try {
            boolean exists = skillRepository.existsByName(name);
            logger.info("Skill with name {} exists: {}", name, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if skill exists by name {}: {}", name, e.getMessage(), e);
            throw new BadRequestException("Failed to check if skill exists: " + e.getMessage());
        }
    }

    @Override
    public Skill findOrCreate(String name, SkillType type, SkillSubType subType) {
        logger.info("Finding or creating skill with name: {}, type: {}, subType: {}", name, type, subType);

        try {
            Skill existingSkill = skillRepository.findByName(name);
            if (existingSkill != null) {
                logger.info("Found existing skill with name: {}", name);
                return existingSkill;
            }

            // Create new skill
            Skill newSkill = new Skill();
            newSkill.setName(name);
            newSkill.setType(type);
            newSkill.setSubType(subType);

            Skill savedSkill = skillRepository.save(newSkill);
            logger.info("Created new skill with name: {} and ID: {}", name, savedSkill.getId());
            return savedSkill;
        } catch (Exception e) {
            logger.error("Error finding or creating skill with name {}: {}", name, e.getMessage(), e);
            throw new BadRequestException("Failed to find or create skill: " + e.getMessage());
        }
    }
}
