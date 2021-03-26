package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCombinationQuestionDto extends QuestionDetailsDto {
    private List<LinkDto> links = new ArrayList<>();
    private List<ItemDto> itemsLeft = new ArrayList<>();
    private List<ItemDto> itemsRight = new ArrayList<>();

    public ItemCombinationQuestionDto() {
    }

    public ItemCombinationQuestionDto(ItemCombinationQuestion question) {
        this.links = question.getLinks().stream().map(LinkDto::new).collect(Collectors.toList());
        this.itemsLeft = question.getItemsL().stream().map(ItemDto::new).collect(Collectors.toList());
        this.itemsRight = question.getItemsR().stream().map(ItemDto::new).collect(Collectors.toList());
    }

    public List<ItemDto> getItemsL() { return itemsLeft; }
    public List<ItemDto> getItemsR() { return itemsRight; }
    public List<LinkDto> getLinks() { return links; }

    public void setItemsLeft(List<ItemDto> itemsLeft) {
        this.itemsLeft = itemsLeft;
    }
    public void setItemsRight(List<ItemDto> itemsRight) {
        this.itemsRight = itemsRight;
    }
    public void setLinkS(List<LinkDto> links) {
        this.links = links;
    }

    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new ItemCombinationQuestion(question, this);
    }

    @Override
    public void update(ItemCombinationQuestion question) {
        question.update(this);
    }

    @Override
    public String toString() {
        return "ItemCombinationQuestionDto{" +
                "itemsR=" + itemsRight + "itemsL=" + itemsLeft + " links=" + links +
                '}';
    }

}
