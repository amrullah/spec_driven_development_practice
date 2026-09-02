# Cashback on Purchases — Example Map

**Story**
As a customer, I want to earn cashback on my purchases so that I am rewarded for my loyalty.

**Shared constraints**
- All amounts in USD; monetary results rounded half-up to the cent.
- Foreign-currency transactions are not eligible for cashback (Rule 5).
- "Month" = calendar month evaluated in the member's profile timezone (Rule 9).
- The cap and the cashback balance belong to the account, not to an individual cardholder (Rule 8).
- Cashback is paid at calendar month end, with no exceptions (Rule 12).

---

## Rule 1 — Should accrue cashback on qualifying spend at the member's tier rate

| Membership tier | Qualifying spend | Cashback accrued | Note |
|---|---|---|---|
| Standard | $10.00 | $0.10 | normal case, 1% |
| Premium | $10.00 | $0.20 | normal case, 2% |
| Standard | $1.00 | $0.01 | minimum eligible spend, smallest accrual possible |
| Premium | $1.00 | $0.02 | |
| Standard | $0.99 | none | below the $1.00 minimum, no accrual at all (Rule 2) |

- Example: The one where a Standard member spends $49.99 and accrues $0.50 (0.4999 rounds half-up).
- Because the minimum eligible spend is $1.00, a qualifying transaction always accrues at least $0.01 — a $0.00 accrual row cannot occur.

## Rule 2 — Must not accrue cashback on transactions below $1.00

| Settled qualifying spend | Eligible? | Cashback (Standard) |
|---|---|---|
| $1.00 | Yes | $0.01 |
| $0.99 | No | none |

- The minimum is tested against the settled amount, the same amount the accrual is calculated on (Rule 6).
- Counter-example: The one where a member makes ten separate $0.99 purchases in a day, spends $9.90 in total, and earns nothing — the minimum applies per transaction, never to a daily or monthly aggregate.
- Counter-example: The one where a $1.05 authorisation settles at $0.95 and becomes ineligible, because eligibility follows settlement.

## Rule 3 — Must round each accrual half-up to the cent per transaction, not per month

- Example: The one where a Standard member makes three $1.50 purchases in a month and earns $0.06 total (each $0.015 rounds to $0.02) rather than $0.05 (rounding the $0.045 aggregate).
- Counter-example: The one where a single $4.50 purchase earns $0.05 — the same spend split across three transactions yields $0.06, and that difference is accepted.

## Rule 4 — Must apply the tier rate that is in effect at authorisation of the purchase

- Example: The one where a member upgrades to Premium mid-month: purchases authorised before the upgrade earn 1%, purchases authorised after earn 2%, and nothing is recalculated.
- Example: The one where a purchase is authorised while the member is Premium and settles after they downgrade — it still accrues 2%, because the rate is captured at authorisation and carried through to settlement.
- Example: The one where a member on a Premium trial earns 2%; the trial lapses to Standard and the 2% already accrued is retained.
- Example: The one where a forced-post transaction arrives with no authorisation record and accrues at the Standard 1% rate even though the member is Premium — with no authorisation there is no tier to read.
- Counter-example: The one where a member is upgraded to Premium on the 20th and purchases from the 1st to the 19th are *not* re-rated — an upgrade never applies retroactively, and no promotion may promise otherwise.
- A grace period following a missed payment is a standing question, not a rate question: accrual stops entirely while the account is delinquent (Rule 7).

## Rule 5 — Should accrue only on qualifying spend, excluding non-qualifying transaction types

| Transaction type | Qualifies? |
|---|---|
| Retail purchase of goods/services | Yes |
| Subscription renewal / recurring billing | Yes |
| Gift card / stored-value top-up | No |
| Cash advance or ATM withdrawal | No |
| Account, late or interest fees | No |
| Balance transfer / P2P transfer | No |
| Chargeback-reversed purchase | No |
| Purchase authorised in a currency other than USD | No |
| Merchant in an excluded category (gambling, crypto, money orders) | No |

