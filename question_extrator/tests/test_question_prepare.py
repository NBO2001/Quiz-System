import pytest

from question_extrator.parser_question import ParserQuestion
from question_extrator.question_prepare import QuestionPrepare
from question_extrator.types import OptionType, QuestionType


def test_should_return_correcly_without_3_questions():

    with open("./mocks/prova_primeira_etapa_texto.txt", "r") as f:
        content = f.readlines()

    with open("./mocks/mock_awser.txt", "r") as f:
        awser = f.read()

    content = "".join(content)

    expect = 51

    question_prepare = QuestionPrepare(content_raw=content, awser_raw=awser)

    result = len(question_prepare.run())

    assert expect == result
