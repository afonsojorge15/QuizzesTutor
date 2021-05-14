<template>
  <div>
    <v-text-field
      v-model="answerDetails.answer"
      label="Answer"
      data-cy="questionAnswerTextArea"
    />
    <span
      v-model="correctanswerDetails.answer"
      label="Correct Answer"
      data-cy="correctAnswerTextArea"
    />
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import draggable from 'vuedraggable';
import OpenAnswerStatementQuestionDetails from '@/models/statement/questions/OpenAnswerStatementQuestionDetails';
import OpenAnswerStatementAnswerDetails from '@/models/statement/questions/OpenAnswerStatementAnswerDetails';
import OpenAnswerStatementCorrectAnswerDetails from '@/models/statement/questions/OpenAnswerStatementCorrectAnswerDetails';
import BaseCodeEditor from '@/components/BaseCodeEditor.vue';

@Component
export default class OpenAnswerAnswer extends Vue {
  @Prop(OpenAnswerStatementQuestionDetails)
  readonly questionDetails!: OpenAnswerStatementQuestionDetails;
  @Prop(OpenAnswerStatementAnswerDetails)
  answerDetails!: OpenAnswerStatementAnswerDetails;
  @Prop(OpenAnswerStatementCorrectAnswerDetails)
  readonly correctanswerDetails!: OpenAnswerStatementCorrectAnswerDetails;
}
</script>

