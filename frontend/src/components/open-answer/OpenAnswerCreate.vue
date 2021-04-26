<template>
  <div>
      <v-row>
        <v-text-field
          v-model="sQuestionDetails.answer"
          :rules="[(v) => !!v || 'Answer is required']"
          label="Answer"
          required
          data-cy="questionAnswerTextArea"
        />
      </v-row>
  </div>
</template>


<script lang="ts">
import { Component, Model, PropSync, Vue, Watch } from 'vue-property-decorator';
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';

@Component
export default class OpenAnswerCreate extends Vue {
  @PropSync('questionDetails', { type: OpenAnswerQuestionDetails })
  sQuestionDetails!: OpenAnswerQuestionDetails;

}
</script>
