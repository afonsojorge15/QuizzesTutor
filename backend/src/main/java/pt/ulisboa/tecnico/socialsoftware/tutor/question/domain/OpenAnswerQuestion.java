package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenAnswerQuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;

import javax.persistence.*;
import java.util.List;


@Entity
@DiscriminatorValue(Question.QuestionTypes.OPEN_ANSWER_QUESTION)
public class OpenAnswerQuestion extends QuestionDetails {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.EAGER, orphanRemoval = true)
    private final String answer = "";
    private final int grade = 0;


    public OpenAnswerQuestion(Question question, OpenAnswerQuestionDto openAnswerQuestionDto) {
        super(question);
    }

    public String getAnswer(){
        return this.answer;
    }


    public int getGrade(){
        return this.grade;
    }


    public void update(OpenAnswerQuestionDto questionDetails) {

    }

    @Override
    public CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {
        return null;
    }

    @Override
    public StatementQuestionDetailsDto getStatementQuestionDetailsDto() {
        return null;
    }

    @Override
    public StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {
        return null;
    }

    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return null;
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return null;
    }

    @Override
    public void update(Updator updator) {

    }

    /*
        @Override
        public void update(Updator updator) {
            updator.update(this);
        }
    */
    @Override
    public String getCorrectAnswerRepresentation() {
        return null;
    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {

    }

/*
    @Override
    public void accept(Visitor visitor) {
        visitor.visitQuestionDetails(this);
    }


    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return new OpenAnswerQuestionAnswerDto();
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return new OpenAnswerQuestionDto(this);
    }
*/
}