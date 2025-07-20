import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import QuestionList from '../views/QuestionList.vue';
import QuestionDetail from '../views/QuestionDetail.vue';
import CreateQuestion from '../views/CreateQuestion.vue';

const routes: Array<RouteRecordRaw> = [
  { path: '/', redirect: '/questions' },
  { path: '/questions', component: QuestionList },
  { path: '/questions/:id', component: QuestionDetail, props: true },
  { path: '/create', component: CreateQuestion }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
