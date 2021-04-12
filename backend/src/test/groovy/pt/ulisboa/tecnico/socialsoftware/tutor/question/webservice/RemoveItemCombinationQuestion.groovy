package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.LinkDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.dto.QuestionSubmissionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RemoveItemCombinationQuestion extends SpockTest {
    @LocalServerPort
    private int port

    def course
    def courseExecution
    def teacher
    def student
    def questionSubmission
    def response
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



        teacher = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        teacher.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        teacher.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(teacher)
        userRepository.save(teacher)
        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
    }

    def "remove question submission"() {
        when:
        response = restClient.delete(
                path: '/questions/' + question.getId(),
                requestContentType: 'application/json'
        )

        then: "check the response status"
        response != null
        response.status == 200
    }

    def cleanup() {
        userRepository.deleteById(teacher.getId())
        courseExecutionRepository.deleteById(externalCourseExecution.getId())

        courseRepository.deleteById(externalCourse.getId())
    }
}