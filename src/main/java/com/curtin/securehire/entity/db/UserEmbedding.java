package com.curtin.securehire.entity.db;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "user_embeddings")
@Data
@Getter
@Setter
public class UserEmbedding {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_id",  referencedColumnName = "id",insertable = false, updatable = false)
    private Candidate candidate;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "embedding", columnDefinition = "vector(1536)")
    private double[] embedding;

}
