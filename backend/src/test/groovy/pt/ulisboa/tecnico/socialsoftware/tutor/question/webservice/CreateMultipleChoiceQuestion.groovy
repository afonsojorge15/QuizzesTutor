package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovy.json.JsonOutput
import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateMultipleChoiceQuestion extends SpockTest {
    @LocalServerPort
    private int port

    def course
    def question
    def optionDto1
    def optionDto2
    def optionDto3
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
    }

    def "create question"() {
        given: "question created"
        questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.SUBMITTED.name())

        optionDto1 = new OptionDto()
        optionDto1.setContent(OPTION_1_CONTENT)
        optionDto1.setCorrect(true)
        optionDto1.setRelevance(5)

        optionDto2 = new OptionDto()
        optionDto2.setContent(OPTION_1_CONTENT)
        optionDto2.setCorrect(false)

        optionDto3 = new OptionDto()
        optionDto3.setContent(OPTION_1_CONTENT)
        optionDto3.setCorrect(false)

        def options = new ArrayList<OptionDto>()
        options.add(optionDto1)
        options.add(optionDto2)
        options.add(optionDto3)

        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        questionDto.getQuestionDetailsDto().setOptions(options)

        when: "sent"
        response = restClient.post(
                path: '/courses/' + externalCourse.getId()  + '/questions',
                body: JsonOutput.toJson(questionDto),
                requestContentType: 'application/json'
        )

        then: "check the response"
        response != null
        response.status == 200
        and: "if it matches the original question"
        def result = response.data
        result.title == QUESTION_1_TITLE
        result.content == QUESTION_1_CONTENT
        result.questionDetailsDto.options.size() == 3
        def result1 = result.questionDetailsDto.options.get(0)
        result1.correct == true
        result1.relevance == 5
        def result2 = result.questionDetailsDto.options.get(1)
        result2.correct == false
        result2.relevance == 0
        def result3 = result.questionDetailsDto.options.get(2)
        result3.correct == false
        result3.relevance == 0
        result1.content == OPTION_1_CONTENT
        result1.content == result2.content
        result2.content == result3.content
    }

    def cleanup() {
        userRepository.deleteById(teacher.getId())
        courseExecutionRepository.deleteById(externalCourseExecution.getId())
        courseRepository.deleteById(externalCourse.getId())
    }
}

