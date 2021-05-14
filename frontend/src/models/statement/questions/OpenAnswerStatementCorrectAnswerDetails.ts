import StatementCorrectAnswerDetails from '@/models/statement/questions/StatementCorrectAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class OpenAnswerStatementCorrectAnswerDetails extends StatementCorrectAnswerDetails {
  public correctanswer: string | null = null;

  constructor(jsonObj?: OpenAnswerStatementCorrectAnswerDetails) {
    super(QuestionTypes.OpenAnswer);
    if (jsonObj) {
      this.correctanswer = jsonObj.correctanswer || this.correctanswer;
    }
  }
}
