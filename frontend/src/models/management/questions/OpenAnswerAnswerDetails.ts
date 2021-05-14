import AnswerDetails from '@/models/management/questions/AnswerDetails';
import { QuestionTypes, convertToLetter } from '@/services/QuestionHelpers';
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';

export default class OpenAnswerType extends AnswerDetails {
  public answer: string = "";

  constructor(jsonObj?: OpenAnswerType) {
    super(QuestionTypes.OpenAnswer);
    if (jsonObj) {
      this.answer = jsonObj.answer || this.answer;
    }
  }
  isCorrect(questionDetails: OpenAnswerQuestionDetails): boolean {
    return this.answer == questionDetails.answer;
  }
  answerRepresentation(): string {
    return this.answer;
  }
}
