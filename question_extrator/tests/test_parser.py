import pytest
from question_extrator.parser_question import ParserQuestion


@pytest.mark.parametrize(
    'option_input_test',
    [
        'a) bla bla',
        'b) bla bla dads',
        'c) dsjshjsda',
        'd) sadsads',
        'e) hshshs',
    ],
)
def test_is_option_should_recognation_option(option_input_test):
    result = ParserQuestion('').is_option(option_input_test)
    expect = True
    assert expect == result


@pytest.mark.parametrize(
    'non_option_input_test',
    [
        '1) bla bla',
        '2) bla bla dads',
        '3) dsjshjsda',
        '4) sadsads',
        '5) hshshs',
    ],
)
def test_is_option_should_not_recognize_non_option(non_option_input_test):
    result = ParserQuestion('').is_option(non_option_input_test)
    expect = False
    assert expect == result


@pytest.mark.parametrize(
    'empty_input_test',
    [
        '',
        ' ',
        '\t',
        '\n',
    ],
)
def test_is_option_should_not_recognize_empty_input(empty_input_test):
    result = ParserQuestion('').is_option(empty_input_test)
    expect = False
    assert expect == result



@pytest.mark.parametrize(
    'question_input_test',
    [
        '01. bla bla bla',
        '02. bla bla dads',
        '13. dsjshjsda',
        '04. sadsads',
        '55. hshshs',
    ],
)
def test_should_recognize_question(question_input_test):
    result = ParserQuestion('').is_question(question_input_test)
    expect = True
    assert expect == result


@pytest.mark.parametrize(
    'non_question_input_test',
    [
        '1) bla bla',
        'b) bla bla dads',
        'c) dsjshjsda',
        'd1) sadsads',
        'e) hshshs',
    ],
)
def test_should_not_recognize_non_question(non_question_input_test):
    result = ParserQuestion('').is_question(non_question_input_test)
    expect = False
    assert expect == result


@pytest.mark.parametrize(
    'empty_question_input_test',
    [
        '',
        ' ',
        '\t',
        '\n',
    ],
)
def test_should_not_recognize_empty_question(empty_question_input_test):
    result = ParserQuestion('').is_question(empty_question_input_test)
    expect = False
    assert expect == result


def test_should_raise_error_for_invalid_parameters():
    with pytest.raises(ValueError):
        ParserQuestion(1)

def test_should_return_correcly_firt_test():

    with open("./mocks/prova_primeira_etapa_texto.txt", 'r') as f:
        content = f.readlines()

    content = "".join(content)
    expect = 54
    parser_question = ParserQuestion(content)
    parser_question.parser()

    result = len(parser_question.get_questions())

    assert expect==result