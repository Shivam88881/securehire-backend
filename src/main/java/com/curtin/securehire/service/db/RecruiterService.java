// src/main/java/com/curtin/securehire/service/RecruiterService.java
package com.curtin.securehire.service.db;

import com.curtin.securehire.constant.BusinessSecTor;
import com.curtin.securehire.constant.CompanyType;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.entity.db.Job;

import java.util.List;

public interface RecruiterService {

    // auth service
    Recruiter signup(Recruiter recruiter);

    Recruiter login(String email, String password);

    // Basic CRUD operations
    Recruiter findById(Integer recruiterId);
    List<Recruiter> findAll();
    Recruiter save(Recruiter recruiter);
    Recruiter update(Integer recruiterId, Recruiter recruiter);
    void delete(Integer recruiterId);

    // Email operations
    Recruiter findByEmail(String email);
    Recruiter getProfile(Integer recruiterId);

    Recruiter updateProfile(Integer recruiterId, Recruiter recruiter);

    boolean existsByEmail(String email);

    boolean changePassword(Integer recruiterId, String oldPassword, String newPassword);

    // Company-related operations
    List<Recruiter> findByCompanyName(String companyName);
    List<Recruiter> findByCompanyType(CompanyType companyType);
    List<Recruiter> findByBusinessSector(BusinessSecTor businessSector);

    // Job-related operations
    List<Job> getRecruiterJobs(Integer recruiterId);
    Job postJob(Integer recruiterId, Job job);

    // Account management
    void blockRecruiter(Integer recruiterId, boolean block);
    void updateRefreshToken(Integer recruiterId, String refreshToken);
    boolean verifyPassword(Integer recruiterId, String password);

    Recruiter findRequiterByRefreshToken(String refreshToken);
    // Search and filters
    List<Recruiter> searchRecruiters(String keyword);
    List<Recruiter> findByFilter(CompanyType companyType, BusinessSecTor businessSector,
                                 Integer minEmployees, Integer maxEmployees);
}
