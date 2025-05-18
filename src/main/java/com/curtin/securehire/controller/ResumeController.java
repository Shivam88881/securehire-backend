package com.curtin.securehire.controller;

import com.curtin.securehire.entity.db.Resume;
import com.curtin.securehire.service.db.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/{resumeId}")
    public ResponseEntity<Resume> findById(@PathVariable Integer resumeId) {
        log.info("Entering findById method with resumeId: {}", resumeId);
        Resume resume = resumeService.findById(resumeId);
        log.info("Exiting findById method. Retrieved resume: {}", resume);
        return ResponseEntity.ok(resume);
    }

    @GetMapping
    public ResponseEntity<List<Resume>> findAll() {
        log.info("Entering findAll method.");
        List<Resume> resumes = resumeService.findAll();
        log.info("Exiting findAll method. Retrieved {} resumes.", resumes.size());
        return ResponseEntity.ok(resumes);
    }

    @PostMapping
    public ResponseEntity<Resume> save(@RequestBody Resume resume) {
        log.info("Entering save method with resume details: {}", resume);
        Resume savedResume = resumeService.save(resume);
        log.info("Exiting save method. Saved resume: {}", savedResume);
        return ResponseEntity.ok(savedResume);
    }

    @PutMapping("/{resumeId}")
    public ResponseEntity<Resume> update(@PathVariable Integer resumeId, @RequestBody Resume resume) {
        log.info("Entering update method with resumeId: {} and updated details: {}", resumeId, resume);
        Resume updatedResume = resumeService.update(resumeId, resume);
        log.info("Exiting update method. Updated resume: {}", updatedResume);
        return ResponseEntity.ok(updatedResume);
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer resumeId) {
        log.info("Entering delete method with resumeId: {}", resumeId);
        resumeService.delete(resumeId);
        log.info("Exiting delete method. Resume deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Resume>> findByUserId(@PathVariable Integer userId) {
        log.info("Entering findByUserId method with userId: {}", userId);
        List<Resume> resumes = resumeService.findByUserId(userId);
        log.info("Exiting findByUserId method. Retrieved {} resumes.", resumes.size());
        return ResponseEntity.ok(resumes);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Resume> addResumeForUser(@PathVariable Integer userId, @RequestBody Resume resume) {
        log.info("Entering addResumeForUser method with userId: {} and resume details: {}", userId, resume);
        Resume addedResume = resumeService.addResumeForUser(userId, resume);
        log.info("Exiting addResumeForUser method. Added resume: {}", addedResume);
        return ResponseEntity.ok(addedResume);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Resume>> findByNameContaining(@RequestParam String name) {
        log.info("Entering findByNameContaining method with name fragment: {}", name);
        List<Resume> resumes = resumeService.findByNameContaining(name);
        log.info("Exiting findByNameContaining method. Retrieved {} resumes.", resumes.size());
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/uploaded-after/{date}")
    public ResponseEntity<List<Resume>> findByUploadedDateAfter(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Entering findByUploadedDateAfter method with date: {}", date);
        List<Resume> resumes = resumeService.findByUploadedDateAfter(date);
        log.info("Exiting findByUploadedDateAfter method. Retrieved {} resumes.", resumes.size());
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> countByUserId(@PathVariable Integer userId) {
        log.info("Entering countByUserId method with userId: {}", userId);
        Long count = resumeService.countByUserId(userId);
        log.info("Exiting countByUserId method. Total resumes found: {}", count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Resume>> findByFilters(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate uploadedDate) {

        log.info("Entering findByFilters method with parameters - UserId: {}, Name: {}, StartDate: {}",
                userId, name, uploadedDate);

        List<Resume> resumes = resumeService.findByFilters(userId, name, uploadedDate);

        log.info("Exiting findByFilters method. Retrieved {} resumes.", resumes.size());
        return ResponseEntity.ok(resumes);
    }
}
