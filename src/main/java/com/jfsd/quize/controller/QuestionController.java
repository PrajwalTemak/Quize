package com.jfsd.quize.controller;

import com.jfsd.quize.entity.Question;
import com.jfsd.quize.entity.Question.QuestionId;
import com.jfsd.quize.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jfsd.quize.dto.QuestionRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/ping")
    public String ping() { return "Server is working!"; }

    // ─── ADD ────────────────────────────────────────────────────────────────
    @PostMapping("/add")
    @Transactional
    public ResponseEntity<String> addQuestion(@RequestBody QuestionRequest req) {
        try {
            Integer nextQuestionNumber = questionRepository.findMaxQuestionNumberByTestId(req.getTestId()) + 1;

            Question q = new Question();
            q.setTestId(req.getTestId());
            q.setQuestionNumber(nextQuestionNumber);
            q.setQuestionText(req.getQuestionText());
            q.setQuestionType(req.getQuestionType());
            q.setOption1(req.getOption1());
            q.setOption2(req.getOption2());
            q.setOption3(req.getOption3());
            q.setOption4(req.getOption4());
            q.setCorrectOption(req.getCorrectOption());
            q.setMarks(req.getMarks());

            questionRepository.save(q);
            return ResponseEntity.ok("Question added with number: " + nextQuestionNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // ─── UPDATE (by testId + questionNumber) ────────────────────────────────
    @PutMapping("/update/{testId}/{questionNumber}")
    @Transactional
    public ResponseEntity<String> updateQuestion(@PathVariable Long testId,
                                                  @PathVariable Integer questionNumber,
                                                  @RequestBody QuestionRequest req) {
        QuestionId qid = new QuestionId(testId, questionNumber);
        Optional<Question> optional = questionRepository.findById(qid);
        if (!optional.isPresent())
            return ResponseEntity.badRequest().body("Question not found for testId=" + testId + ", questionNumber=" + questionNumber);

        Question q = optional.get();
        q.setQuestionText(req.getQuestionText());
        q.setQuestionType(req.getQuestionType());
        q.setOption1(req.getOption1());
        q.setOption2(req.getOption2());
        q.setOption3(req.getOption3());
        q.setOption4(req.getOption4());
        q.setCorrectOption(req.getCorrectOption());
        q.setMarks(req.getMarks());

        questionRepository.save(q);
        return ResponseEntity.ok("Question " + questionNumber + " of test " + testId + " updated successfully");
    }

    // ─── DELETE + RE-NUMBER ─────────────────────────────────────────────────
    @DeleteMapping("/delete/{testId}/{questionNumber}")
    @Transactional
    public ResponseEntity<String> deleteQuestion(@PathVariable Long testId,
                                                  @PathVariable Integer questionNumber) {
        QuestionId qid = new QuestionId(testId, questionNumber);
        if (!questionRepository.existsById(qid))
            return ResponseEntity.badRequest().body("Question not found for testId=" + testId + ", questionNumber=" + questionNumber);

        // Step 1: Delete the target question
        questionRepository.deleteById(qid);

        // Step 2: Fetch all questions AFTER the deleted one (higher question numbers)
        List<Question> questionsAfter = questionRepository
                .findByTestIdAndQuestionNumberGreaterThanOrderByQuestionNumberAsc(testId, questionNumber);

        // Step 3: Delete those questions from DB (we'll re-insert with new numbers)
        questionRepository.deleteAll(questionsAfter);
        questionRepository.flush(); // ensure deletes are committed before re-insert

        // Step 4: Re-save each with questionNumber - 1
        for (Question q : questionsAfter) {
            q.setQuestionNumber(q.getQuestionNumber() - 1);
            questionRepository.save(q);
        }

        return ResponseEntity.ok("Question " + questionNumber + " deleted and remaining questions re-numbered");
    }

    // ─── GET ALL QUESTIONS FOR A TEST ───────────────────────────────────────
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<Question>> getQuestionsByTest(@PathVariable Long testId) {
        return ResponseEntity.ok(questionRepository.findByTestIdOrderByQuestionNumberAsc(testId));
    }
}
