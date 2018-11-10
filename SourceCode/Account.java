/*Assignment #: 01
Course: EECS2011 E
Professor: Jia Xu
Name : Akalpit Sharma */
 
 /*
 * Interface that unites all three accounts
 */
public interface Account {
	
	/*
	 * Some constants from CIBC_Addendum
	 */

	final double CREDIT_EXCEED_LIMIT_CHARGE = 29.0;
	final double NON_SUFFICIENT_FUNDS_CHARGE = 45.0;
	final double PAY_PER_USE_FEE = 5.00;
	final double MONTHLY_FIXED_FEE = 4.00;

	boolean withdrawAmount(double amount);

	void depositAmount(double amount);

	double cancelAccount();

	void suspendAccount();

	void reactivateAccount();

	double getBalance();

	double terminateAccount();

	void setOverdraftOption(OverdraftOption option);

	void setLimit(double limit);

	void transferAmount(double amount, Account account);

	int getSocialInsuranceNumber();

	Account createAccount(Customer customer);

	OverdraftOption getOverdraftOption();

	void charge(double amount);
}
