package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.LinkDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Entity
@DiscriminatorValue(Question.QuestionTypes.ITEM_COMBINATION_QUESTION)
public class ItemCombinationQuestion extends QuestionDetails {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Item> itemsLeft = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Item> itemsRight = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Link> links = new ArrayList<>();

    public ItemCombinationQuestion() {
        super();
    }

    public ItemCombinationQuestion(Question question, ItemCombinationQuestionDto questionDto) {
        super(question);
        update(questionDto);
    }

    public List<Item> getItemsL() {
        return itemsLeft;
    }
    public List<Item> getItemsR() { return itemsRight; }
    public List<Link> getLinks() {
        return links;
    }

    public void setItemsLeft(List<ItemDto> items) {
        int index = 0;
        for (ItemDto itemDto : items) {
            if (itemDto.getId() == null) {
                itemDto.setSequence(index++);
                Item item1 = new Item(itemDto);
                item1.setQuestionDetails(this);
                itemsLeft.add(item1);
            } else {
                Item item = getItemsL()
                        .stream()
                        .filter(op -> op.getId().equals(itemDto.getId()))
                        .findAny()
                        .orElseThrow(() -> new TutorException(OPTION_NOT_FOUND, itemDto.getId()));

                item.setContent(itemDto.getContent());
            }
        }
    }

    public void setItemsRight(List<ItemDto> items) {
        int index = 0;
        for (ItemDto itemDto : items) {
            if (itemDto.getId() == null) {
                itemDto.setSequence(index++);
                Item item1 = new Item(itemDto);
                item1.setQuestionDetails(this);
                itemsRight.add(item1);
            } else {
                Item item = getItemsL()
                        .stream()
                        .filter(op -> op.getId().equals(itemDto.getId()))
                        .findAny()
                        .orElseThrow(() -> new TutorException(OPTION_NOT_FOUND, itemDto.getId()));

                item.setContent(itemDto.getContent());
            }
        }
    }


    public void setLinkS(List<LinkDto> links_arg) {
        for (LinkDto linkDto : links_arg) {
            if (linkDto.getId() == null) {
                Link link1 = new Link(linkDto);
                link1.setQuestionDetails(this);
                links.add(link1);
            } else {
                Link link = getLinks()
                        .stream()
                        .filter(op -> op.getId().equals(linkDto.getId()))
                        .findAny()
                        .orElseThrow(() -> new TutorException(OPTION_NOT_FOUND, linkDto.getId()));
            }
        }
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public void addItem(Item item) {
        if (item.getGroup()) {
            itemsLeft.add(item);
        }
        if (!item.getGroup()) {
            itemsRight.add(item);
        }
    }

    public void update(ItemCombinationQuestionDto questionDetails) {
        setItemsLeft(questionDetails.getItemsL());
        setItemsRight(questionDetails.getItemsR());
        setLinkS(questionDetails.getLinks());
    }

    @Override
    public void update(Updator updator) {
        updator.update(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitQuestionDetails(this);
    }

    public void visitItem(Visitor visitor) {

        for (Item item : this.getItemsL()) {
            item.accept(visitor);
        }
        for (Item item : this.getItemsR()) {
            item.accept(visitor);
        }
    }

    public void visitLink(Visitor visitor) {
        for (Link link : this.getLinks()) {
            link.accept(visitor);
        }
    }
    @Override
    public CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {/*
        return new MultipleChoiceCorrectAnswerDto(this);*/
        return null;
    }

    @Override
    public StatementQuestionDetailsDto getStatementQuestionDetailsDto() {/*
        return new MultipleChoiceStatementQuestionDetailsDto(this);*/
        return null;
    }

    @Override
    public StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {/*
        return new MultipleChoiceStatementAnswerDetailsDto();*/
        return null;
    }

    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {/*
        return new MultipleChoiceAnswerDto();*/
        return null;
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return new ItemCombinationQuestionDto(this);
    }


    public Integer getCorrectAnswer() {/*
        return this.getOptions()
                .stream()
                .filter(Option::isCorrect)
                .findAny().orElseThrow(() -> new TutorException(NO_CORRECT_OPTION))
                .getSequence();*/
        return null;
    }

    @Override
    public void delete() {
        super.delete();

        for (Item item : this.itemsLeft) {
            item.remove();
        }
        this.itemsLeft.clear();
        for (Link link : this.links) {
            link.remove();
        }
        this.links.clear();
    }

    @Override
    public String toString() {
        return "ItemCombinationQuestionDto{" +
                "itemsR=" + itemsRight + "itemsL=" + itemsLeft + " links=" + links +
                '}';
    }

    @Override
    public String getCorrectAnswerRepresentation() {
        /*return convertSequenceToLetter(this.getCorrectAnswer());*/
        return null;
    }

    public static String convertSequenceToLetter(Integer correctAnswer) {/*
        return correctAnswer != null ? Character.toString('A' + correctAnswer) : "-";*/
        return null;
    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        /*var result = this.options
                .stream()
                .filter(x -> selectedIds.contains(x.getId()))
                .map(x -> convertSequenceToLetter(x.getSequence()))
                .collect(Collectors.joining("|"));
        return !result.isEmpty() ? result : "-";*/
        return null;
    }
}