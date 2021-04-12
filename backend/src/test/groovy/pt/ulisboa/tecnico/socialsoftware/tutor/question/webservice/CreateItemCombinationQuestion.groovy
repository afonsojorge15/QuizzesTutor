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
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Link
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Item
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateItemCombinationQuestion  extends SpockTest {
    @LocalServerPort
    private int port

    def course
    def question
    def itemDto1
    def itemDto2
    def linkDto
    def mapper
    def teacher
    def questionDto
    def response

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)
        createExternalCourseAndExecution()
        teacher = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        teacher.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        teacher.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(teacher)
        userRepository.save(teacher)
        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
        mapper = new ObjectMapper()
    }

    def "create question"() {
        given: "question created"
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

        when:
        response = restClient.post(
                path: '/courses/' + externalCourse.getId() + '/questions',
                body: mapper.writeValueAsString(questionDto),
                requestContentType: 'application/json'
        )

        then: "check the response status"
        response != null
        response.status == 200
        and: "if it responds with the updated question"
        def result = response.data
        result.title == QUESTION_2_TITLE
        result.content == QUESTION_2_CONTENT

        result.questionDetailsDto.items.size() == 2
        def result1 = result.questionDetailsDto.items.get(0)
        result1.group == true
        def result2 = result.questionDetailsDto.items.get(1)
        result2.group == false
        result.questionDetailsDto.links.get(0) == links
        result1.content == ITEM_1_CONTENT
        result2.content == ITEM_2_CONTENT
    }


    def cleanup() {
        userRepository.deleteById(teacher.getId())
        courseExecutionRepository.deleteById(externalCourseExecution.getId())
        courseRepository.deleteById(externalCourse.getId())
    }
}
