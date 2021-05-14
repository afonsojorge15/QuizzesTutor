package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenAnswerQuestion;

import java.util.List;
import java.util.stream.Collectors;

public class OpenAnswerStatementQuestionDetailsDto extends StatementQuestionDetailsDto {
    private String answer="";

    public OpenAnswerStatementQuestionDetailsDto(OpenAnswerQuestion question) {
        this.answer = question.getAnswer();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "OpenAnswerStatementQuestionDetailsDto{" +
                "answer=" + answer +
                '}';
    }
}