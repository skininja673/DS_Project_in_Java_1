/*Assignment #: 01
Course: EECS2011 E
Professor: Jia Xu
Name : Akalpit Sharma */
public class Test {
	public static void main(String[] args) throws InterruptedException {
		Customer customer = new Customer();
		customer.createCheckingAccount();
		CheckingAccount checkingAccount = customer.getCheckingAccount();
		checkingAccount.withdrawAmount(30);
		checkingAccount.setOverdraftOption(OverdraftOption.PAY_PER_USE);
		AccountActivity.processAccountLogEndOfMonth(checkingAccount);
		AccountActivity.processAccountLogEndOfDay(checkingAccount);
		checkingAccount.setOverdraftOption(OverdraftOption.NO_OVERDRAFT_PROTECTION);
		System.out.println("Current balance " + checkingAccount.getBalance()); // Expected -50
		checkingAccount.depositAmount(50);
		AccountActivity.processAccountLogEndOfMonth(checkingAccount);
		AccountActivity.processAccountLogEndOfDay(checkingAccount);
		System.out.println("Current balance " + checkingAccount.getBalance()); // Expected 0
		AccountActivity.processAccountLogEndOfMonth(checkingAccount);
		AccountActivity.processAccountLogEndOfDay(checkingAccount);
		System.out.println("Current balance " + checkingAccount.getBalance()); // Expected 0
		customer.createCheckingAccount();
		checkingAccount.setOverdraftOption(OverdraftOption.MONTHLY_FIXED_FEE);
		checkingAccount.withdrawAmount(200); // Expected out of limit
		checkingAccount.setLimit(1000);
		checkingAccount.withdrawAmount(200);
		System.out.println("Current balance " + checkingAccount.getBalance()); // Expected -200
		AccountActivity.processAccountLogEndOfMonth(checkingAccount);
		System.out.println("Current balance " + checkingAccount.getBalance()); // Expected -207.57
		AccountActivity.processAccountLogEndOfMonth(checkingAccount);
		System.out.println("Current balance " + checkingAccount.getBalance()); // Expected -215.27~
		customer.terminateCheckingAccount();
		DemandLoanAccount demandLoanAccount = customer.getDemandLoanAccount();
		try {
			System.out.println("Current balance " + demandLoanAccount.getBalance()); // Expected -215.27~
			customer.getCreditAccount();
		} catch (Exception e) {
			System.err.println("Customer doesn't have such account.");
		}
		Customer new_customer = new Customer();
		new_customer.createCreditAccount();
		CreditAccount creditAccount = new_customer.getCreditAccount();
		creditAccount.withdrawAmount(500);
		creditAccount.setLimit(1000);
		creditAccount.withdrawAmount(500);
		System.out.println("Current balance " + creditAccount.getBalance()); // Expected -529
		AccountActivity.sortAccountLog();
		AccountActivity.saveAccountLog();
	}

}
