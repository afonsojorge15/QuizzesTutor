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
class UpdateMultipleChoiceQuestion extends SpockTest {
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
        questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.SUBMITTED.name())

        optionDto1 = new OptionDto()
        optionDto1.setContent(OPTION_1_CONTENT)
        optionDto1.setCorrect(true)

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

    def "edit question"() {
        given: "a questionDto"

        def newQuestion = questionDto
        newQuestion.setTitle(QUESTION_2_TITLE)
        newQuestion.setContent(QUESTION_2_CONTENT)

        def option1 = optionDto1
        option1.setContent(OPTION_2_CONTENT)
        option1.setRelevance(0)

        def option2 = optionDto2
        option2.setContent(OPTION_2_CONTENT)
        option2.setCorrect(true)
        option2.setRelevance(4)

        def option3 = optionDto3
        option3.setContent(OPTION_2_CONTENT)
        option3.setCorrect(true)
        option3.setRelevance(3)

        def options = new ArrayList<OptionDto>()
        options.add(option1)
        options.add(option2)
        options.add(option3)

        newQuestion.getQuestionDetailsDto().setOptions(options)

        when: "sent"
        response = restClient.put(
                path: '/questions/' + question.getId(),
                body: JsonOutput.toJson(newQuestion),
                requestContentType: 'application/json'
        )

        then: "check the response"
        response != null
        response.status == 200
        and: "if it matches the updated question"
        def result = response.data
        result.title == QUESTION_2_TITLE
        result.content == QUESTION_2_CONTENT
        result.questionDetailsDto.options.size() == 6
        def result1 = result.questionDetailsDto.options.get(3)
        result1.correct == false
        result1.relevance == 0
        def result2 = result.questionDetailsDto.options.get(4)
        result2.correct == true
        result2.relevance == 4
        def result3 = result.questionDetailsDto.options.get(5)
        result3.correct == true
        result3.relevance == 3
        result1.content == OPTION_2_CONTENT
        result1.content == result2.content
        result2.content == result3.content
    }

    def cleanup() {
        userRepository.deleteById(teacher.getId())
        courseExecutionRepository.deleteById(externalCourseExecution.getId())
        courseRepository.deleteById(externalCourse.getId())
    }
}