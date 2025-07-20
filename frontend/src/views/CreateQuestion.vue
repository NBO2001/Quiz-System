<template>
  <div>
    <h2>Create Question</h2>
    <form @submit.prevent="submit">
      <div>
        <label>Content</label>
        <input v-model="content" required />
      </div>
      <div v-for="(o, idx) in options" :key="idx">
        <input v-model="o.text" placeholder="Option text" />
        <label>
          Correct
          <input type="checkbox" v-model="o.isCorrect" />
        </label>
      </div>
      <button type="button" @click="addOption">Add Option</button>
      <button type="submit">Create</button>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { createQuestion, type QuestionOption } from '../api';
import { useRouter } from 'vue-router';

export default defineComponent({
  setup() {
    const router = useRouter();
    const content = ref('');
    const options = ref<QuestionOption[]>([{ text: '', isCorrect: false }]);

    const addOption = () => {
      options.value.push({ text: '', isCorrect: false });
    };

    const submit = async () => {
      await createQuestion({
        content: content.value,
        options: options.value
      });
      router.push('/questions');
    };

    return { content, options, addOption, submit };
  }
});
</script>
