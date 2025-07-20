## Overview
Agents automate repetitive tasks and maintain consistent standards across this
monorepo. They help run checks, enforce style rules, and keep documentation up
to date. Each project has a README that describes how to set up the environment
and run tests. Agents should consult those documents before executing commands.

## Responsibilities
- Keep the codebase free of spelling mistakes.
- Ensure documentation is clear and concise.
- Enforce consistent code style within each project.

## Backend
Agents working in the Scala `backend` project must:
- Use `scalafmt` for formatting (`sbt scalafmt`).
- Run `sbt test` to validate changes.
- Review `backend/README.md` for any additional setup steps.

## Question Extractor
Agents working in the Python `question_extractor` project (directory
`question_extrator`) must:
- Format the code with`make format`.
- Execute tests with `make test`.
- Review `question_extrator/README.md` for project details.

## Contributing Agents
If you add or update an agent:
- Describe the change in a pull request using the provided template.
- Include new or updated tests when required.
- Ensure linting passes before requesting review.
