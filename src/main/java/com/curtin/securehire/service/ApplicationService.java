// src/main/java/com/curtin/securehire/service/ApplicationService.java
package com.curtin.securehire.service;

import com.curtin.securehire.constant.AplicationStatus;
import com.curtin.securehire.entity.Aplication;
import com.curtin.securehire.entity.Recruiter;
import com.curtin.securehire.entity.User;

import java.util.List;

public interface ApplicationService {
    Aplication findById(Integer applicationId);
    List<Aplication> findAll();
    Aplication save(Aplication application);
    Aplication update(Integer applicationId, Aplication application);
    void delete(Integer applicationId);

    // Additional functionality
    List<Aplication> findByUserID(Integer userId);
    List<Aplication> findByRecruiterId(Integer recruiterId);
    List<Aplication> findByJobId(Integer jobId);
    List<Aplication> findByStatus(AplicationStatus status);
    Aplication updateStatus(Integer applicationId, AplicationStatus status);
    Aplication assignRecruiter(Integer applicationId, Recruiter recruiter);
    Aplication toggleChatAccess(Integer applicationId, boolean canChat);
}
