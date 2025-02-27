from question_extrator.parser_correcly_options import ParserCorrectlyOptions


def test_should_return_correctly_options():
    options_raw_content = '01. A\n02. B\n03. ANULADA\n04. D\n05. E\n'
    parser = ParserCorrectlyOptions(options_raw_content)
    expected = {1: 'A', 2: 'B', 4: 'D', 5: 'E'}
    assert parser.parser() == expected
