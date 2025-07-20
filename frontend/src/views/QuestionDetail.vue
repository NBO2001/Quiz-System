<template>
  <div v-if="question">
    <h2>{{ question.content }}</h2>
    <ul>
      <li v-for="opt in question.options" :key="opt.text">
        <label>
          <input type="radio" name="opt" :value="opt.text" v-model="selected" />
          {{ opt.text }}
        </label>
      </li>
    </ul>
    <button @click="submitAnswer">Check Answer</button>
    <p v-if="checked">Correct: {{ isCorrect }}</p>
    <button @click="remove">Delete Question</button>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import {
  getQuestion,
  checkAnswer,
  deleteQuestion,
  type Question
} from '../api';
import { useRoute, useRouter } from 'vue-router';

export default defineComponent({
  setup() {
    const route = useRoute();
    const router = useRouter();
    const id = route.params.id as string;
    const question = ref<Question | null>(null);
    const selected = ref('');
    const checked = ref(false);
    const isCorrect = ref(false);

    onMounted(async () => {
      question.value = await getQuestion(id);
    });

    const submitAnswer = async () => {
      isCorrect.value = await checkAnswer(id, selected.value);
      checked.value = true;
    };

    const remove = async () => {
      await deleteQuestion(id);
      router.push('/questions');
    };

    return { question, selected, submitAnswer, checked, isCorrect, remove };
  }
});
</script>
