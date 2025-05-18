package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.constant.RoleName;
import com.curtin.securehire.dto.LoginRequest;
import com.curtin.securehire.dto.LoginResponse;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.repository.db.RecruiterRepository;
import com.curtin.securehire.repository.db.CandidateRepository;
import com.curtin.securehire.security.MyUserDetails;
import com.curtin.securehire.security.MyUserDetailsService;
import com.curtin.securehire.security.jwt.JwtUtil;
import com.curtin.securehire.service.db.CandidateService;
import com.curtin.securehire.service.db.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private RecruiterService recruiterService;

    public LoginResponse login(LoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

            String jwtToken = jwtUtil.generateToken(userDetails);
            String refreshJwtToken = jwtUtil.generateRefreshToken(userDetails);

            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            if(RoleName.CANDIDATE.getValue().equals(role)) {
                Candidate candidate = candidateRepository.findByEmail(userDetails.getUsername()).get();
                candidateService.updateUserByRefreshToken(refreshJwtToken, candidate);
            } else if (RoleName.RECRUITER.getValue().equals(role)) {
                Recruiter recruiter = recruiterRepository.findByEmail(userDetails.getUsername()).get();
                recruiterService.updateRefreshToken( recruiter.getId(),refreshJwtToken);
                recruiterRepository.save(recruiter);
            }

            return new LoginResponse(jwtToken,refreshJwtToken ,userDetails.getAuthorities().iterator().next().getAuthority(), userDetails.getUsername());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage() != null ? e.getMessage() : "Invalid username or password.");
        }
    }

}
