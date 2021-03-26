package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_CONTENT_FOR_OPTION;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_SEQUENCE_FOR_OPTION;

@Entity
@Table(name = "options")
public class Option implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer sequence;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean correct;

    @Column
    private Integer relevance = 0;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_details_id")
    private MultipleChoiceQuestion questionDetails;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "option", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<MultipleChoiceAnswer> questionAnswers = new HashSet<>();

    public Option() {
    }

    public Option(OptionDto option) {
        setSequence(option.getSequence());
        setContent(option.getContent());
        setCorrect(option.isCorrect());
        setRelevance(option.getRelevance());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOption(this);
    }

    public Integer getId() {
        return id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        if (sequence == null || sequence < 0)
            throw new TutorException(INVALID_SEQUENCE_FOR_OPTION);

        this.sequence = sequence;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        if (this.relevance < 1 && correct){
            this.relevance = 1;
        }
        else if (!correct){
            this.relevance = 0;
        }
        this.correct = correct;
    }


    public Integer getRelevance(){
        return relevance;
    }

    public void setRelevance(Integer relevance){
        if (relevance > 0){
            this.correct = true;
        }
        else if (relevance == 0){
            this.correct = false;
        }
        this.relevance = relevance;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null || content.isBlank())
            throw new TutorException(INVALID_CONTENT_FOR_OPTION);

        this.content = content;
    }

    public MultipleChoiceQuestion getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(MultipleChoiceQuestion question) {
        this.questionDetails = question;
        question.addOption(this);
    }

    public Set<MultipleChoiceAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void addQuestionAnswer(MultipleChoiceAnswer questionAnswer) {
        questionAnswers.add(questionAnswer);
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", correct=" + correct +
                ", content='" + content + '\'' +
                ", question=" + questionDetails.getId() +
                ", questionAnswers=" + questionAnswers +
                ", relevance='" + relevance +
                '}';
    }

    public void remove() {
        this.questionDetails = null;
        this.questionAnswers.clear();
    }
}
