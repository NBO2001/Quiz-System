import re
from typing import Optional

from pydantic import BaseModel

from question_extrator.types import OptionType

PATTERN_SPLIT = r'(\d{1,2})\.'


class QuestionType(BaseModel):

    statement: str
    options: list[OptionType]
    discipline: str
    number: Optional[int]

    @classmethod
    def from_raw(
        cls, raw_statement: str, options: list[OptionType], discipline: str
    ):
        splited_text = [
            x for x in re.split(PATTERN_SPLIT, raw_statement, 1) if x
        ]

        if len(splited_text) == 2:
            number = int(splited_text[0])
            stmt = splited_text[1].strip()
        else:
            number = None
            stmt = raw_statement

        return cls(
            statement=stmt,
            options=options,
            discipline=discipline,
            number=number,
        )
