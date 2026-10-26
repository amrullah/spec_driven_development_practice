---
model: sonnet
description: Write a failing Acceptance Tests for the next spec rule
argument-hint: "<rule name> @docs/specs/<feature>.md"
---

Write a failing acceptance test for: $ARGUMENTS

Read CLAUDE.md for project conventions before writing anything.
Re-read the spec file to understand the full rule, it's examples and it's counter-examples.

## Structure

One Outer class per feature, named <Feature>AcceptanceIT.
One @Nested inner class per rule -- name it after the rule.
One @Test per example from the spec.

Use @DisplayName with the spec's exact business language:
- Class: The rule name
- Method: "The one where..." text from the spec.

## How to test

Test through the REST API using the @SpringBootTest + MockMvc.
Send real HTTP Requests. Assert Real HTTP responses.
NEVER call services or domain objects directly. This is not a unit test.

Assert exact values from the spec examples.
For money: .andExpect(jsonPath("$.amount").value("1.60")) or use isEqualComparingTo with BigDecimal.

## What NOT to do

DO NOT write production code. The test MUST FAIL.
A passing test means you tested nothing.
DO NOT write tests for all rules at once.
One rule only - The one specified in the arguments.
DO NOT invent examples beyond what the spec provides.
The spec is the contract.
DO NOT use mocks in acceptance tests.
Wire the full stack: Controller -> Service -> Domain -> Persistence

## When you're done
Run the test. Confirm it fails for the RIGHT reason:
- Missing Endpoint -> 404 or compilation error (good).
- Wrong value -> not yet, the endpoint shouldn't exist.
- Test passes -> something is wrong, investigate.

Report: which rule you tested, how many examples, and the failure reason

STOP. Do not proceed to the implementation.

