package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenAnswerQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

import javax.persistence.Column;

public class OpenAnswerQuestionDto extends QuestionDetailsDto {


    private  String _answer = "";

    public OpenAnswerQuestionDto(OpenAnswerQuestion openAnswerQuestion) {
        this._answer = openAnswerQuestion.getAnswer();
    }

    public OpenAnswerQuestionDto() {
    }


    public void setAnswer(String answer){
        this._answer = answer;
    }



    public String getAnswer(){
        return this._answer;
    }





    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new OpenAnswerQuestion(question, this);
    }

    @Override
    public void update(OpenAnswerQuestion question) {
        question.update(this);
    }

    @Override
    public String toString() {
        return "OpenAnswerQuestionDto{" +
                "answer=" + _answer +
                '}';
    }

}