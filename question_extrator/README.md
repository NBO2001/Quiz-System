# question_extrator

`question_extrator` is a small library for turning plain text questions
and their answers into structured objects.  The project was extracted from
`Quiz-System` and exposes utilities to parse questions, answers and to
prepare fully annotated question objects.

## Features

* **ParserQuestion** – splits raw text into questions and choices.
* **ParserCorrectlyOptions** – reads an answer key and maps each
  question number to the correct option letter.
* **QuestionPrepare** – combines questions with the answer key producing
  `QuestionType` objects with `OptionType` entries marked as correct.

All data models are Pydantic models located in `question_extrator/types`.

## Running the tests

The test suite is written with `pytest`.  From the `question_extrator`
folder run:

```bash
pytest
```

`pytest-cov` is listed as a development dependency so coverage information
is generated when the plugin is available.

