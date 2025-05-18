package com.curtin.securehire.service.db;

import com.curtin.securehire.entity.db.Resume;

import java.time.LocalDate;
import java.util.List;

public interface ResumeService {
    // Basic CRUD operations
    Resume findById(Integer resumeId);
    List<Resume> findAll();
    Resume save(Resume resume);
    Resume update(Integer resumeId, Resume resume);
    void delete(Integer resumeId);

    List<Resume> findByUserId(Integer userId);
    Resume addResumeForUser(Integer userId, Resume resume);
    List<Resume> findByNameContaining(String name);
    List<Resume> findByUploadedDateAfter(LocalDate date);
    Long countByUserId(Integer userId);

    // Advanced queries
    List<Resume> findByFilters(Integer userId, String name, LocalDate startDate);
}
