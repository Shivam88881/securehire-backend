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
@Document(indexName = "jobs")
public class JobDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String responsibilities;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String requirements;

    @Field(type = FieldType.Keyword)
    private String companyName;

    @Field(type = FieldType.Integer)
    private Integer recruiterId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String location;

    @Field(type = FieldType.Keyword)
    private String employmentType;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String skills;

    @Field(type = FieldType.Date)
    private LocalDate postedDate;

    @Field(type = FieldType.Date)
    private LocalDate deadlineDate;

    @Field(type = FieldType.Integer)
    private Integer salaryRangeLow;

    @Field(type = FieldType.Integer)
    private Integer salaryRangeHigh;

    @Field(type = FieldType.Keyword)
    private String status;
}
