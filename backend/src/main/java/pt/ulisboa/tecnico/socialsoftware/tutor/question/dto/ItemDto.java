package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Item;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_SEQUENCE_FOR_OPTION;

public class ItemDto implements Serializable {
    private Integer sequence;
    private Integer id;
    private Boolean group;
    private String content;
    private List<Link> link = new ArrayList<>();

    public ItemDto() {
    }

    public ItemDto(Item item) {
        this.id = item.getId();
        this.group = item.getGroup();
        this.content = item.getContent();
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
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "OptionDto{" +
                "id=" + id +
                ", group=" + group +
                ", content='" + content + '\'' +
                '}';
    }
}