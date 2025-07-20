<template>
  <div>
    <h2>All Questions</h2>
    <ul>
      <li v-for="q in questions" :key="q.id">
        <router-link :to="`/questions/${q.id}`">{{ q.content }}</router-link>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { getQuestions, type Question } from '../api';

export default defineComponent({
  setup() {
    const questions = ref<Question[]>([]);

    onMounted(async () => {
      questions.value = await getQuestions();
    });

    return { questions };
  }
});
</script>
