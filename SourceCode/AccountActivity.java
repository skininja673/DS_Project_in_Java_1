/*Assignment #: 01
Course: EECS2011 E
Professor: Jia Xu
Name : Akalpit Sharma */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/*
 * Class that holds all account activities
 */
public class AccountActivity {
	/*
	 * List that holds all entries
	 */
	
	private static List<LogEntry> logEntries = new ArrayList<>();

	/*
	 * Method that adds new entry with current date, message and account info
	 */

	public static void addEntry(Account account, String message) {
		LogEntry logEntry = new LogEntry(Date.from(Instant.now()), account.getSocialInsuranceNumber(),
				account.getClass().getName() + " : " + message);
		logEntries.add(logEntry);
	}

	/*
	 * Sorts entries in the list. First by SIN, then by date.
	 */

	public static void sortAccountLog() {
		Collections.sort(logEntries);
	}

	/*
	 * Method that process records in the accountLog at the end of each business day
	 * according to the rules in the document
	 */

	public static void processAccountLogEndOfDay(Account account) {
		if (account instanceof CheckingAccount) { // If account is checking type
			if (account.getOverdraftOption().equals(OverdraftOption.PAY_PER_USE)) { // If overdraft is payperuse
				if (account.getBalance() < 0) { // If customer has negative balance
					account.charge(Account.PAY_PER_USE_FEE); // Charge account with pay per use fee
					addEntry(account, "End of the day. Pay per use fee charge for " + Account.PAY_PER_USE_FEE);
				}
			}
		}
	}

	/*
	 * Method that process records in the accountLog at the end of each calendar
	 * month according to the rules in the document
	 */

	public static void processAccountLogEndOfMonth(Account account) {
		if (account instanceof CheckingAccount) { // If account is checking type
			if (account.getOverdraftOption().equals(OverdraftOption.MONTHLY_FIXED_FEE)) { // If monthly fixed
				account.charge(Account.MONTHLY_FIXED_FEE); // Charge this account
				addEntry(account, "End of the month. Monthly fixed fee charge for " + Account.MONTHLY_FIXED_FEE);
				if (account.getBalance() < 0) { // If customer has negative balance
					double currentDebt = account.getBalance(); // Get balance
					double interest = currentDebt * 21 / (12 * 100); // Calculate interest
					account.charge(interest); // Charge this account
					addEntry(account, "End of the month. Interest charge for " + interest);
				}
			}
		}
		/*
		 * If account either credit or demand
		 */
		if (account instanceof CreditAccount || account instanceof DemandLoanAccount) {
			if (account.getBalance() < 0) { // On negative balance
				double currentDebt = account.getBalance(); // Get balance
				double interest = currentDebt * 21 / (12 * 100); // Calculate interest
				account.charge(interest); // Charge this account
				addEntry(account, "End of the month. Interest charge for " + interest);
			}
		}
	}

	/*
	 * Method that saves log to the log.txt file
	 */

	public static void saveAccountLog() {
		StringBuilder sb = new StringBuilder(); // Initializing StringBuilder
		for (int i = 0; i < logEntries.size(); i++) { // Iterate over list
			LogEntry logEntry = logEntries.get(i); // Get current entry
			if (i == logEntries.size() - 1) { // If it the last one
				sb.append(logEntry.toString()); // Add to builder without new line
			} else {
				sb.append(logEntry.toString()).append("\n"); // Add it to build and make new line
			}
		}
		try {
			PrintWriter pw = new PrintWriter(new File("log.txt")); // Initializing PrintWriter
			pw.println(sb.toString()); // save stringbuilder to file
			pw.close(); // close stream
			System.out.println("Log saved.");
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
		}
	}

	/*
	 * Method that reads log.txt and fill List with entries
	 */
	public static void retrieveAccountLog() {
		List<LogEntry> logEntries = new ArrayList<>(); // Initializing Arraylist
		try {
			Scanner scn = new Scanner(new File("log.txt")); // Opening scanner
			while (scn.hasNextLine()) { // While it has line
				String line = scn.nextLine(); // Read line
				LogEntry logEntry = new LogEntry(line); // Create new log entry from this line
				logEntries.add(logEntry); // Add it to the list
			}
			scn.close(); // Close scanner
			System.out.println("Log loaded.");
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
		}
		AccountActivity.logEntries = logEntries; // Assignn current list to loaded
	}
}

/*
 * Inner class that helps to store log data
 */
class LogEntry implements Comparable<LogEntry> {

	private Date date;
	private Integer socialInsuranceNumber;
	private String message;

	/*
	 * Default constructor from fields
	 */

	public LogEntry(Date date, int socialInsuranceNumber, String message) {
		this.date = date;
		this.socialInsuranceNumber = socialInsuranceNumber;
		this.message = message;
	}

	/*
	 * Constructor that takes one single line from log.txt
	 */

	public LogEntry(String line) {
		try {
			String[] data = line.split(">"); // Split string into array by ">" sign
			this.date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US).parse(data[0],
					new ParsePosition(0)); // Parse String as Date
			this.socialInsuranceNumber = Integer.valueOf(data[1]); // Parse String as Integer
			this.message = data[2]; // Assign String to message
		} catch (Exception e) {
			System.out.println("Loading failed.");
		}
	}

	public int getSocialInsuranceNumber() {
		return socialInsuranceNumber;
	}

	public Date getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US); // Create simple format date
		String time = sdf.format(date); // Format our date
		return time + ">" + socialInsuranceNumber + ">" + message; // Make string in such format
	}

	/*
	 * Comparator logic in order to sort entries
	 */
	@Override
	public int compareTo(LogEntry le) {
		int compareResult = socialInsuranceNumber.compareTo(le.getSocialInsuranceNumber()); // First compare by SIN
		if (compareResult != 0) {
			return compareResult;
		}
		/*
		 * If they're equal, compare by date
		 */
		return date.compareTo(le.getDate());
	}
}
