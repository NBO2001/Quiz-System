import re
from typing import Optional

from question_extrator.parser_correcly_options import ParserCorrectlyOptions
from question_extrator.parser_question import ParserQuestion
from question_extrator.types import QuestionType


class QuestionPrepare:
    def __init__(
        self,
        content_raw: str,
        awser_raw: Optional[str] = None,
        skip_unkown_awser: bool = True,
    ):
        self.__content = content_raw
        self.__awser_raw = awser_raw
        self.__skip_unknow_awser = skip_unkown_awser

    def run(self) -> list[QuestionType]:

        question_parser = ParserQuestion(content=self.__content)
        question_parser.parser()
        questions = question_parser.get_questions()

        if self.__awser_raw:
            parser_correcly_op = ParserCorrectlyOptions(
                options_raw_content=self.__awser_raw
            )

            correcly_question = parser_correcly_op.parser()

            for question in questions:

                if question.number in correcly_question.keys():
                    op_correct = correcly_question[question.number].lower()

                    for option in question.options:
                        splited_option = [
                            x for x in re.split(r"(^[a-e])\)", option.option, 1) if x
                        ]

                        if len(splited_option) == 2 and splited_option[0] == op_correct:
                            option.option = splited_option[1].strip()
                            option.is_correct = True
                        elif len(splited_option) == 2:
                            option.option = splited_option[1].strip()
                            option.is_correct = False

            if self.__skip_unknow_awser:
                questions = [
                    question
                    for question in questions
                    if len(
                        [
                            option
                            for option in question.options
                            if option.is_correct is not None
                        ]
                    )
                ]

        return questions
