# Cashback on Purchases — Example Map

**Story**
As a customer, I want to earn cashback on my purchases so that I am rewarded for my loyalty.

**Shared constraints**
- All amounts in USD; monetary results rounded half-up to the cent.
- "Month" = calendar month evaluated in the customer's timezone.

---

## Rule 1 — Should accrue cashback on qualifying spend at the member's tier rate

| Membership tier | Qualifying spend | Cashback accrued | Note |
|---|---|---|---|
| Standard | $10.00 | $0.10 | normal case, 1% |
| Premium | $10.00 | $0.20 | normal case, 2% |
| Standard | $0.50 | $0.01 | exactly half a cent, rounds up |

- Example: The one where a Standard member spends $49.99 and accrues $0.50 (0.4999 rounds half-up).
- Counter-example: The one where a Standard member spends $0.25, the calculated $0.0025 rounds to $0.00, and no cashback is accrued at all.
- Questions:
  - Is there a minimum transaction value below which we don't accrue (e.g. no accrual under $1.00)?
  - Do we accrue $0.00 rows at all, or suppress them from the member's statement?

## Rule 2 — Must round each accrual half-up to the cent per transaction, not per month

- Example: The one where a Standard member makes three $0.50 purchases in a month and earns $0.03 total (each rounds to $0.01) rather than $0.02 (rounding the $0.015 aggregate).
- Counter-example: The one where a single $1.50 purchase earns $0.02 — the same spend as three $0.50 purchases yields a different result, and that is accepted.
- Questions:
  - Is per-transaction rounding the intended commercial behaviour, or should we accrue at higher precision and round only at payout?

## Rule 3 — Must apply the tier rate that is in effect at the moment of the purchase

- Example: The one where a member upgrades to Premium mid-month: purchases before the upgrade earn 1%, purchases after earn 2%, and nothing is recalculated.
- Counter-example: The one where a member downgrades to Standard after a large Premium-rate purchase and keeps the 2% already accrued on it.
- Questions:
  - Is tier read at authorisation, at settlement, or at accrual time — these can differ by days?
  - Does an upgrade ever apply retroactively to the current month (a common marketing promise)?
  - What rate applies during a trial or grace period of Premium?

## Rule 4 — Should accrue only on qualifying spend, excluding non-qualifying transaction types

| Transaction type | Qualifies? |
|---|---|
| Retail purchase of goods/services | Yes |
| Gift card / stored-value top-up | No |
| Cash advance or ATM withdrawal | No |
| Account, late or interest fees | No |
| Balance transfer / P2P transfer | No |
| Chargeback-reversed purchase | No |

- Counter-example: The one where a member buys a $200 gift card and later spends it on goods — cashback accrues on the later goods purchase, not on the gift card purchase, and never on both.
- Questions:
  - Does qualifying spend include sales tax, shipping and tips, or only the merchandise subtotal?
  - Are foreign-currency purchases eligible, and at which exchange rate is USD spend determined?
  - Do subscription renewals and recurring billing qualify?
  - Is there a merchant-category exclusion list (gambling, crypto, money orders)?

## Rule 5 — Must accrue only on settled transactions, never on authorisations alone

- Example: The one where a $100 purchase is authorised on the 30th and settles on the 2nd, and cashback is recognised on settlement.
- Counter-example: The one where an authorisation expires without settling (e.g. an abandoned hotel hold) and no cashback is ever accrued or later reversed.
- Questions:
  - Which date drives the monthly cap — purchase date or settlement date? A purchase on the 31st settling on the 1st belongs to only one month.
  - How do we handle a settlement amount that differs from the authorised amount (tips, fuel)?

## Rule 6 — Must not accrue cashback while the member's account is not in good standing

- Example: The one where a suspended account makes a purchase and no cashback is accrued.
- Counter-example: The one where an account is suspended after accrual — previously earned cashback is retained (or frozen) rather than erased.
- Questions:
  - Is cashback on a closed account forfeited, or paid out with the final statement?
  - Does a delinquent-but-open account keep accruing?

