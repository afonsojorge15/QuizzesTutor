package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenAnswerQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(Question.QuestionTypes.OPEN_ANSWER_QUESTION)
public class OpenAnswerAnswer extends AnswerDetails {

    @Column(columnDefinition = "TEXT")
    private String answer="";

    public OpenAnswerAnswer() {
        super();
    }

    public OpenAnswerAnswer(QuestionAnswer questionAnswer){
        super(questionAnswer);
    }

    public OpenAnswerAnswer(QuestionAnswer questionAnswer, String answer){
        super(questionAnswer);
        this.setAnswer(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswer(OpenAnswerQuestion question, OpenAnswerStatementAnswerDetailsDto OpenAnswerStatementAnswerDetailsDto) {
        if (OpenAnswerStatementAnswerDetailsDto.getAnswer() != null) {
            this.setAnswer(OpenAnswerStatementAnswerDetailsDto.getAnswer());
        } else {
            this.setAnswer(null);
        }
    }


    @Override
    public boolean isCorrect() {

        return getAnswer() != null && getAnswer().equals(getQuestionAnswer().getQuizQuestion().getQuestion().getQuestionDetails().getCorrectAnswerRepresentation());
    }


    public void remove() {
        answer =null;
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new OpenAnswerAnswerDto(this);
    }

    @Override
    public boolean isAnswered() {
        return this.getAnswer() != null;
    }

    @Override
    public String getAnswerRepresentation() {
        return this.getAnswer();
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new OpenAnswerStatementAnswerDetailsDto(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAnswerDetails(this);
    }
}
