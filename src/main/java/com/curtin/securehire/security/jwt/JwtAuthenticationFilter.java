package com.curtin.securehire.security.jwt;


import com.curtin.securehire.constant.RoleName;
import com.curtin.securehire.entity.db.Candidate;
import com.curtin.securehire.entity.db.Recruiter;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.db.CandidateRepository;
import com.curtin.securehire.repository.db.RecruiterRepository;
import com.curtin.securehire.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public Candidate userWithRefreshToken(String refreshToken) {
        Optional<Candidate> user = candidateRepository.findByRefreshToken(refreshToken);
        return user.orElse(null);
    }

    public Recruiter recruiterWithRefreshToken(String refreshToken) {
        Optional<Recruiter> recruiter = recruiterRepository.findByRefreshToken(refreshToken);
        return recruiter.orElse(null);
    }

    public Candidate findUserByRefreshToken(String refreshToken) {
        Optional<Candidate> user = candidateRepository.findByRefreshToken(refreshToken);
        return user.orElse(null);
    }

    public Candidate updateUserByRefreshToken(String refreshToken, Candidate candidate) {
        candidate.setRefreshToken(refreshToken);
        candidateRepository.save(candidate);
        return candidate;
    }

    public Recruiter findRequiterByRefreshToken(String refreshToken) {
        Optional<Recruiter> recruiterOpt = recruiterRepository.findByRefreshToken(refreshToken);
        return recruiterOpt.orElse(null);
    }

    public void updateRefreshToken(Integer recruiterId, String refreshToken) {

        Optional<Recruiter> recruiterOpt = recruiterRepository.findById(recruiterId);
        if (recruiterOpt.isEmpty()) {
            throw new NotFoundException("Recruiter not found with ID: " + recruiterId);
        }

        try {
            Recruiter recruiter = recruiterOpt.get();
            recruiter.setRefreshToken(refreshToken);
            recruiterRepository.save(recruiter);
        } catch (Exception e) {
            throw new BadRequestException("Failed to update refresh token: " + e.getMessage());
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = null;
        String refreshJwtToken = null;
        String username = null;
        String role = null;

        // Get the cookies from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwt = cookie.getValue();
                    try {
                        username = jwtUtil.extractUsername(jwt);
                        role = jwtUtil.extractRole(jwt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            if (jwt == null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refreshJwtToken")) {
                        refreshJwtToken = cookie.getValue();
                        try {
                            username = jwtUtil.extractUsernameFromRefreshToken(refreshJwtToken);
                            role = jwtUtil.extractRoleFromRefreshToken(refreshJwtToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }

        System.out.println("Username is: .........................................." + username);
        System.out.println("Role is: ..............................................." + role);

        System.out.println("Jwt is: ..............................................." + jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            if (jwt == null && jwtUtil.validateRefreshToken(refreshJwtToken, userDetails) ) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                Candidate existingCandidateWithRefreshToken = null;
                Recruiter existingRecruiterWithRefreshToken = null;

                final String newAuthToken = jwtUtil.generateToken(userDetails);
                final String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

                System.out.println("Role is: ..............................................." + role);
                System.out.println("Checking role.................................................");
                if(RoleName.CANDIDATE.getValue().equals(role)) {
                    System.out.println("finding candidate with refresh token.....................................");
                    existingCandidateWithRefreshToken = findUserByRefreshToken(refreshJwtToken);
                    System.out.println("updating candidate with refresh token.....................................");
                    updateUserByRefreshToken(newRefreshToken, existingCandidateWithRefreshToken);
                    System.out.println("updated candidate with refresh token.....................................");

                } else if(RoleName.RECRUITER.getValue().equals(role)) {
                    existingRecruiterWithRefreshToken = findRequiterByRefreshToken(refreshJwtToken);
                    updateRefreshToken(existingRecruiterWithRefreshToken.getId(), newRefreshToken);
                } else {
                    throw new RuntimeException("Invalid role");
                }



                // Create a new cookie for the JWT
                Cookie jwtCookie = new Cookie("jwt", newAuthToken);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setMaxAge(900);
                jwtCookie.setPath("/");
                jwtCookie.setSecure(true);

                // Create a new cookie for the refresh token
                Cookie refreshTokenCookie = new Cookie("refreshJwtToken", newRefreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setMaxAge(86400);
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setSecure(true);

                // Add the cookies to the response
                response.addCookie(jwtCookie);
                response.addCookie(refreshTokenCookie);
            }

            if (jwt != null && jwtUtil.validateToken(jwt, userDetails)) {
                System.out.println("jwt Token is valid.................................................");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        else {
            System.out.println("No user found...........or "+SecurityContextHolder.getContext().getAuthentication());
        }
        System.out.println("filterChain.doFilter...................................................");
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // Or use logger
            throw e;
        }

    }
}
