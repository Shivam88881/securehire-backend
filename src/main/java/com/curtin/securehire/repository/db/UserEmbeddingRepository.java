package com.curtin.securehire.repository.db;

import com.curtin.securehire.entity.db.UserEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmbeddingRepository extends JpaRepository<UserEmbedding, Integer> {

}