- Qualifying spend is the settled total **excluding sales tax** where the merchant itemises it. Shipping and tips are included. Where tax is not itemised, the full settled amount is used.
- A purchase is foreign-currency if the currency presented at authorisation is not USD. Where no authorisation record exists (a forced post), the settled currency decides.
- The excluded merchant categories are held as a configurable MCC list with an effective date. A transaction is judged by the list in force on its purchase date, so a mid-month change never revisits cashback already awarded.
- Counter-example: The one where a member buys a $200 gift card and later spends it on goods — cashback accrues on the later goods purchase, not on the gift card purchase, and never on both.
- Counter-example: The one where two identical $100 baskets accrue different cashback because one merchant itemises sales tax and the other does not — accepted as a consequence of the data available.
- Counter-example: The one where a merchant category is added to the exclusion list on the 15th and a purchase at that merchant on the 10th keeps its cashback.

## Rule 6 — Must accrue only on settled transactions, never on authorisations alone, and always on the settled amount

- Example: The one where a $100 purchase is authorised on the 30th and settles on the 2nd, and cashback is recognised on settlement — while still counting against the cap of the month the purchase was authorised in (Rule 8).
- Example: The one where a $50 fuel authorisation settles at $62 and cashback accrues on $62.
- Counter-example: The one where a $100 hotel authorisation settles at $80 and cashback accrues on $80, not on the amount held.
- Counter-example: The one where an authorisation is still unsettled 30 days after the purchase, is treated as expired, and no cashback is ever accrued or later reversed.

## Rule 7 — Must not accrue cashback unless the account is in good standing at both authorisation and settlement

- An account is delinquent from one day past due, and returns to good standing the moment it is brought current.
- Example: The one where a suspended account makes a purchase and no cashback is accrued.
- Example: The one where an account goes delinquent on the 10th — purchases from the 10th accrue nothing, and when the account is brought current on the 20th accrual resumes from the 20th with nothing backdated for the gap.
- Example: The one where a purchase is authorised on the 5th in good standing, the account falls one day past due on the 8th, and the transaction settles on the 9th — no cashback accrues, because standing must hold at both moments.
- Example: The one where a member who has never been suspended closes their own account in good standing and the cashback earned before closure is paid at the next calendar month end, not on the final statement.
- Counter-example: The one where an account is suspended *after* accrual — previously earned cashback is frozen rather than erased, and is released in full when the account returns to good standing.
- Counter-example: The one where a suspended account is closed rather than reinstated and the frozen balance is forfeited — forfeiture follows any closure that comes after a suspension, whatever the reason for it and whoever requested it.

## Rule 8 — Must cap awarded cashback at $50 per account per calendar month

| Already awarded this month | Cashback calculated for new purchase | Awarded | Running total |
|---|---|---|---|
| $0.00 | $2.00 | $2.00 | $2.00 |
| $49.00 | $0.50 | $0.50 | $49.50 |
| $49.00 | $10.00 | $1.00 | $50.00 (partial award at the cap) |
| $50.00 | $5.00 | $0.00 | $50.00 |
| −$7.00 (after a clawback, Rule 11) | $2.00 | $2.00 | −$5.00 |

- The cap is $50 for Standard and Premium alike; a Premium member simply reaches it on half the spend.
- The cap is shared across the whole account — joint holders and additional cardholders draw from one $50 pool.
- A transaction belongs to the month of its purchase (authorisation) date, but headroom is consumed in the order accruals actually arrive at settlement, first come first served. Ties are broken by transaction id.
- The member is notified each time the month's awarded total crosses $40 (80% of the cap) and $50 upward, so a clawback that drops them back below a threshold arms that notification again.
- Counter-example: The one where a Premium member's single $5,000 purchase would calculate $100.00 but is awarded only $50.00, and the unawarded $50.00 does not carry into the next month.
- Counter-example: The one where a purchase made on the 5th settles on the 20th, after later purchases have already consumed the month's headroom — it is awarded only what remains, and the earlier awards are not restated.
- Counter-example: The one where two cardholders on one account each spend heavily and the account still awards $50 in total, not $50 each.

