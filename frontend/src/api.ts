export interface QuestionOption {
  text: string;
  isCorrect?: boolean | null;
}

export interface Question {
  id: string;
  content: string;
  options: QuestionOption[];
}

export interface QuestionDto {
  content: string;
  options: QuestionOption[];
}

const API_BASE = 'http://localhost:9000';

export async function getQuestions(): Promise<Question[]> {
  const res = await fetch(`${API_BASE}/questions`);
  if (!res.ok) throw new Error('Failed to fetch questions');
  return res.json();
}

export async function getQuestion(id: string, answered = true): Promise<Question> {
  const res = await fetch(`${API_BASE}/questions/${id}?answered=${answered}`);
  if (!res.ok) throw new Error('Failed to fetch question');
  return res.json();
}

export async function checkAnswer(id: string, answer: string): Promise<boolean> {
  const res = await fetch(`${API_BASE}/questions/check_answer/${id}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ text: answer })
  });
  if (!res.ok) throw new Error('Failed to check answer');
  const data = await res.json();
  return data.isCorrect;
}

export async function createQuestion(q: QuestionDto): Promise<Question> {
  const res = await fetch(`${API_BASE}/questions`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(q)
  });
  if (!res.ok) throw new Error('Failed to create question');
  return res.json();
}

export async function deleteQuestion(id: string): Promise<void> {
  const res = await fetch(`${API_BASE}/questions/${id}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Failed to delete question');
}
