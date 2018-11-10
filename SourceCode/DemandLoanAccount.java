/*Assignment #: 01
Course: EECS2011 E
Professor: Jia Xu
Name : Akalpit Sharma */
public class DemandLoanAccount implements Account {

	private Customer customer;
	private double loanAmount = 0;

	@Override
	public boolean withdrawAmount(double amount) {
		AccountActivity.addEntry(this, "Error. This account does not has any withdrawal features.");
		return false;
	}

	@Override
	public void depositAmount(double amount) {

		/*
		 * Make sure that amount is positive number
		 */

		try {
			assert (amount > 0) : amount;
		} catch (AssertionError e) {
			AccountActivity.addEntry(this, "Incorrect input. Deposit was declined.");
			return;
		}

		this.loanAmount += amount;
		AccountActivity.addEntry(this, "Deposit for " + amount + " was successful.");
	}

	@Override
	public double cancelAccount() {
		AccountActivity.addEntry(this, "Error. This account does not has any canceling features.");
		return -1;
	}

	@Override
	public void suspendAccount() {
		AccountActivity.addEntry(this, "Error. This account does not has any suspending features.");
		return;
	}

	@Override
	public void reactivateAccount() {
		AccountActivity.addEntry(this, "Error. This account does not has any reactivating features.");
		return;
	}

	@Override
	public double getBalance() {
		return this.loanAmount;
	}

	@Override
	public double terminateAccount() {
		AccountActivity.addEntry(this, "Error. This account does not has any terminating features.");
		return 0;
	}

	@Override
	public void setOverdraftOption(OverdraftOption option) {
		AccountActivity.addEntry(this, "Error. This account does not has any overdraft features.");
		return;
	}

	@Override
	public void setLimit(double limit) {
		AccountActivity.addEntry(this, "Error. This account does not has any limit features.");
		return;
	}

	@Override
	public void transferAmount(double amount, Account account) {
		AccountActivity.addEntry(this, "Error. This account does not has any transfer features.");
		return;
	}

	@Override
	public int getSocialInsuranceNumber() {
		return customer.getSocialInsuranceNumber();
	}

	@Override
	public Account createAccount(Customer customer) {
		this.customer = customer;
		return this;
	}

	@Override
	public OverdraftOption getOverdraftOption() {
		AccountActivity.addEntry(this, "Error. This account does not has any overdraft features.");
		return null;
	}

	@Override
	public void charge(double amount) {
		this.loanAmount -= Math.abs(amount);
		AccountActivity.addEntry(this, "Account was charged for " + amount);
	}

}
