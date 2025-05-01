package com.curtin.securehire.service;

import com.curtin.securehire.entity.*;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User signup(User user);
    User signin(String email, String password);
    User getProfile(Integer userId);
    User updateProfile(Integer userId, User user);
    void deleteUser(Integer userId);
    User changePassword(Integer userId, String newPassword);
    List<User> listAllUsers();
    User findUserById(Integer userId);
    User resetPassword(Integer userId, String newPassword);
    User activateUser(Integer userId);
    User deactivateUser(Integer userId);
    User updateUserRole(Integer userId, Integer roleId);
    List<User> getUsersByRole(Role role);
    Address addAddress(Integer userId, Address address);
    Address updateAddress(Integer userId, Address address);
    Resume addResume(Integer userId, Resume resume);
    Resume changeDefaultResume(Integer userId, Integer resumeId);
    void updateExpectedSalaryRange(Integer userId, double minSalary, double maxSalary);
    Skill addSkill(Integer userId, Skill skill);
    List<Skill> updateSkill(Integer userId, ArrayList<Integer> updatedSkillList);
    void updatePremiumUserStatus(Integer userId, boolean isPremium);
}
