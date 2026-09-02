# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repository is

A practice project for Spec Driven Development in Claude Code, built around a
`cashback-reward-service` Spring Boot application. The specs currently lead the code:
`doc/specs/` holds finished feature specs, while `src/` is still the generated
Spring Boot skeleton (`CashbackRewardServiceApplication` plus a `contextLoads` test).
Expect to be implementing against a spec rather than extending existing domain code.

## Build and test

`./mvnw` is broken in this checkout — it looks for `.mvn/wrapper/maven-wrapper.properties`
but the file lives at `wrapper/maven-wrapper.properties`. Use the system `mvn` (3.9.x, Java 25):

```bash
mvn compile                                   # build
mvn test                                      # all tests
mvn test -Dtest=CashbackRewardServiceApplicationTests            # one test class
mvn test -Dtest=CashbackRewardServiceApplicationTests#contextLoads   # one test method
mvn spring-boot:run                           # run the service
```

Java 25, Spring Boot 4.1.1, Lombok (wired via `annotationProcessorPaths` in `pom.xml` —
new modules need no extra config), H2 in-memory with the H2 console starter, Spring Data JPA,
Bean Validation, Spring MVC. Test starters are the Boot 4 split ones
(`spring-boot-starter-{data-jpa,validation,webmvc}-test`), not a single `spring-boot-starter-test`.

Package root is `com.amrullah.cashback_reward_service` (underscored — the artifact id's
hyphenated form is not a valid package name).

## Spec-driven workflow

`/discover "<user story>"` (`.claude/commands/discover.md`) runs Example Mapping over a user
story and writes the result to `doc/specs/<feature>.md`. Its rules matter when editing specs
by hand too:

- Rules are numbered and each starts with **Should…** or **Must…**.
- Examples use "The one where…" prose; when inputs vary independently, a markdown table
  replaces the bullets rather than duplicating them.
- Counter-examples are valid business boundaries or exclusions, never bugs.
- A finished spec contains no open questions — questions are resolved with the user first,
  then folded into the rules.
- Specs stay in business language: no UI steps, no Gherkin, no Given/When/Then.

Existing specs: `doc/specs/cashback.md` (earning, capping, refunds, payout — 12 rules) and
`doc/specs/view-account-history.md` (10 rules, built on top of it). `view-account-history.md`
cites cashback rules by number ("Cashback Rule 8"), so renumbering a rule in `cashback.md`
breaks those references. Shared constraints established there and assumed elsewhere:
USD only, half-up rounding to the cent, $1.00 minimum spend, $50 monthly cap per account,
months evaluated in the member's profile timezone, 24-month history retention.
