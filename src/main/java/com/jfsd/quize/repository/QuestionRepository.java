package com.jfsd.quize.repository;

import com.jfsd.quize.entity.Question;
import com.jfsd.quize.entity.Question.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, QuestionId> {
    List<Question> findByTestIdOrderByQuestionNumberAsc(Long testId);
    long countByTestId(Long testId);

    @Query("SELECT COALESCE(MAX(q.questionNumber), 0) FROM Question q WHERE q.testId = :testId")
    Integer findMaxQuestionNumberByTestId(@Param("testId") Long testId);
    List<Question> findByTestIdAndQuestionNumberGreaterThanOrderByQuestionNumberAsc(
    Long testId, Integer questionNumber
);
void deleteAllByTestId(Long testId);
}
