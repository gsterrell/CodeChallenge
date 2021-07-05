//CPSI Code Challenge
//CodeChallenge.java
//Programmer: Gregory Terrell

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Employee {
	// Employee information fields. 
	private String name = "";
	private float rate = 0.0f;
	private float hours = 0.0f;
	private String role = "";
	private float salary = 0;

	//Employee constructor. 
	public Employee(String empName, float empRate, float empHours, String empRole) {
		name = empName;
		rate = empRate;
		hours = empHours;
		role = empRole;
		CalculateSalary();
	}

	//Print function for displaying employee information
	public void PrintFields() {
		System.out.printf("%-32s | $%-5.2f | %-5.2f | %-9s | $%-,12.2f\n", name, rate, hours, role, salary);
	}

	//Return salary. Not strictly necessary currently,
	//but can be augmented later for future use.
	public float ReturnSalary() {
		return salary;
	}

	// Calculate the annual salary (hours * rate * weeks/year, plus other
	// considerations)
	private void CalculateSalary() {
		//Calculate initial salary (rate *hours * weeks/year)
		salary = rate * hours * 52;
		//If full time employee, cap salary at $50k
		if (role.contains("FULL TIME") && salary > 50000.00f)
				salary = 50000.00f;
		//If contract worker, set new salary (initial salary + $10k)
		else if (role.contains("CONTRACT"))
			salary += 10000.00f;
	}

	public static void main(String[] args) {
		BufferedReader employeeFile = null;

		//If arguments were given at command line, try to open a file
		if (args.length > 0) {
			try {
				employeeFile = new BufferedReader(new FileReader(args[0]));
				ArrayList<Employee> employeeRecords = new ArrayList<Employee>();
				String row;
				String[] data = null;
				float salaryTotal = 0;
				while ((row = employeeFile.readLine()) != null) {
					data = row.split(",");
					Employee e = new Employee(data[0], Float.parseFloat(data[1]), Float.parseFloat(data[2]), data[3]);
					salaryTotal += e.ReturnSalary();
					employeeRecords.add(e);
				}

				try {
					System.out.printf("%-32s | %-6s | %-6s | %-9s | %-8s\n\n", "Name", "Rate", "Hours", "Role",
							"Salary");
					// If argument "a" is passed in command line, print out records in as-is order
					if (args[1].contains("-a")) {
						for (Employee x : employeeRecords) {
							x.PrintFields();
						}
						System.out.printf("\nTOTAL SALARY: %,60.2f", salaryTotal);
					}
					// If argument "b" is passed in command line, print out records by role type
					else if (args[1].contains("-b")) {
						//Use Collections.sort method to re-order list in role order
						Collections.sort(employeeRecords, new Comparator<Employee>() {
							public int compare(Employee e1, Employee e2) {
								return e1.role.compareTo(e2.role);
							}
						});
						//variables to hold role salary totals
						float contractTotal = 0;
						float fulltimeTotal = 0;
						float parttimeTotal = 0;

						//For each record, print the information and add salary to appropriate total
						for (Employee x : employeeRecords) {
							if (x.role.contains("FULL TIME"))
								fulltimeTotal += x.salary;
							else if (x.role.contains("CONTRACT"))
								contractTotal += x.salary;
							else if (x.role.contains("PART TIME"))
								parttimeTotal += x.ReturnSalary();
							x.PrintFields();
						}
						System.out.printf(
								"\nPART TIME SALARY TOTAL: %,50.2f\n" + "FULL TIME SALARY TOTAL: %,50.2f\n"
										+ "CONTRACT SALARY TOTAL: %,51.2f",
								parttimeTotal, fulltimeTotal, contractTotal);
					}
				} catch (IndexOutOfBoundsException e) { //Catch if no calculation method was given
					System.err.println("Caught IndexOutOfBoundsException: " + e.getMessage()
							+ "\nNo calculation method chosen.");
				} finally {	
					//Close file after use
					employeeFile.close();
				}	
			} catch (IOException e) {
				System.err.println("Caught IOException: " + e.getMessage()
				+ "\nFile could not be found or file could not be opened.");
			}
		} else
			System.out.println("Input file not given. Please try again.\n"
					+ "Format: java -jar CodeChallenge.jar -flag" 
					+ "\nFlag options: \n-a: records listed as entered, salary total in dollars displayed."
					+ "\n-b: records sorted by role, salary totals for each role displayed"
					);
	}
}