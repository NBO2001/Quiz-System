import re


DISCIPLINES = [
    'LÍNGUA PORTUGUESA',
    'LITERATURA',
    'HISTÓRIA',
    'GEOGRAFIA',
    'BIOLOGIA',
    'QUÍMICA',
    'FÍSICA',
    'MATEMÁTICA',
]


class ParserQuestion:
    def __init__(self, content: str):
        if type(content) is not str:
            raise ValueError("content not is str type")
        
        self.questions = []
        self.content = [
            word.strip()
            for word in content.split('\n')
            if len(word.strip()) > 0
        ]

    def is_question(self, text: str) -> bool:
        return re.match(r'^\d{1,2}\.\s', text) != None

    def is_option(self, text: str) -> bool:
        return re.match(r'^[a-e]\)', text) != None

    def parser(
        self,
    ):
        discipline = None
        question_started = False
        option_finding = False
        for line in self.content:
            if line in DISCIPLINES:
                discipline = line
            elif self.is_question(line):
                question_started = True
                option_finding = False

                question = {
                    'question': line,
                    'options': [],
                    'discipline': discipline.lower(),
                }

                self.questions.append(question)
            elif self.is_option(line):
                question_started = False
                option_finding = True
                self.questions[-1]['options'].append(line)
            elif question_started:
                self.questions[-1]['question'] += ' ' + line
            elif option_finding:
                self.questions[-1]['options'][-1] += ' ' + line

    def get_questions(
        self,
    ):
        return self.questions