## Rule 7 — Must cap awarded cashback at $50 per member per calendar month

| Already awarded this month | Cashback calculated for new purchase | Awarded | Running total |
|---|---|---|---|
| $0.00 | $2.00 | $2.00 | $2.00 |
| $49.00 | $0.50 | $0.50 | $49.50 |
| $49.00 | $10.00 | $1.00 | $50.00 (partial award at the cap) |
| $50.00 | $5.00 | $0.00 | $50.00 |

- Counter-example: The one where a Premium member's single $5,000 purchase would calculate $100.00 but is awarded only $50.00, and the unawarded $50.00 does not carry into the next month.
- Questions:
  - Is the $50 cap the same for Standard and Premium, or is the Premium cap higher?
  - Is the cap per member or per account (joint accounts, additional cardholders, business accounts)?
  - Is the cap applied per transaction in settlement order, and what is the tie-break when two settle at the same instant?
  - Should the member be notified when the cap is reached?

## Rule 8 — Must reset the cap at the first instant of each calendar month in the customer's timezone

- Example: The one where a member in Tokyo makes a purchase at 00:10 on 1 February local time and it counts against February's cap, even though it is still January in UTC.
- Counter-example: The one where a member relocates and changes their timezone mid-month — the already-awarded total for the current month is not recalculated under the new timezone.
- Questions:
  - Which timezone value do we use: profile timezone, billing-address country, or the transaction's local time?
  - What happens to a transaction that falls in the DST overlap hour?
  - Does a timezone change mid-month risk a member exceeding or under-using the cap, and is that acceptable?

## Rule 9 — Must reverse cashback proportionally when a purchase is refunded

| Original spend | Cashback awarded on it | Refund amount | Cashback reversed |
|---|---|---|---|
| $100.00 (Premium) | $2.00 | $100.00 (full) | $2.00 |
| $100.00 (Premium) | $2.00 | $25.00 (partial) | $0.50 |
| $500.00 (Premium, capped) | $1.00 | $500.00 (full) | $1.00 (awarded, not the $10.00 calculated) |
| $500.00 (Premium, capped) | $1.00 | $250.00 (partial) | $0.50 (half of what was awarded) |

- Counter-example: The one where a refunded gift-card purchase reverses nothing, because it never accrued cashback.
- Counter-example: The one where a member is refunded more than the original purchase (goodwill overpayment) and the reversal is still limited to the cashback actually awarded — cashback is never reversed below zero for that purchase.
- Questions:
  - Is a partial reversal proportional to the *awarded* amount (as tabled) or to the *calculated* amount capped at what was awarded? These differ whenever the cap bit.
  - Is the reversal rounded half-up per refund, and can a series of partial refunds reverse more (or less) in total than was awarded?

## Rule 10 — Must attribute a reversal to the month in which the cashback was earned

- Example: The one where a January purchase is refunded in January, $2.00 is reversed, and January's cap headroom increases by $2.00 so later January purchases can earn again.
- Counter-example: The one where a January purchase is refunded in March after January's cashback has been paid out — the reversal is deducted from March, and January's closed cap is not reopened.
- Questions:
  - Is a closed month ever restated, or is the clawback always applied to the current month?
  - Can the current month's cashback balance go negative, and if so is the debt carried forward or written off?
  - Does a clawback in the current month consume cap headroom (reducing what the member can still earn), or sit outside the cap?
  - What if the member has already redeemed or withdrawn the cashback being clawed back?

## Rule 11 — Should make earned and reversed cashback traceable to the transaction that caused it

- Example: The one where a member's statement shows each accrual and each reversal against its originating purchase, plus the month-to-date total against the $50 cap.
- Counter-example: The one where a purchase calculates $0.00 cashback (Rule 1) and no ledger entry is shown, only the purchase itself.
- Questions:
  - Is cashback credited immediately per transaction, or accumulated and paid at month end / statement close?
  - How long must we retain the accrual history for a refund arriving many months later?
