import re

from question_extrator.types import OptionType, QuestionType

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
            raise ValueError('content not is str type')

        self.questions: list[QuestionType] = []
        self.__questions = []
        self.content = [
            word.strip()
            for word in content.split('\n')
            if len(word.strip()) > 0
        ]

    def is_question(self, text: str) -> bool:
        return re.match(r'^\d{1,2}\.\s', text) != None

    def is_option(self, text: str) -> bool:
        return re.match(r'^[a-e]\)', text) != None

    def __convert_question__(self, question: dict) -> QuestionType:
        options = []
        for option in question['options']:
            options.append(OptionType(option=option))
        return QuestionType.from_raw(
            raw_statement=question['statement'],
            options=options,
            discipline=question['discipline'],
        )

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
                    'statement': line,
                    'options': [],
                    'discipline': discipline.lower(),
                }

                self.__questions.append(question)
            elif self.is_option(line):
                question_started = False
                option_finding = True
                self.__questions[-1]['options'].append(line)
            elif question_started:
                self.__questions[-1]['statement'] += ' ' + line
            elif option_finding:
                self.__questions[-1]['options'][-1] += ' ' + line

        for question in self.__questions:
            self.questions.append(self.__convert_question__(question))

    def get_questions(
        self,
    ) -> list[QuestionType]:
        return self.questions