## Rule 9 — Must reset the cap at the first instant of each calendar month in the member's profile timezone

- The profile timezone is mandatory at signup and always populated, so there is no fallback case.
- The purchase (authorisation) timestamp is the one converted into the profile timezone to decide the month.
- Example: The one where a member with a Tokyo profile timezone makes a purchase at 00:10 on 1 February local time and it counts against February's cap, even though it is still January in UTC.
- Example: The one where a member updates their profile timezone on 10 March — March continues to be evaluated under the old timezone and the new one takes effect from 1 April, so no transaction changes month mid-flight.
- Counter-example: The one where a transaction falls in the repeated local hour of a DST fall-back and is assigned to the later (post-transition) occurrence.

## Rule 10 — Must reverse cashback proportionally when a purchase is refunded

| Original spend | Cashback awarded on it | Refund amount | Cashback reversed |
|---|---|---|---|
| $100.00 (Premium) | $2.00 | $100.00 (full) | $2.00 |
| $100.00 (Premium) | $2.00 | $25.00 (partial) | $0.50 |
| $500.00 (Premium, capped) | $1.00 | $500.00 (full) | $1.00 (awarded, not the $10.00 calculated) |
| $500.00 (Premium, capped) | $1.00 | $250.00 (partial) | $0.50 (half of what was awarded) |

- A reversal is always proportional to the cashback **awarded** on that purchase, never to the amount that would have been calculated before the cap.
- Each reversal rounds half-up to the cent, and cumulative reversals on a purchase can never exceed the cashback awarded on it; residual rounding drift is absorbed by the final refund.
- Example: The one where a $100.00 Premium purchase awarded $2.00 is refunded in three parts of $33.34, $33.33 and $33.33 — reversing $0.67, $0.67 and then $0.66, because the cumulative total is held at $2.00.
- Counter-example: The one where a refunded gift-card purchase reverses nothing, because it never accrued cashback.
- Counter-example: The one where a member is refunded more than the original purchase (goodwill overpayment) and the reversal is still limited to the cashback actually awarded — cashback is never reversed below zero for that purchase.

## Rule 11 — Must attribute a reversal to the month in which the cashback was earned, and post it to the current month once that month has closed

- Example: The one where a January purchase is refunded in January, $2.00 is reversed, and January's cap headroom increases by $2.00 so later January purchases can earn again.
- Example: The one where a January purchase is refunded in March after January's cashback has been paid out — the $2.00 is deducted from March, January is never restated or reopened, and March's month-to-date awarded total drops by $2.00 so the member can earn $2.00 more in March.
- Example: The one where a member has earned $5.00 in March and a $12.00 clawback from January posts — March's month-to-date awarded total goes to −$7.00, so the member can earn up to $57.00 gross in March before the cap bites.
- Example: The one where the member has already withdrawn the cashback being clawed back — the deduction posts anyway, the balance goes negative, and cashback earned afterwards pays the debt down first, with only the surplus above zero redeemable.
- Counter-example: The one where a negative balance is still outstanding 90 days later — the shortfall is charged to the member's statement and the cashback balance resets to zero rather than being carried further.
- Counter-example: The one where a closed month's cap is not reopened even though the reversal originated there — the headroom that returns is always the current month's.

## Rule 12 — Should make earned and reversed cashback traceable to the transaction that caused it, and pay it at calendar month end

- An accrual appears as pending against its originating purchase from the moment of settlement, and converts to paid at calendar month end. Reversals show against the pending entry in the same way.
- Payout is always at calendar month end, whatever the member's statement cycle and including at account closure (Rule 7).
- Accrual and reversal history is retained for 24 months.
- Example: The one where a member's statement shows each accrual and each reversal against its originating purchase, plus the month-to-date total against the $50 cap.
- Counter-example: The one where a refund arrives 25 months after the purchase and reverses nothing, because the accrual it would attach to is no longer retained.
