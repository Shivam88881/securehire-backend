// Skill.java
package com.curtin.securehire.entity;

import com.curtin.securehire.constant.SkillSubType;
import com.curtin.securehire.constant.SkillType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "skills")
@Data
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Enumerated(EnumType.STRING)
    private SkillType type;

    @Enumerated(EnumType.STRING)
    private SkillSubType subType;
}
