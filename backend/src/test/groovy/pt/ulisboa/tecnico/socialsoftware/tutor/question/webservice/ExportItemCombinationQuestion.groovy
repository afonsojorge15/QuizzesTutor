package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto

import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.LinkDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Item
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Link


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExportItemCombinationQuestion extends SpockTest {
    @LocalServerPort
    private int port

    def courseExecutionDto

    def question

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)
        createExternalCourseAndExecution()
        def questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.SUBMITTED.name())

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


        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        questionDto.getQuestionDetailsDto().setItemsLeft(items)
        questionDto.getQuestionDetailsDto().setItemsRight(items)
        questionDto.getQuestionDetailsDto().setLinkS(links)

        questionService.createQuestion(externalCourse.getId(), questionDto)
        question = questionRepository.findAll().get(0)
    }

    def "teacher exports a course"() {
        given: 'a demon teacher'
        def teacher
        teacher = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        teacher.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        teacher.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(teacher)
        userRepository.save(teacher)
        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/courses/" + externalCourseExecution.getId() + "/questions/export",
                requestContentType: "application/json"
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null
    }
}
