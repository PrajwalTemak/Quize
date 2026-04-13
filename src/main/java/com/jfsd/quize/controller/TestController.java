package com.jfsd.quize.controller;
import com.jfsd.quize.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jfsd.quize.dto.AddTestRequest;
import com.jfsd.quize.entity.Test;
import com.jfsd.quize.repository.TestRepository;

import java.math.BigDecimal;
import java.util.Optional;
@RestController
@RequestMapping("/tests")
@CrossOrigin
public class TestController {

    @Autowired
    private TestRepository testRepository;

    // ─── ADD ────────────────────────────────────────────────────────────────
    @PostMapping("/add")
    public ResponseEntity<String> addTest(@RequestBody AddTestRequest request) {
        Test test = new Test();
        test.setTitle(request.getTitle());
        test.setDescription(request.getDescription());
        test.setExamCode(request.getExamCode());
        test.setCreatedBy(request.getCreatedBy());
        test.setTotalMarks(request.getTotalMarks());
        test.setDurationMinutes(request.getDurationMinutes());
        test.setStartTime(request.getStartTime());
        test.setEndTime(request.getEndTime());
        test.setNegativeMarking(
            request.getNegativeMarking() != null ? request.getNegativeMarking() : BigDecimal.ZERO
        );
        test.setIsPublished(
            request.getIsPublished() != null ? request.getIsPublished() : false
        );
        test.setShowResults(
            request.getShowResults() != null ? request.getShowResults() : false
        );

        testRepository.save(test);
        return ResponseEntity.ok("Test added successfully with ID: " + test.getId());
    }

    // ─── UPDATE (by testId) ──────────────────────────────────────────────────
    @PutMapping("/update/{testId}")
    public ResponseEntity<String> updateTest(@PathVariable Long testId,@RequestBody AddTestRequest request) {
        Optional<Test> optional = testRepository.findById(testId);
        if (!optional.isPresent())
            return ResponseEntity.badRequest().body("Test not found with ID: " + testId);

        Test test = optional.get();

        if (request.getTitle() != null)           test.setTitle(request.getTitle());
        if (request.getDescription() != null)     test.setDescription(request.getDescription());
        if (request.getExamCode() != null)        test.setExamCode(request.getExamCode());
        if (request.getCreatedBy() != null)       test.setCreatedBy(request.getCreatedBy());
        if (request.getTotalMarks() != null)      test.setTotalMarks(request.getTotalMarks());
        if (request.getDurationMinutes() != null) test.setDurationMinutes(request.getDurationMinutes());
        if (request.getStartTime() != null)       test.setStartTime(request.getStartTime());
        if (request.getEndTime() != null)         test.setEndTime(request.getEndTime());
        if (request.getNegativeMarking() != null) test.setNegativeMarking(request.getNegativeMarking());
        if (request.getIsPublished() != null)     test.setIsPublished(request.getIsPublished());
        if (request.getShowResults() != null)     test.setShowResults(request.getShowResults());

        testRepository.save(test);
        return ResponseEntity.ok("Test updated successfully with ID: " + testId);
    }

    // ─── DELETE (by testId) ──────────────────────────────────────────────────
    @DeleteMapping("/delete/{testId}")
@Transactional
public ResponseEntity<String> deleteTest(@PathVariable Long testId) {
    if (!testRepository.existsById(testId))
        return ResponseEntity.badRequest().body("Test not found with ID: " + testId);

    // Delete questions first to avoid foreign key error
    questionRepository.deleteAllByTestId(testId);

    testRepository.deleteById(testId);
    return ResponseEntity.ok("Test deleted successfully with ID: " + testId);
}
    // ─── GET BY EXAM CODE ────────────────────────────────────────────────────
    @GetMapping("/find/{examCode}")
    public ResponseEntity<?> getTest(@PathVariable String examCode) {
        Optional<Test> optional = testRepository.findByExamCode(examCode);
        if (!optional.isPresent())
            return ResponseEntity.badRequest().body("Test not found with examCode: " + examCode);

        return ResponseEntity.ok(optional.get());
    }

    // ─── GET ALL TESTS ───────────────────────────────────────────────────────
    @GetMapping("/all")
    public ResponseEntity<?> getAllTests() {
        return ResponseEntity.ok(testRepository.findAll());
    }

@Autowired
private QuestionRepository questionRepository;


}
