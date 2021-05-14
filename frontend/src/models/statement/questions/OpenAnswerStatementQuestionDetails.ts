import StatementQuestionDetails from '@/models/statement/questions/StatementQuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import StatementOption from '@/models/statement/StatementOption';
import { _ } from 'vue-underscore';

export default class OpenAnswerStatementQuestionDetails extends StatementQuestionDetails {
  answer: string = '';

  constructor(jsonObj?: OpenAnswerStatementQuestionDetails) {
    super(QuestionTypes.OpenAnswer);
    if (jsonObj) {
      if (jsonObj.answer) {
        this.answer = jsonObj.answer;
      }
    }
  }
}
