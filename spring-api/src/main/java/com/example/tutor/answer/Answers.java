package com.example.tutor.answer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


public class Answers implements Serializable {

    private Integer student_id;
    private Integer quiz_id;
    private LocalDateTime answer_date;
    private LocalDateTime time_taken;
    private List<AnswerDTO> answers;

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public LocalDateTime getAnswer_date() {
        return answer_date;
    }

    public void setAnswer_date(LocalDateTime answer_date) {
        this.answer_date = answer_date;
    }

    public LocalDateTime getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(LocalDateTime time_taken) {
        this.time_taken = time_taken;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}