/*Assignment #: 01
Course: EECS2011 E
Professor: Jia Xu
Name : Akalpit Sharma */
public class CheckingAccount implements Account {

	private OverdraftOption overdraftOption;
	private Customer customer;
	private double balance;
	private double overdraftLimit;
	private boolean isCreated;
	private boolean isSuspended;

	@Override
	public boolean withdrawAmount(double amount) {

		/*
		 * Make sure account created and not suspended
		 */

		try {
			assert (isCreated && !isSuspended);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. This account is not available. Withdraw was declined.");
			return false;
		}

		/*
		 * Make sure that amount is positive number
		 */

		try {
			assert (amount > 0) : amount;
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Incorrect input. Withdrawal was declined.");
			return false;
		}

		/*
		 * Process request according to overdraft option
		 */
		switch (overdraftOption) {
		case NO_OVERDRAFT_PROTECTION:
			if (this.balance >= amount) {
				/*
				 * If there was no overdraft, process request without charge
				 */
				this.balance -= amount;
				AccountActivity.addEntry(this, "Withdrawal for " + amount + " was successful.");
				return true;
			} else {
				/*
				 * If there was non sufficient funds, decline withdrawal and charge it
				 */
				AccountActivity.addEntry(this, "Withdrawal was declined.");
				this.balance -= Account.NON_SUFFICIENT_FUNDS_CHARGE;
				AccountActivity.addEntry(this, "Non-Sufficient Funds penalty has been charged.");
				return false;
			}
		case MONTHLY_FIXED_FEE:
			if (this.balance + overdraftLimit >= amount) {
				/*
				 * If such amount is available, process request
				 */
				this.balance -= amount;
				AccountActivity.addEntry(this, "Withdrawal for " + amount + " was successful.");
				return true;
			} else {
				/*
				 * If not, decline
				 */
				AccountActivity.addEntry(this, "Withdrawal was declined. Out of limit");
				return false;
			}
		case PAY_PER_USE:
			if (this.balance + overdraftLimit >= amount) {
				/*
				 * If such amount is available, process request
				 */
				this.balance -= amount;
				AccountActivity.addEntry(this, "Withdrawal for " + amount + " was successful.");
				return true;
			} else {
				/*
				 * If not, decline
				 */
				AccountActivity.addEntry(this, "Withdrawal was declined. Out of limit");
				return false;
			}
		default:
			assert false : overdraftOption;
			return false;
		}
	}

	@Override
	public void depositAmount(double amount) {

		/*
		 * Make sure account created and not suspended
		 */

		try {
			assert (isCreated && !isSuspended);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. This account is not available. Deposit was declined.");
			return;
		}

		/*
		 * Make sure that amount is positive number
		 */

		try {
			assert (amount > 0) : amount;
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Incorrect input. Deposit was declined.");
			return;
		}

		/*
		 * Process request
		 */

		this.balance += amount;
		AccountActivity.addEntry(this, "Deposit for " + amount + " was successful.");
	}

	@Override
	public Account createAccount(Customer customer) {
		/*
		 * Create new account, set balance to zero, default overdraft operation to none,
		 * limit to min.
		 */
		this.balance = 0;
		this.overdraftOption = OverdraftOption.NO_OVERDRAFT_PROTECTION;
		this.overdraftLimit = 100;
		this.isCreated = true;
		this.customer = customer;
		AccountActivity.addEntry(this, "New account has been created.");
		return this;
	}

	@Override
	public double cancelAccount() {
		/*
		 * Make sure it is created and not suspended
		 */
		try {
			assert (isCreated && !isSuspended);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. This account is not available to cancel.");
			return -1;
		}
		/*
		 * Process Log to the end of month
		 */
		AccountActivity.processAccountLogEndOfMonth(this);
		/*
		 * Terminate
		 */
		return terminateAccount();
	}

	@Override
	public void suspendAccount() {
		/*
		 * Make sure it is not suspended
		 */
		try {
			assert (!isSuspended);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Unable to suspend account. Account is not active");
			return;
		}
		/*
		 * Suspend it
		 */
		this.isSuspended = true;
		AccountActivity.addEntry(this, "This account has been suspended.");
	}

	@Override
	public void reactivateAccount() {
		/*
		 * Make sure it is supended
		 */
		try {
			assert (isSuspended);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Unable to reactivate. Account is active already");
			return;
		}
		/*
		 * Reactivate it
		 */
		this.isSuspended = false;
		AccountActivity.addEntry(this, "This account has been reactivated.");
	}

	@Override
	public double getBalance() {
		/*
		 * Get balance if it's not suspeneded
		 */
		try {
			assert (!isSuspended);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Unable to get balance. Account is not active");
			return -1;
		}
		return this.balance;
	}

	@Override
	public double terminateAccount() {
		/*
		 * Terminate account if it is active
		 */
		try {
			assert (!isSuspended);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Unable to terminate. Account is not active");
			return 0;
		}
		this.isCreated = false;
		AccountActivity.addEntry(this, "Account has been terminated.");
		return balance;
	}

	@Override
	public void setOverdraftOption(OverdraftOption option) {
		/*
		 * Set new overdraft option. Check if it is not the same
		 */
		try {
			assert (this.overdraftOption != option);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. You already have this overdraft protection.");
			return;
		}
		this.overdraftOption = option;
		AccountActivity.addEntry(this, "Overdraft option has been changed to " + overdraftOption);
	}

	@Override
	public void setLimit(double limit) {
		/*
		 * Check if it has overdraft option
		 */
		try {
			assert (this.overdraftOption != OverdraftOption.NO_OVERDRAFT_PROTECTION);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. You need to have overdraft protection to set limit.");
			return;
		}
		/*
		 * Check if limit is in bounds
		 */
		try {
			assert (limit >= 100 & limit <= 5000);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. Limit out of bounds. Minimum - 100$, Maximum - 5000$.");
			return;
		}
		/*
		 * Set new limit
		 */
		this.overdraftLimit = limit;
		AccountActivity.addEntry(this, "Limit was set to " + limit);
	}

	@Override
	public void transferAmount(double amount, Account account) {
		/*
		 * First, withdraw from this account, then deposit to the specified
		 */
		try {
			withdrawAmount(amount);
			account.depositAmount(amount);
			AccountActivity.addEntry(this, "Transfer success.");
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. Transfer was declined.");
			return;
		}
	}

	/*
	 * Method that charges current account for specified amount
	 */

	@Override
	public void charge(double amount) {
		this.balance -= Math.abs(amount);
		AccountActivity.addEntry(this, "Account was charged for " + amount);
	}

	@Override
	public int getSocialInsuranceNumber() {
		return customer.getSocialInsuranceNumber();
	}

	@Override
	public OverdraftOption getOverdraftOption() {
		return overdraftOption;
	}

}
