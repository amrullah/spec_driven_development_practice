# Basic Cashback Calculation — Example Map

**Story**
As a Customer I want to earn cashback on my purchases, so that I am rewarded for shopping with partner merchants.

**Shared constraints**
- All amounts are in USD, in whole cents.
- Cashback is always rounded **down** to the cent, never up.
- A merchant's rate is a percentage above 0% and up to 100%, with at most 2 decimal places (Rule 4).
- Only registered customers can earn cashback (Rule 5).
- Every purchase carries a unique purchase id supplied by the caller (Rule 9).

---

## Rule 1 — Must calculate cashback as purchase amount × the merchant's rate, rounded down to the cent

| Merchant rate | Purchase amount | Cashback | Note |
|---|---|---|---|
| 5% | $100.00 | $5.00 | normal case |
| 1.25% | $100.00 | $1.25 | fractional rate |
| 2% | $49.99 | $0.99 | 0.9998 rounds down, never up |
| 100% | $20.00 | $20.00 | highest allowed rate |

- Counter-example: The one where a $5.00 purchase at a 0.01% merchant calculates $0.0005. It qualifies, but it rounds down to $0.00 and is recorded as earning nothing.

## Rule 2 — Must not earn cashback on purchases below $5.00

| Purchase amount | Merchant rate | Cashback |
|---|---|---|
| $5.00 | 10% | $0.50 (minimum qualifying purchase) |
| $4.99 | 10% | $0.00 (accepted and recorded, earns nothing) |

- Counter-example: The one where a customer makes two $4.99 purchases at the same merchant and earns nothing on either. The minimum applies per purchase and never to a total.

## Rule 3 — Must apply the rate configured for the merchant when the purchase is recorded

- Example: The one where the same $100.00 earns $5.00 at a 5% merchant and $2.00 at a 2% merchant.
- Example: The one where a merchant's rate changes from 2% to 4%. Purchases recorded before the change keep the cashback they earned at 2%, and purchases after it earn 4%.
- Counter-example: The one where a purchase is at a merchant with no configured rate. It's accepted and recorded with $0.00 cashback, and the balance is unchanged.

## Rule 4 — Must only allow merchant rates above 0% and up to 100%, with at most 2 decimal places

| Rate configured | Accepted? |
|---|---|
| 0.01% | Yes (lowest) |
| 1.25% | Yes |
| 100% | Yes (highest) |
| 0% | No (400) |
| 100.01% | No (400) |
| 1.255% | No (400, too many decimals) |

- Configuring a rate for a new merchant creates it (201). Configuring a rate for an existing merchant updates it (200).
- Counter-example: The one where an invalid rate change is rejected and the merchant's existing rate stays in force.

## Rule 5 — Must only accept purchases from registered customers

- Example: The one where a registered customer makes their first purchase. Their balance starts at $0.00 and is credited with the cashback earned.
- Counter-example: The one where an unregistered customer makes a purchase. It's rejected (404) and nothing is recorded, so the customer must register first.

## Rule 6 — Must register a customer only once, starting with a $0.00 balance

- Example: The one where a customer registers (201), and their balance reads $0.00 before any purchase.
- Counter-example: The one where an already-registered customer id registers again. It's rejected (409 Conflict) and the existing balance is untouched.

## Rule 7 — Must credit cashback to the customer's rewards balance, and make the new balance visible

- Example: The one where a customer with a $10.00 balance earns $5.00. The purchase response shows $5.00 earned and a new balance of $15.00, and a balance query (200) also returns $15.00.
- Counter-example: The one where a purchase earns $0.00 (below the minimum, rounded to nothing, or at an unknown merchant). It's still recorded, and the balance is unchanged.

## Rule 8 — Must only accept purchase amounts greater than zero, in whole cents, with no upper limit

| Purchase amount | Accepted? |
|---|---|
| $0.01 | Yes (smallest; earns $0.00 under Rule 2) |
| $1,000,000.00 | Yes (no maximum) |
| $0.00 | No (400) |
| −$5.00 | No (400) |
| $10.005 | No (400, more than 2 decimals) |

## Rule 9 — Must not credit the same purchase twice

- The first submission of a purchase id is recorded (201). Any later submission of the same id credits nothing and returns the original result (200), meaning the cashback earned and the balance as they were when it was first recorded.
- Example: The one where a purchase is resubmitted after a network retry. The balance goes up only once.
- Example: The one where a recorded purchase id is resubmitted with a different amount or merchant. The original result is returned, the new details are ignored, and nothing is credited.
- Counter-example: The one where a customer genuinely makes two identical $20.00 purchases at the same merchant under different purchase ids, and both earn cashback.
