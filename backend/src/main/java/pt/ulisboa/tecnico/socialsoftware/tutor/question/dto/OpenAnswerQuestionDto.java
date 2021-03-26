package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenAnswerQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

public class OpenAnswerQuestionDto extends QuestionDetailsDto {
    private String answer = "";
    private int grade = 0;

    public OpenAnswerQuestionDto() {
    }

    public OpenAnswerQuestionDto(OpenAnswerQuestion question) {
        this.answer = question.getAnswer();
        this.grade = question.getGrade();
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public void setGrade(int grade){
        this.grade = grade;
    }

    public String getAnswer(){
        return this.answer;
    }


    public int getGrade(){
        return this.grade;
    }


    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new OpenAnswerQuestion(question, this);
    }

    public void update(OpenAnswerQuestion question) {
        question.update(this);
    }

    @Override
    public String toString() {
        return "OpenAnswerQuestionDto{" +
                "answer=" + answer +
                "grade=" + grade +
                '}';
    }



}