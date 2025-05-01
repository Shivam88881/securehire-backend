package com.curtin.securehire.repository;

import com.curtin.securehire.constant.SkillSubType;
import com.curtin.securehire.constant.SkillType;
import com.curtin.securehire.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    // Find skills by name
    List<Skill> findByNameContaining(String name);

    // Find skills by type
    List<Skill> findByType(SkillType type);

    // Find skills by sub-type
    List<Skill> findBySubType(SkillSubType subType);

    // Find skills by type and sub-type
    List<Skill> findByTypeAndSubType(SkillType type, SkillSubType subType);

    // Find skills by exact name
    Skill findByName(String name);

    // Custom query to find skills with flexible filtering
    @Query("SELECT s FROM Skill s WHERE " +
            "(:name IS NULL OR s.name LIKE %:name%) AND " +
            "(:type IS NULL OR s.type = :type) AND " +
            "(:subType IS NULL OR s.subType = :subType)")
    List<Skill> findSkillsByFilters(
            @Param("name") String name,
            @Param("type") SkillType type,
            @Param("subType") SkillSubType subType);

    // Check if skill exists by name
    boolean existsByName(String name);
}
