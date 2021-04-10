package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_SEQUENCE_FOR_OPTION;

@Entity
@Table(name = "items")
public class Item implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer sequence;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Boolean group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_details_id")
    private ItemCombinationQuestion questionDetails;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private List<Link> link = new ArrayList<>();

    public Item() {
    }

    public Item(ItemDto item) {
        setSequence(item.getSequence());
        setContent(item.getContent());
        setGroup(item.getGroup());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }

    public Integer getSequence() {
        return sequence;
    }

    public Boolean getGroup(){
        return group;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setGroup(Boolean group){
        this.group = group;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public void setSequence(Integer sequence) {
        if (sequence == null || sequence < 0)
            throw new TutorException(INVALID_SEQUENCE_FOR_OPTION);

        this.sequence = sequence;
    }

    public ItemCombinationQuestion getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(ItemCombinationQuestion question) {
        this.questionDetails = question;
        question.addItem(this);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", question=" + questionDetails.getId() +
                ", group=" + group +
                '}';
    }

    public void remove() {
        this.questionDetails = null;
    }
}