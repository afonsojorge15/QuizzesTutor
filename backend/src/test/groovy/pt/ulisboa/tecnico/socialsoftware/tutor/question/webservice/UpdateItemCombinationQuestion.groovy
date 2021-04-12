package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput
import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.LinkDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class  UpdateItemCombinationQuestion  extends SpockTest {
    @LocalServerPort
    private int port

    def itemDto1
    def itemDto2
    def linkDto
    def course
    def mapper
    def question
    def teacher
    def questionDto
    def response

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)
        createExternalCourseAndExecution()
        mapper = new ObjectMapper()
        questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.SUBMITTED.name())



        itemDto1 = new ItemDto()
        itemDto1.setContent(ITEM_1_CONTENT)
        itemDto1.setGroup(true)
        itemDto2 = new ItemDto()
        itemDto2.setContent(ITEM_2_CONTENT)
        itemDto2.setGroup(false)
        def items = new ArrayList<ItemDto>()
        items.add(itemDto1)
        items.add(itemDto2)
        linkDto = new LinkDto()
        linkDto.setLink(itemDto1, itemDto2)
        def links = new ArrayList<LinkDto>()
        links.add(linkDto)


        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        questionDto.getQuestionDetailsDto().setItemsLeft(items)
        questionDto.getQuestionDetailsDto().setItemsRight(items)
        questionDto.getQuestionDetailsDto().setLinkS(links)

        questionService.createQuestion(externalCourse.getId(), questionDto)
        question = questionRepository.findAll().get(0)



        teacher = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        teacher.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        teacher.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(teacher)
        userRepository.save(teacher)
        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
    }

    def "edit question submission"() {
        given: "a questionDto"

        def newQuestion = questionDto
        newQuestion.setTitle(QUESTION_2_TITLE)
        newQuestion.setContent(QUESTION_2_CONTENT)


        def item1 = itemDto1
        item1.setContent(ITEM_1_CONTENT)
        item1.setGroup(true)
        def item2 = itemDto2
        item2.setContent(ITEM_2_CONTENT)
        item2.setGroup(false)
        def items = new ArrayList<ItemDto>()
        items.add(itemDto1)
        items.add(itemDto2)
        def link = linkDto
        link.setLink(item1, item2)
        def links = new ArrayList<LinkDto>()
        links.add(link)


        newQuestion.getQuestionDetailsDto().setItemsLeft(items)
        newQuestion.getQuestionDetailsDto().setItemsRight(items)
        newQuestion.getQuestionDetailsDto().setLinkS(links)

        when:

        response = restClient.put(
                path: '/questions/' + question.getId(),
                body: mapper.writeValueAsString(newQuestion),
                requestContentType: 'application/json'
        )

        then: "check the response status"
        response != null
        response.status == 200
        and: "if it responds with the updated question"
        def result = response.data
        result.title == QUESTION_2_TITLE
        result.content == QUESTION_2_CONTENT

        result.questionDetailsDto().items.size() == 4
        def result1 = result.questionDetailsDto().items.get(2)
        result1.group == true
        def result2 = result.questionDetailsDto().items.get(3)
        result2.group == false
        result.questionDetails().getLinks().get(0) == links
        result1.content == ITEM_1_CONTENT
        result2.content == ITEM_2_CONTENT
    }

    def cleanup() {
        userRepository.deleteById(teacher.getId())
        courseExecutionRepository.deleteById(externalCourseExecution.getId())
        courseRepository.deleteById(externalCourse.getId())
    }
}