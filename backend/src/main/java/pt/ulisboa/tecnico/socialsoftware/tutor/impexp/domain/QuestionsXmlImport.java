package pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.CourseRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Languages;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

public class QuestionsXmlImport {
    private QuestionService questionService;
    private CourseRepository courseRepository;

    public void importQuestions(InputStream inputStream, QuestionService questionService, CourseRepository courseRepository) {
        this.questionService = questionService;
        this.courseRepository = courseRepository;

        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        Document doc;
        try {
            Reader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
            doc = builder.build(reader);
        } catch (FileNotFoundException e) {
            throw new TutorException(QUESTIONS_IMPORT_ERROR, "File not found");
        } catch (JDOMException e) {
            throw new TutorException(QUESTIONS_IMPORT_ERROR, "Coding problem");
        } catch (IOException e) {
            throw new TutorException(QUESTIONS_IMPORT_ERROR, "File type or format");
        }

        if (doc == null) {
            throw new TutorException(QUESTIONS_IMPORT_ERROR, "File not found ot format error");
        }

        importQuestions(doc);
    }

    public void importQuestions(String questionsXml, QuestionService questionService, CourseRepository courseRepository) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        InputStream stream = new ByteArrayInputStream(questionsXml.getBytes());

        importQuestions(stream, questionService, courseRepository);
    }

    private void importQuestions(Document doc) {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//questions/question", Filters.element());
        for (Element element : xp.evaluate(doc)) {
            importQuestion(element);
        }
    }

    private void importQuestion(Element questionElement) {
        String courseType = questionElement.getAttributeValue("courseType");
        String courseName = questionElement.getAttributeValue("courseName");
        Integer key = Integer.valueOf(questionElement.getAttributeValue("key"));
        String content = questionElement.getAttributeValue("content");
        String title = questionElement.getAttributeValue("title");
        String status = questionElement.getAttributeValue("status");
        String creationDate = questionElement.getAttributeValue("creationDate");
        String type = questionElement.getAttributeValue("type");

        QuestionDto questionDto = new QuestionDto();
        questionDto.setKey(key);
        questionDto.setContent(content);
        questionDto.setTitle(title);
        questionDto.setStatus(status);
        questionDto.setCreationDate(creationDate);

        Element imageElement = questionElement.getChild("image");
        if (imageElement != null) {
            Integer width = imageElement.getAttributeValue("width") != null ?
                    Integer.valueOf(imageElement.getAttributeValue("width")) : null;
            String url = imageElement.getAttributeValue("url");

            ImageDto imageDto = new ImageDto();
            imageDto.setWidth(width);
            imageDto.setUrl(url);

            questionDto.setImage(imageDto);
        }

        QuestionDetailsDto questionDetailsDto;
        switch (type) {
            case Question.QuestionTypes.MULTIPLE_CHOICE_QUESTION:
                questionDetailsDto = importMultipleChoiceQuestion(questionElement);
                break;
            case Question.QuestionTypes.CODE_FILL_IN_QUESTION:
                questionDetailsDto = importCodeFillInQuestion(questionElement);
                break;
            case Question.QuestionTypes.CODE_ORDER_QUESTION:
                questionDetailsDto = importCodeOrderQuestion(questionElement);
                break;
            case Question.QuestionTypes.ITEM_COMBINATION_QUESTION:
                questionDetailsDto = importItemCombinationQuestion(questionElement);
                break;
            default:
                throw new TutorException(QUESTION_TYPE_NOT_IMPLEMENTED, type);
        }

        questionDto.setQuestionDetailsDto(questionDetailsDto);

        Course course = courseRepository.findByNameType(courseName, courseType).orElseThrow(() -> new TutorException(COURSE_NOT_FOUND, courseName));
        questionService.createQuestion(course.getId(), questionDto);
    }

    private QuestionDetailsDto importMultipleChoiceQuestion(Element questionElement) {
        List<OptionDto> optionDtos = new ArrayList<>();
        for (Element optionElement : questionElement.getChild("options").getChildren("option")) {
            Integer optionSequence = Integer.valueOf(optionElement.getAttributeValue("sequence"));
            Integer optionRelevance = Integer.valueOf(optionElement.getAttributeValue("relevance"));
            String optionContent = optionElement.getAttributeValue("content");
            boolean correct = Boolean.parseBoolean(optionElement.getAttributeValue("correct"));

            OptionDto optionDto = new OptionDto();
            optionDto.setSequence(optionSequence);
            optionDto.setContent(optionContent);
            optionDto.setCorrect(correct);
            optionDto.setRelevance(optionRelevance);

            optionDtos.add(optionDto);
        }
        MultipleChoiceQuestionDto multipleChoiceQuestionDto = new MultipleChoiceQuestionDto();
        multipleChoiceQuestionDto.setOptions(optionDtos);
        return multipleChoiceQuestionDto;
    }

    private QuestionDetailsDto importCodeFillInQuestion(Element questionElement) {
        CodeFillInQuestionDto questionDto = new CodeFillInQuestionDto();
        questionDto.setCode(questionElement.getChildText("code"));
        questionDto.setLanguage(Languages.valueOf(questionElement.getChild("code").getAttributeValue("language")));
        var spots = new ArrayList<CodeFillInSpotDto>();
        for (Element spotElement : questionElement.getChild("fillInSpots").getChildren("fillInSpot")) {
            var spot = new CodeFillInSpotDto();
            spot.setSequence(Integer.valueOf(spotElement.getAttributeValue("sequence")));
            var options = new ArrayList<OptionDto>();
            for (Element optionElement : spotElement.getChildren("fillInOption")) {
                Integer optionSequence = Integer.valueOf(optionElement.getAttributeValue("sequence"));
                String optionContent = optionElement.getAttributeValue("content");
                boolean correct = Boolean.parseBoolean(optionElement.getAttributeValue("correct"));

                OptionDto optionDto = new OptionDto();
                optionDto.setSequence(optionSequence);
                optionDto.setContent(optionContent);
                optionDto.setCorrect(correct);

                options.add(optionDto);
            }

            spot.setOptions(options);
            spots.add(spot);
        }
        questionDto.setFillInSpots(spots);
        return questionDto;
    }

    private QuestionDetailsDto importCodeOrderQuestion(Element questionElement) {
        CodeOrderQuestionDto questionDto = new CodeOrderQuestionDto();
        questionDto.setLanguage(Languages.valueOf(questionElement.getChild("orderSlots").getAttributeValue("language")));
        var slots = new ArrayList<CodeOrderSlotDto>();
        for (Element slotElement : questionElement.getChild("orderSlots").getChildren("slot")) {
            var slot = new CodeOrderSlotDto();
            slot.setOrder(Integer.valueOf(slotElement.getAttributeValue("order")));
            slot.setSequence(Integer.valueOf(slotElement.getAttributeValue("sequence")));
            slot.setContent(slotElement.getValue());

            slots.add(slot);
        }
        questionDto.setCodeOrderSlots(slots);
        return questionDto;
    }

    private QuestionDetailsDto importItemCombinationQuestion(Element questionElement) {



        List<ItemDto> itemDtos = new ArrayList<>();
        for (Element itemsLeftElement : questionElement.getChild("itemLeft").getChildren("item")) {
            Integer itemLeftSequence = Integer.valueOf(itemsLeftElement.getAttributeValue("sequence"));
            String itemLeftContent = itemsLeftElement.getAttributeValue("content");
            Boolean itemLeftGroup = Boolean.parseBoolean(itemsLeftElement.getAttributeValue("group"));


            ItemDto itemDto = new ItemDto();
            itemDto .setSequence(itemLeftSequence);
            itemDto .setContent(itemLeftContent);
            itemDto .setGroup(itemLeftGroup);

            itemDtos.add(itemDto);
        }
        List<ItemDto> itemDtos1 = new ArrayList<>();
        for (Element itemsRightElement : questionElement.getChild("itemLeft").getChildren("item")) {
            Integer itemRightSequence = Integer.valueOf(itemsRightElement.getAttributeValue("sequence"));
            String itemRightContent = itemsRightElement.getAttributeValue("content");
            Boolean itemRightGroup = Boolean.parseBoolean(itemsRightElement.getAttributeValue("group"));


            ItemDto itemDto1 = new ItemDto();
            itemDto1 .setSequence(itemRightSequence);
            itemDto1 .setContent(itemRightContent);
            itemDto1 .setGroup(itemRightGroup);

            itemDtos1.add(itemDto1);
        }

        List<LinkDto> linkDtos = new ArrayList<>();

        ItemCombinationQuestionDto itemCombinationQuestionDto = new ItemCombinationQuestionDto();
        itemCombinationQuestionDto.setItemsLeft(itemDtos);
        itemCombinationQuestionDto.setItemsLeft(itemDtos1);
        itemCombinationQuestionDto.setLinkS(linkDtos);
        return itemCombinationQuestionDto;




    }




}
