package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto;

import java.util.List;

public class MultipleChoiceCorrectAnswerDto extends CorrectAnswerDetailsDto {
    private List<OptionDto> correctOptions;
    private Integer correctOptionId;

    public MultipleChoiceCorrectAnswerDto(MultipleChoiceQuestion question) {
        this.correctOptionId = question.getCorrectOptionId();
        this.correctOptions = question.getCorrectOptions();
    }

    public Integer getCorrectOptionId() {
        return correctOptionId;
    }

    public List<OptionDto> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptionId(Integer correctOptionId) {
        this.correctOptionId = correctOptionId;
    }

    public void setCorrectOption(OptionDto option){
        this.correctOptions.add(option);
    }


    @Override
    public String toString() {
        return "MultipleChoiceCorrectAnswerDto{" +
                "correctOptions=" + correctOptions +
                '}';
    }
}