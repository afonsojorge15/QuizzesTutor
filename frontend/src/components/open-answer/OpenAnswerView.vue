<template>
  <div>
    <v-row>
      <span
        v-html="
          convertMarkDown(
            '**[★]** ' + questionDetails.answer
        )"
      />
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Question from '@/models/management/Question';
import Image from '@/models/management/Image';
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';

@Component
export default class MultipleChoiceView extends Vue {
  @Prop() readonly questionDetails!: OpenAnswerQuestionDetails;

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>
