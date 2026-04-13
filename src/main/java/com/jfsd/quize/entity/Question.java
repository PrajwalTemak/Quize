package com.jfsd.quize.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "questions")
@IdClass(Question.QuestionId.class)
public class Question {

    @Id
    @Column(name = "test_id")
    private Long testId;

    @Id
    @Column(name = "question_number")
    private Integer questionNumber;  // now per-test numbering

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(columnDefinition = "TEXT")
    private String option1;
    @Column(columnDefinition = "TEXT")
    private String option2;
    @Column(columnDefinition = "TEXT")
    private String option3;
    @Column(columnDefinition = "TEXT")
    private String option4;

    @Column(name = "correct_option")
    private Integer correctOption;

    @Column(nullable = false)
    private int marks;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Composite Key Class
    public static class QuestionId implements Serializable {
        private Long testId;
        private Integer questionNumber;

        public QuestionId() {}
        public QuestionId(Long testId, Integer questionNumber) {
            this.testId = testId;
            this.questionNumber = questionNumber;
        }

        // equals() and hashCode() are required
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof QuestionId)) return false;
            QuestionId that = (QuestionId) o;
            return testId.equals(that.testId) && questionNumber.equals(that.questionNumber);
        }

        @Override
        public int hashCode() {
            return testId.hashCode() + questionNumber.hashCode();
        }
    }

    // Enum for question type
    public enum QuestionType { OBJECTIVE, SUBJECTIVE }

    // Getters & Setters
    public Long getTestId() { return testId; }
    public void setTestId(Long testId) { this.testId = testId; }

    public Integer getQuestionNumber() { return questionNumber; }
    public void setQuestionNumber(Integer questionNumber) { this.questionNumber = questionNumber; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }

    public String getOption1() { return option1; }
    public void setOption1(String option1) { this.option1 = option1; }

    public String getOption2() { return option2; }
    public void setOption2(String option2) { this.option2 = option2; }

    public String getOption3() { return option3; }
    public void setOption3(String option3) { this.option3 = option3; }

    public String getOption4() { return option4; }
    public void setOption4(String option4) { this.option4 = option4; }

    public Integer getCorrectOption() { return correctOption; }
    public void setCorrectOption(Integer correctOption) { this.correctOption = correctOption; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
