package com.curtin.securehire.service.db;

import com.curtin.securehire.entity.db.*;

import java.util.ArrayList;
import java.util.List;

public interface CandidateService {
    Candidate getProfile(Integer userId);
    Candidate updateProfile(Integer userId, Candidate candidate);
    void deleteUser(Integer userId);
    Candidate changePassword(Integer userId, String newPassword);
    List<Candidate> listAllUsers();
    Candidate findUserById(Integer userId);
    Candidate resetPassword(Integer userId, String newPassword);
    Candidate activateUser(Integer userId);
    Candidate deactivateUser(Integer userId);
    Candidate updateUserRole(Integer userId, Integer roleId);
    List<Candidate> getUsersByRole(Role role);
    Address addAddress(Integer userId, Address address);
    Address updateAddress(Integer userId, Address address);
    Resume addResume(Integer userId, Resume resume);
    Resume changeDefaultResume(Integer userId, Integer resumeId);
    void updateExpectedSalaryRange(Integer userId, double minSalary, double maxSalary);
    Skill addSkill(Integer userId, Skill skill);
    List<Skill> updateSkill(Integer userId, ArrayList<Integer> updatedSkillList);
    void updatePremiumUserStatus(Integer userId, boolean isPremium);
    Candidate findUserByRefreshToken(String refreshToken);
    Candidate updateUserByRefreshToken(String newRefreshToken, Candidate candidate);
}
