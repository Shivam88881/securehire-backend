package com.curtin.securehire.controller;

import com.curtin.securehire.constant.RoleName;
import com.curtin.securehire.dto.LoginRequest;
import com.curtin.securehire.dto.LoginResponse;
import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.repository.db.CandidateRepository;
import com.curtin.securehire.repository.db.RecruiterRepository;
import com.curtin.securehire.security.jwt.JwtUtil;
import com.curtin.securehire.service.db.impl.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse res) throws Exception {
        LoginResponse response = loginService.login(request);

        // Create a new cookie
        Cookie jwtCookie = new Cookie("jwt", response.getJwtToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(900);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);

        // Create a new cookie
        Cookie refreshJwtCookie = new Cookie("refreshJwtToken", response.getRefreshToken());
        refreshJwtCookie.setHttpOnly(true);
        refreshJwtCookie.setMaxAge(86400);
        refreshJwtCookie.setPath("/");
        refreshJwtCookie.setSecure(true);

        // Add the cookie to the response
        res.addCookie(jwtCookie);
        res.addCookie(refreshJwtCookie);
        return ResponseEntity.ok("Login successful");

    }

    @GetMapping("/load")
    public ResponseEntity<?> loadUser(HttpServletRequest request) throws BadRequestException{
        String username=null;
        String jwt = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        String role = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    // Invalidate the cookie
                    jwt = cookie.getValue();
                    try {
                        username = jwtService.extractUsername(jwt);
                        role = jwtService.extractRole(jwt);
                    }catch(Exception e) {
                        throw new BadRequestException(e.getMessage());
                    }
                    break;
                }
            }

            if(jwt == null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refreshJwtToken")) {
                        // Invalidate the cookie
                        refreshToken = cookie.getValue();
                        try {
                            username = jwtService.extractUsername(refreshToken);
                            role = jwtService.extractRole(refreshToken);
                        }catch(Exception e) {
                            throw new BadRequestException(e.getMessage());
                        }
                        break;
                    }
                }
            }
        }else {
            throw new BadRequestException("Login to use all feature");
        }

        if(username != null) {
            if(RoleName.CANDIDATE.getValue().equals(role)) {
                Candidate candidate = candidateRepository.findByEmail(username).get();
                return ResponseEntity.ok(candidate);
            } else if (RoleName.RECRUITER.getValue().equals(role)) {
                Recruiter recruiter = recruiterRepository.findByEmail(username).get();
                return ResponseEntity.ok(recruiter);
            }
        }

        throw new BadRequestException("JWT token is invalid");
    }

}
