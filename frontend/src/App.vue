<template>
  <div>
    <h1>Quiz System</h1>
    <nav>
      <router-link to="/questions">Questions</router-link>
      |
      <router-link to="/create">Create Question</router-link>
    </nav>
    <router-view />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent({
  name: 'App'
});
</script>

<style scoped>
nav {
  margin-bottom: 1em;
}
nav a {
  margin-right: 0.5em;
}
</style>
