package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.constant.RoleName;
import com.curtin.securehire.dto.RegisterRequest;
import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.entity.db.Role;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.db.CandidateRepository;
import com.curtin.securehire.repository.db.RecruiterRepository;
import com.curtin.securehire.repository.db.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    public String register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new NotFoundException("Role not found"));
        System.out.println("registering with role = "+request.getRole());
        if (RoleName.CANDIDATE.equals(request.getRole())) {
            Candidate candidate = new Candidate();
            candidate.setName(request.getName());
            candidate.setEmail(request.getEmail());
            candidate.setPhone(request.getPhone());
            candidate.setPassword(encodedPassword);
            candidate.setRole(role);
            candidateRepository.save(candidate);
            return "Candidate registered successfully";
        } else if (RoleName.RECRUITER.equals(request.getRole())) {
            System.out.println("trying to register recruiter");
            Recruiter recruiter = new Recruiter();
            recruiter.setCompanyName(request.getCompanyName());
            recruiter.setEmail(request.getEmail());
            recruiter.setPhone(request.getPhone());
            recruiter.setPassword(encodedPassword);
            recruiter.setRole(role);
            recruiterRepository.save(recruiter);
            return "Recruiter registered successfully";
        } else {
            throw new BadRequestException("Invalid role");
        }
    }
}


