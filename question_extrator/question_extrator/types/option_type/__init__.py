from typing import Optional

from pydantic import BaseModel


class OptionType(BaseModel):
    option: str
    is_correct: Optional[bool] = None
    explanation: Optional[str] = None
