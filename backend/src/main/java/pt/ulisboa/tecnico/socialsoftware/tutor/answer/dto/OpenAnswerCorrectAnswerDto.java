package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenAnswerQuestion;

public class OpenAnswerCorrectAnswerDto extends CorrectAnswerDetailsDto {
    private String correctanswer="";


    public OpenAnswerCorrectAnswerDto(OpenAnswerQuestion question) {
        this.correctanswer = question.getAnswer();
    }

    public String getCorrectAnswer() {
        return correctanswer;
    }

    public void setCorrectAnswer(String correctanswer) {
        this.correctanswer = correctanswer;
    }

    @Override
    public String toString() {
        return "OpenAnswerCorrectAnswerDto{" +
                "correctanswer=" + correctanswer +
                '}';
    }
}
