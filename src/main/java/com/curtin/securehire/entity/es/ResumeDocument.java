package com.curtin.securehire.entity.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "resumes")
public class ResumeDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Date)
    private LocalDate uploadedDate;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String skills;

    @Field(type = FieldType.Keyword)
    private String fileType;

    @Field(type = FieldType.Boolean)
    private Boolean isDefault;

    @Field(type = FieldType.Integer)
    private Integer fileSize;
}