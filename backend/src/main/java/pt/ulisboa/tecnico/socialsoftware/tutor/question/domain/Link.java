package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.LinkDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "links")
public class Link implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_details_id")
    private ItemCombinationQuestion questionDetails;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public Link() {
    }

    public Link(LinkDto link) {
        setLink(link.getLink().get(0),link.getLink().get(1));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitLink(this);
    }

    public Integer getId() {
        return id;
    }

    public List<ItemDto> getLink() {
        List<ItemDto> aux = new ArrayList<>();
        for (Item item: items){
            aux.add(new ItemDto(item));
        }
        return aux;
    }

    public void setLink(ItemDto item1, ItemDto item2) {
        items.add(new Item(item1));
        items.add(new Item(item2));
    }

    public void removeLink(){
        items.clear();
    }

    public ItemCombinationQuestion getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(ItemCombinationQuestion question) {
        this.questionDetails = question;
        question.addLink(this);
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", link=" + items +
                '}';
    }

    public void remove() {
        this.questionDetails = null;
    }
}