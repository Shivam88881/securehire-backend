package com.curtin.securehire.entity.es;


import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.entity.db.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "candidates")
public class CandidateDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String firstName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String lastName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String email;

    @Field(type = FieldType.Text)
    private String skills; // Concatenated skills for search

    @Field(type = FieldType.Keyword)
    private String role;


    // Candidate document conversion for Elasticsearch indexing
    public CandidateDocument convertToDocument(Candidate candidate) {
        CandidateDocument document = new CandidateDocument();
        document.setId(candidate.getId().toString());
        String[] names = candidate.getName().split(" ");

        if (names.length > 1) {
            document.setFirstName(names[0]);
            document.setLastName(names[1]);
        } else {
            document.setFirstName(names[0]);
        }

        document.setEmail(candidate.getEmail());

        // Skills concatenation for better searchability
        if (candidate.getSkills() != null && !candidate.getSkills().isEmpty()) {
            String skills = candidate.getSkills().stream()
                    .map(Skill::getName)
                    .collect(Collectors.joining(" "));
            document.setSkills(skills);
        }
        return document;
    }

}

