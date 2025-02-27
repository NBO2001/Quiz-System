import re

PATTERN_AWSER = re.compile(r'(\d{1,2})\. ([A-E])[\s|\n]')


class ParserCorrectlyOptions:
    def __init__(self, options_raw_content: str):
        self._raw_content = options_raw_content

    def parser(self) -> dict[int, str]:
        matches = PATTERN_AWSER.findall(self._raw_content)
        return {int(match[0]): match[1] for match in matches}
