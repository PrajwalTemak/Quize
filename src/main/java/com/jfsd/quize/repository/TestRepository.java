package com.jfsd.quize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jfsd.quize.entity.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findByExamCode(String examCode);

    // for views
    List<Test> findByIsPublished(Boolean isPublished);
    List<Test> findByIsPublishedAndStartTimeAfter(Boolean isPublished, LocalDateTime time);
    List<Test> findByIsPublishedAndStartTimeBeforeAndEndTimeAfter(Boolean isPublished, LocalDateTime start, LocalDateTime end);
    List<Test> findByEndTimeBefore(LocalDateTime time);
}
