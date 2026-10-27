---
model: sonnet
description: Run one TDD cycle (RED → GREEN → REFACTOR → CHALLENGE → STOP)
argument-hint: "<@Nested Rule class or Example method to drive from an AcceptanceIT class>"
---

Run ONE TDD cycle for: $ARGUMENTS

Read CLAUDE.md for architecture and testing conventions before writing any code.

The acceptance test is the OUTER loop. It stays red while you work through the INNER loop
of unit tests below. The acceptance test going green never replaces a unit test.

## RED — confirm the failure

Outer: run the failing acceptance test. Read the failure message.
Understand WHY it fails before writing any production code.
If the test already passes, STOP — something is wrong.

Inner: pick the next missing piece the failure points to (domain, then application,
then adapters). Write ONE failing unit test for it and run it. See it fail for the
right reason. NO production code before this inner RED.

| Production code about to be created or changed | Test to write FIRST | Style |
|---|---|---|
| domain/model, domain/service | unit test beside the code | plain JUnit + AssertJ, NO Spring |
| application service | unit test with fake/stub ports | plain JUnit, no Spring |
| adapter/in/web controller | controller test | @WebMvcTest |
| adapter/out/persistence repository | repository test | @DataJpaTest |

## GREEN — minimum code to pass

Write the MINIMUM production code to make the CURRENT failing unit test pass.
Then repeat inner RED → GREEN for the next missing piece, until the acceptance test is green.
Minimum means minimum:
- No extra methods "while we're here"
- No anticipating the next test
- No abstractions until refactoring demands them
- Hard-code if that's all the current test requires

Never create or change a production class without a failing test for it first.
A green acceptance test does not count as that test.

Respect architecture boundaries:
- Domain code: pure Java, no Spring, no JPA
- Controllers: thin delegation, no business logic
- Persistence: JPA entities stay in adapter layer
- Let the scoped rules guide you

## REFACTOR — clean up with confidence
All tests are green. Now improve the code:
- Remove duplication
- Extract clear names
- Simplify conditionals
- Check that the code reads like the spec

Run ALL tests after refactoring — not just the current one.
If anything breaks, fix it before moving on.

## CHALLENGE — drive out edge cases
Before stopping, ask yourself:
"What else should this do?"
"What input could break this?"

Consider: zero/empty input, not-found, boundary values, rounding, invalid state, null, negative amounts,
duplicate requests.

Propose at least one edge case to the user.
If approved, that edge case becomes the next RED.

## STOP
Report what you changed:
- Which acceptance test is now passing
- Unit tests written (file + method), one per new or changed production class
- What production code you wrote or modified
- What you refactored
- What edge case you propose next

Before reporting, check: every new or changed production class has a unit test beside it
of the right style. If one is missing, write it (RED first) before stopping.

Do NOT write tests for behaviour outside the rule being driven (edge cases wait for approval).
Do NOT add unrequested features or "improvements".
Do NOT modify any existing test to make it pass — fix the production code instead.

Wait for the user before starting the next cycle.
