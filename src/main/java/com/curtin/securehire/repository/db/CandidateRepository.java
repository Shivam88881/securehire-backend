package com.curtin.securehire.repository.db;

import com.curtin.securehire.entity.db.Role;
import com.curtin.securehire.entity.db.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Optional<Candidate> findByEmail(String email);
    List<Candidate> findByRole(Role role);
    Optional<Candidate> findByUsername(String username);
    @Query("SELECT c FROM Candidate c WHERE c.refreshToken = :refreshToken")
    Optional<Candidate> findByRefreshToken(String refreshToken);
}
