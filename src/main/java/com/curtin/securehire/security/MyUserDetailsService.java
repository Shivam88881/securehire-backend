package com.curtin.securehire.security;

import com.curtin.securehire.repository.db.CandidateRepository;
import com.curtin.securehire.repository.db.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return candidateRepository.findByEmail(email)
                .map(user -> new MyUserDetails(user.getEmail(), user.getPassword(), user.getRole().getName().getValue()))
                .or(() -> recruiterRepository.findByEmail(email)
                        .map(recruiter -> new MyUserDetails(recruiter.getEmail(), recruiter.getPassword(), recruiter.getRole().getName().getValue()))
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

