# View Account History — Example Map

**Story**
As a customer, I want to view my cashback transaction history so that I can know where I earn the most cashbacks.

**Shared constraints (inherited from the Cashback spec)**
- Cashback events are account-level, not cardholder-level (Cashback Rule 8).
- Accrual and reversal history is retained for 24 months (Cashback Rule 12).
- Accruals are *pending* from settlement and become *paid* at calendar month end.
- All amounts in USD.

---

## Rule 1 — Should show only events that changed the cashback balance, each traceable to its originating transaction

| Event | Appears in history? | Shown against |
|---|---|---|
| Accrual on a settled qualifying purchase | Yes | the originating purchase |
| Reversal from a refund | Yes | the originating purchase |
| Clawback posted to a later month | Yes | the original purchase, dated by its posting (Rule 6) |
| Month-end payout | Yes | the month, not a single purchase |
| Redemption by the member | Yes | the redemption itself |
| Negative balance charged to statement after 90 days | Yes | the month it was charged |
| Purchase that earned nothing (below minimum, delinquent, foreign currency, excluded category) | No | — |
| Fee, cash advance, balance transfer, gift-card top-up | No | — |
| Authorisation not yet settled, or expired unsettled | No | — nothing accrued |

- History is a record of cashback, not a statement: a transaction that earned nothing leaves no trace, whatever the reason.
- Counter-example: The one where a purchase is authorised on the 30th and settles on the 2nd — it appears in history on the 2nd, dated by settlement, even though it counts against the previous month's cap.

## Rule 2 — Must show each accrual's status so pending, paid and frozen amounts are never confused

- Example: The one where a member views history mid-month and sees the month's accruals as *pending*, and the previous month's as *paid*.
- Example: The one where an account is suspended and its earned-but-unpaid cashback shows as *frozen* rather than pending (Cashback Rule 7).
- Counter-example: The one where a purchase settles on the last day of the month and flips from pending to paid within the same viewing session — status is derived at read time, not fixed when the entry was written.

## Rule 3 — Must show the amount actually awarded, and note where the monthly cap reduced it

- The forgone amount is never shown; the entry states only that the cap was reached.
- Example: The one where a $5,000 Premium purchase awarded $50.00 and the entry carries a *monthly cap reached* note.
- Example: The one where a purchase at the cap boundary awarded $1.00 of a calculated $10.00 — same note, partial award (Cashback Rule 8).
- Counter-example: The one where a purchase earned its full calculated amount and carries no note at all.

## Rule 4 — Should default to the current calendar month, and let the member select any period inside retention

- The default matches how the cap and payout work, so the opening view is the month in progress.
- Example: The one where a member opens history on 3 March and sees March's two settled accruals only, then widens the period to the last 12 months to compare merchants.
- Counter-example: The one where a member opens history on the 1st of a month, before anything has settled, and sees an empty list with the period's totals at zero — an empty month is a valid result, not an error.

## Rule 5 — Should rank merchants by net cashback earned over the selected period, so "where do I earn most" is answerable without arithmetic

- Grouping is by merchant only. Categories are not a grouping the member sees.
- Example: The one where a member's 12-month view ranks merchants by net cashback earned, highest first.
- Example: The one where reversals reduce a merchant's total — a merchant with $8.00 earned and $6.00 refunded ranks on $2.00, not $8.00.
- Counter-example: The one where a merchant's net total for the period is negative (refunds exceeded earnings) and it still appears, at the bottom, rather than being hidden.

## Rule 6 — Must attribute a clawback to the month it posts in, while naming the purchase it came from

- Example: The one where a January purchase is refunded in March: the deduction appears in March, showing the original January purchase and its date, and January's history is unchanged when revisited.
- Counter-example: The one where a January purchase is refunded in January — the reversal simply sits in January against its purchase, because that month has not closed.

## Rule 7 — Must show the whole account's history across all cardholders, attributing each entry to the cardholder who transacted

- Every cardholder sees the same shared history, because the balance and the $50 cap are shared.
- Example: The one where an additional cardholder's purchase appears in the primary holder's history, named to that cardholder — and the additional cardholder sees the primary holder's purchases in the same list.
- Counter-example: The one where a cardholder removed from the account mid-year still has their past entries shown, because those events shaped the account's balance.

## Rule 8 — Must show only events inside the 24-month retention window

| Event age | In history? |
|---|---|
| 1 month | Yes |
| 24 months | Yes |
| 24 months + 1 day | No |

- A period selection reaching past 24 months returns what is retained, not an error.

## Rule 9 — Must show period earned and redeemable balance above the list

- *Period earned* is the total for the selected period, net of reversals attributed to it.
- *Redeemable balance* is what can be redeemed right now: paid, not frozen, and above zero.
- Example: The one where a member has $12.00 paid from earlier months and $3.00 pending for the current month — the period earned reads $3.00 and the redeemable balance reads $12.00.
- Counter-example: The one where a clawback has driven the balance to −$5.00 and the redeemable balance shows $0.00 while the list still shows the negative running total — a member is never offered a negative amount to redeem (Cashback Rule 11).

## Rule 10 — Must present history newest first, with each event carrying the date that made it real

- An accrual is dated by settlement; a reversal by the refund; a payout by month end; a clawback by its posting date.
- Counter-example: The one where two events share a timestamp and are ordered by transaction id, so the sequence is stable between views.
