package pt.ulisboa.tecnico.socialsoftware.tutor.impexp.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Image
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Item
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Link
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.LinkDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User

@DataJpaTest
class ImportExportItemCombinationQuestionTest extends SpockTest {
    def question
    def questionId
    def item1
    def item2
    def item3
    def user
    def link

    def setup() {

        def questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_2_TITLE)
        questionDto.setContent(QUESTION_2_CONTENT)
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        def items = new ArrayList<ItemDto>()
        def itemDto1 = new ItemDto()
        itemDto1.setContent(ITEM_2_CONTENT)
        def itemDto2 = new ItemDto()
        itemDto2.setContent(ITEM_1_CONTENT)
        def itemDto3 = new ItemDto()
        itemDto3.setContent(ITEM_1_CONTENT)

        items.add(itemDto1)
        items.add(itemDto2)
        items.add(itemDto3)

        def links = new ArrayList<LinkDto>()
        def linkDto = new LinkDto()
        linkDto.removeLink()
        linkDto.setLink(itemDto1, itemDto3)

        links.add(linkDto)

        questionDto.getQuestionDetailsDto().setItemsL(items)
        questionDto.getQuestionDetailsDto().setItemsR(items)
        questionDto.getQuestionDetailsDto().setLinks(links)
    }

    def 'export and import questions to xml'() {
        given: 'a xml with questions'
        def questionsXml = questionService.exportQuestionsToXml()
        print questionsXml
        and: 'a clean database'
        questionService.removeQuestion(questionId)

        when:
        questionService.importQuestionsFromXml(questionsXml)

        then:
        questionRepository.findQuestions(externalCourse.getId()).size() == 1
        def questionResult = questionService.findQuestions(externalCourse.getId()).get(0)
        questionResult.getQuestionDetails().getLinks().size() == 1
        def link = questionResult.getQuestionDetails().getLinks().get(0)
        link.get(0) == item1
        link.get(1) == item3
        link.get(0).getContent() == ITEM_2_CONTENT
        link.get(1).getContent() == ITEM_1_CONTENT
    }

    def 'export to latex'() {
        when:
        def questionsLatex = questionService.exportQuestionsToLatex()

        then:
        questionsLatex != null
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
