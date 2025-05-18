// src/main/java/com/curtin/securehire/repository/RecruiterRepository.java
package com.curtin.securehire.repository.db;

import com.curtin.securehire.constant.BusinessSecTor;
import com.curtin.securehire.constant.CompanyType;
import com.curtin.securehire.entity.db.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {

    // Email-related queries
    Optional<Recruiter> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Recruiter> findByRefreshToken(String refreshToken);

    // Company name queries
    List<Recruiter> findByCompanyNameContainingIgnoreCase(String companyName);

    // Company type and business sector queries
    List<Recruiter> findByCompanyType(CompanyType companyType);
    List<Recruiter> findByBusinessSecTor(BusinessSecTor businessSector);

    // Block status queries
    List<Recruiter> findByIsBlocked(boolean isBlocked);

    // Search queries
    List<Recruiter> findByCompanyNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String companyName, String description);

    // Advanced queries
    @Query("SELECT r FROM Recruiter r WHERE " +
            "(:companyType IS NULL OR r.companyType = :companyType) AND " +
            "(:businessSector IS NULL OR r.businessSecTor = :businessSector) AND " +
            "(:minEmployees IS NULL OR r.NoOfEmployee.min >= :minEmployees) AND " +
            "(:maxEmployees IS NULL OR r.NoOfEmployee.max <= :maxEmployees)")
    List<Recruiter> findByFilters(
            @Param("companyType") CompanyType companyType,
            @Param("businessSector") BusinessSecTor businessSector,
            @Param("minEmployees") Integer minEmployees,
            @Param("maxEmployees") Integer maxEmployees);

    // Most recent recruiters
    List<Recruiter> findTop10ByOrderByIdDesc();

    // Recruiters with certain number of jobs
    @Query("SELECT r FROM Recruiter r WHERE SIZE(r.jobs) >= :minJobs")
    List<Recruiter> findByMinimumJobCount(@Param("minJobs") int minJobs);

    // Recruiters with website containing domain
    List<Recruiter> findByWebsiteContaining(String domain);
}
