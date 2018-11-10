/*Assignment #: 01
Course: EECS2011 E
Professor: Jia Xu
Name : Akalpit Sharma */
public class CreditAccount implements Account {

	private Customer customer;
	private double balance;
	private double creditLimit;
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
			AccountActivity.addEntry(this, "Error. This account is not available.");
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
		 * If amount is available, process
		 */

		if (this.balance + creditLimit >= amount) {
			this.balance -= amount;
			AccountActivity.addEntry(this, "Withdrawal for " + amount + " was successful.");
			return true;
		} else {
			/*
			 * Or decline withdrawal and charge account with fee
			 */
			AccountActivity.addEntry(this, "Withdrawal was declined.");
			this.balance -= Account.CREDIT_EXCEED_LIMIT_CHARGE;
			AccountActivity.addEntry(this, "Credit Exceed Limit penalty has been charged.");
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

		balance += amount;
		AccountActivity.addEntry(this, "Deposit for " + amount + " was successful.");
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
		AccountActivity.addEntry(this, "Error. This account does not has any overdraft features.");
		return;
	}

	@Override
	public void setLimit(double limit) {
		/*
		 * Check if limit is positive number
		 */
		try {
			assert (limit >= 0);
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Error. Incorrect input for credit limit.");
			return;
		}
		/*
		 * Set new limit
		 */
		this.creditLimit = limit;
		AccountActivity.addEntry(this, "Credit limit was set to " + limit);
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

	@Override
	public int getSocialInsuranceNumber() {
		return customer.getSocialInsuranceNumber();
	}

	@Override
	public Account createAccount(Customer customer) {
		/*
		 * Create new account, set balance to zero, limit to default 100
		 */
		this.balance = 0;
		this.creditLimit = 100;
		this.isCreated = true;
		this.customer = customer;
		AccountActivity.addEntry(this, "New account has been created.");
		return this;
	}

	@Override
	public OverdraftOption getOverdraftOption() {
		AccountActivity.addEntry(this, "Error. This account does not has any overdraft features.");
		return null;
	}

	/*
	 * Method that charges current account for specified amount
	 */

	@Override
	public void charge(double amount) {
		this.balance -= amount;
		AccountActivity.addEntry(this, "Account was charged for " + amount);
	}

}
