package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CodeFillInQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CodeOrderQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Item
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*
import spock.lang.Unroll

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage

@DataJpaTest
class CreateItemCombinationQuestionTest extends SpockTest {
    def "create an item combo question with two items connected to each other"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())
        and: 'two itemIDs'
        def itemDto1 = new ItemDto()
        itemDto1.setContent(OPTION_1_CONTENT)
        itemDto1.setGroup(true)
        def itemDto2 = new ItemDto()
        itemDto2.setContent(OPTION_2_CONTENT)
        itemDto2.setGroup(false)
        def items = new ArrayList<ItemDto>()
        items.add(itemDto1)
        items.add(itemDto2)
        def linkDto = new LinkDto()
        linkDto.setLink(itemDto1, itemDto2)
        def links = new ArrayList<LinkDto>()
        links.add(linkDto)
        questionDto.getQuestionDetailsDto().setItemsLeft(items)
        questionDto.getQuestionDetailsDto().setItemsRight(items)
        questionDto.getQuestionDetailsDto().setLinkS(links)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the link exists between both items"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getQuestionDetails().getOptions().size() == 1
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
        result.getQuestionDetails().getItemsL().size() == 1
        result.getQuestionDetails().getItemsR().size() == 1
        result.getQuestionDetails().getLinks().size() == 1
        result.getQuestionDetails().getLinks().get(0) == links
    }

    def "create an item combo question with two items not connected to each other"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())
        and: 'two itemIDs'
        def itemDto1 = new ItemDto()
        itemDto1.setContent(ITEM_1_CONTENT)
        itemDto1.setGroup(true)
        def itemDto2 = new ItemDto()
        itemDto2.setContent(ITEM_2_CONTENT)
        itemDto2.setGroup(false)
        def items = new ArrayList<ItemDto>()
        items.add(itemDto1)
        items.add(itemDto2)
        def links = new ArrayList<LinkDto>()
        questionDto.getQuestionDetailsDto().setItemsLeft(items)
        questionDto.getQuestionDetailsDto().setItemsRight(items)
        questionDto.getQuestionDetailsDto().setLinkS(links)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the link does not exist"
        def result = questionRepository.findAll().get(0)
        result.getQuestionDetails().getItemsL().size() == 1
        result.getQuestionDetails().getItemsR().size() == 1
        result.getQuestionDetails().getLinks().size() == 0
    }

    def "create an item combo question with three and one item connecting to other 2 items"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())
        and: 'three itemIDs'
        def itemDto1 = new ItemDto()
        itemDto1.setContent(ITEM_1_CONTENT)
        itemDto1.setGroup(true)
        def itemDto2 = new ItemDto()
        itemDto2.setContent(ITEM_2_CONTENT)
        itemDto2.setGroup(false)
        def itemDto3 = new ItemDto()
        itemDto3.setContent(ITEM_2_CONTENT)
        itemDto3.setGroup(false)
        def items = new ArrayList<ItemDto>()
        items.add(itemDto1)
        items.add(itemDto2)
        items.add(itemDto3)
        def linkDto = new LinkDto()
        def linkDto2 = new LinkDto()
        linkDto.setLink(itemDto1, itemDto2)
        linkDto2.setLink(itemDto1, itemDto3)
        def links = new ArrayList<LinkDto>()
        links.add(linkDto)
        links.add(linkDto2)
        questionDto.getQuestionDetailsDto().setItemsLeft(items)
        questionDto.getQuestionDetailsDto().setItemsRight(items)
        questionDto.getQuestionDetailsDto().setLinkS(links)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "both links exist"
        def result = questionRepository.findAll().get(0)
        result.getQuestionDetails().getItemsL().size() == 1
        result.getQuestionDetails().getItemsR().size() == 2
        result.getQuestionDetails().getLinks().size() == 2
        result.getQuestionDetails().getLinks().get(0) == links
    }

    def "create an item combo question with two items connected to each other but from the same group"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())
        and: 'two itemIDs from the same group'
        def itemDto1 = new ItemDto()
        itemDto1.setContent(ITEM_1_CONTENT)
        itemDto1.setGroup(true)
        def itemDto2 = new ItemDto()
        itemDto2.setContent(ITEM_2_CONTENT)
        itemDto2.setGroup(true)
        def items = new ArrayList<ItemDto>()
        items.add(itemDto1)
        items.add(itemDto2)
        def links = new ArrayList<LinkDto>()
        questionDto.getQuestionDetailsDto().setItemsLeft(items)
        questionDto.getQuestionDetailsDto().setItemsRight(items)
        questionDto.getQuestionDetailsDto().setLinkS(links)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.LINK_BETWEEN_SAME_GROUP_ITEMS
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
