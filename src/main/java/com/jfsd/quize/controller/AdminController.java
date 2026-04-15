package com.jfsd.quize.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jfsd.quize.entity.Test;
import com.jfsd.quize.repository.TestRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired private TestRepository testRepository;

    @GetMapping("/fix-exam-codes")
    @Transactional
    public ResponseEntity<String> fixExamCodes() {
        List<Test> all = testRepository.findAll();
        for (Test t : all) {
            if (t.getExamCode() != null) {
                t.setExamCode(t.getExamCode().toUpperCase().trim());
            }
        }
        testRepository.saveAll(all);
        return ResponseEntity.ok("Fixed " + all.size() + " tests");
    }

    @GetMapping("/list-tests")
    public ResponseEntity<?> listTests() {
        return ResponseEntity.ok(testRepository.findAll()
            .stream()
            .map(t -> t.getId() + " | " + t.getExamCode() + " | " + t.getTitle())
            .collect(java.util.stream.Collectors.toList()));
    }
}