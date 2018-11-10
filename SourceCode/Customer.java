
/*Assignment #: 01
Course: EECS2011 E
Professor: Jia Xu
Name : Akalpit Sharma */
import java.util.Random;

/*
 * Customer class that has SIN and bank accounts
 */
public class Customer {
	private int socialInsuranceNumber;
	private CheckingAccount checkingAccount;
	private CreditAccount creditAccount;
	private DemandLoanAccount demandLoanAccount;

	/*
	 * Default constructor that generate random SIN
	 */

	public Customer() {
		generateRandomSecurityNumber();
		this.checkingAccount = null;
		this.creditAccount = null;
		this.demandLoanAccount = null;
	}

	/*
	 * Create Checking account
	 */

	public void createCheckingAccount() {
		try {
			assert (checkingAccount == null); // Make sure that customer doesn't has any.
		} catch (AssertionError e) {
			System.err.println("Error. Customer already has checking account.");
			return;
		}
		this.checkingAccount = (CheckingAccount) new CheckingAccount().createAccount(this); // Create and assign
	}

	/*
	 * Create Credit account
	 */

	public void createCreditAccount() {
		try {
			assert (creditAccount == null); // Make sure that customer doesn't has any.
		} catch (AssertionError e) {
			System.err.println("Error. Customer already has credit account.");
			return;
		}
		this.creditAccount = (CreditAccount) new CreditAccount().createAccount(this); // Create and assign
	}

	/*
	 * Method that cancel checking account
	 */

	public void cancelCheckingAccount() {
		checkingAccount.cancelAccount();
		terminateCheckingAccount();
	}

	/*
	 * Method that cancel credit account
	 */

	public void cancelCreditAccount() {
		creditAccount.cancelAccount();
		terminateCreditAccount();
	}

	/*
	 * Method that terminates checking account
	 */

	public void terminateCheckingAccount() {
		double leftOverBalance = checkingAccount.terminateAccount(); // get balance
		this.checkingAccount = null; // destroy checking account
		if (leftOverBalance < 0) { // If customer has negative balance
			/*
			 * Create new demand loan account or add to existing this balance
			 */
			if (this.demandLoanAccount == null) {
				this.demandLoanAccount = (DemandLoanAccount) new DemandLoanAccount().createAccount(this);
				this.demandLoanAccount.charge(leftOverBalance);
			} else {
				this.demandLoanAccount.charge(leftOverBalance);
			}
		}
	}

	/*
	 * Method that terminates credit account
	 */

	public void terminateCreditAccount() {
		double leftOverBalance = creditAccount.terminateAccount(); // get balance
		this.creditAccount = null; // destroy credit account
		/*
		 * Create new demand loan account or add to existing this balance
		 */
		if (leftOverBalance < 0) {
			if (this.demandLoanAccount != null) {
				this.demandLoanAccount = (DemandLoanAccount) new DemandLoanAccount().createAccount(this);
				this.demandLoanAccount.charge(leftOverBalance);
			} else {
				this.demandLoanAccount.charge(leftOverBalance);
			}
		}
	}

	/*
	 * Return CheckingAccount if it exists
	 */

	public CheckingAccount getCheckingAccount() throws NullPointerException {
		try {
			assert (checkingAccount != null);
		} catch (AssertionError e) {
			throw new NullPointerException("Error. Customer does not has such account.");
		}
		return checkingAccount;
	}

	/*
	 * Return CreditAccount if it exists
	 */

	public CreditAccount getCreditAccount() throws NullPointerException {
		try {
			assert (creditAccount != null);
		} catch (AssertionError e) {
			throw new NullPointerException("Error. Customer does not has such account.");
		}
		return creditAccount;
	}

	/*
	 * Return DemandLoanAccount if it exists
	 */

	public DemandLoanAccount getDemandLoanAccount() throws NullPointerException {
		try {
			assert (demandLoanAccount != null);
		} catch (AssertionError e) {
			throw new NullPointerException("Error. Customer does not has such account.");
		}
		return demandLoanAccount;
	}

	public int getSocialInsuranceNumber() {
		return socialInsuranceNumber;
	}

	/*
	 * Method that generates random security number
	 */

	private void generateRandomSecurityNumber() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			sb.append(new Random().nextInt(10));
		}
		this.socialInsuranceNumber = Integer.valueOf(sb.toString());
	}
}
