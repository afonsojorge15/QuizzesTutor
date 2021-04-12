package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Item;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Link;
import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

public class LinkDto implements Serializable {
    private Integer id;
    private List <ItemDto> items = new ArrayList<>();

    public LinkDto() {
    }

    public LinkDto(Link link) {
        this.id = link.getId();
        this.items = link.getLink();
    }

    public Integer getId() {
        return this.id;
    }

    public List<ItemDto> getLink() {
        return this.items;
    }

    public void setLink(ItemDto item1, ItemDto item2) {
        this.items.add(item1);
        this.items.add(item2);
    }

    public void removeLink(){
        items.clear();
    }

    @Override
    public String toString() {
        return "LinkDto{" +
                "id=" + this.id +
                ", link='" + this.items + '\'' +
                '}';
    }
}
