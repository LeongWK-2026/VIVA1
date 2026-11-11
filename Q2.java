package viva1;

import java.util.Scanner;
public class Q2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Get the outstanding balance with input validation (RM 500 - RM 50,000)
        double balance = 0;
        do {
            System.out.print("Enter outstanding balance (RM): ");
            balance = sc.nextDouble();
            if (balance < 500 || balance > 50000) {
                System.out.println("Error: Balance must be between RM 500 and RM 50000.");
            }
        } while (balance < 500 || balance > 50000);

        // Get the annual interest rate as a percentage
        System.out.print("Enter annual interest rate (%): ");
        double annualRate = sc.nextDouble();

        // Convert the annual rate into a monthly rate
        double monthlyRate = annualRate / (100 * 12);

        // Get the planned monthly payment
        System.out.print("Enter planned monthly payment (RM): ");
        double plannedPayment = sc.nextDouble();

        // Ask whether the user will take a payment holiday
        System.out.print("Take a payment holiday? (Y/N): ");
        char choice = sc.next().charAt(0);

        // Initialize holiday payment details with default values
        int holidayMonth = -1;
        if (choice == 'Y' || choice == 'y') {
            System.out.print("Enter holiday month (1-36): ");
            holidayMonth = sc.nextInt();
        }

        // Prepare variables and array for year-end balances
        double totalInterest = 0;
        double totalLateFees = 0;
        double totalAmountPaid = 0;
        int raisedCount = 0;
        boolean cleared = false;

        // Array to store balances at Month 12 (Year 1), Month 24 (Year 2), and Month 36 (Year 3)
        double[] yearBalances = new double[3]; // Index 0 = Year 1, Index 1 = Year 2, Index 2 = Year 3

        // Simulate the loan for up to 36 months
        for (int month = 1; month <= 36; month++) {
            // At the start of each month, add the monthly interest
            double interest = balance * monthlyRate;
            balance += interest;
            totalInterest += interest;

            // Check whether the payment holiday occurs this month
            if (month == holidayMonth) {
                balance += 25.00; // Add late fee directly to balance
                totalLateFees += 25.00;
                System.out.println("Month " + month + ": Payment holiday - late fee RM 25.00 charged.");
            } else {
                // Calculate minimum payment logic
                double minPayment = balance * 0.05;
                if (minPayment < 50.00) {
                    minPayment = 50.00;
                }

                // Determine actual payment amount to pay
                double paymentToMake = plannedPayment;
                if (plannedPayment < minPayment) {
                    paymentToMake = minPayment;
                    raisedCount++;
                }

                // Never pay more than the balance owed
                if (paymentToMake > balance) {
                    paymentToMake = balance;
                }

                // Make payment
                balance -= paymentToMake;
                totalAmountPaid += paymentToMake;
            }

            // Record balance at Month 12, Month 24, and Month 36 into the array
            if (month == 12) {
                yearBalances[0] = balance;
            } else if (month == 24) {
                yearBalances[1] = balance;
            } else if (month == 36) {
                yearBalances[2] = balance;
            }

            // Check if the balance drops below RM 0.005 (cleared)
            if (balance < 0.005) {
                balance = 0.0;
                cleared = true;
                System.out.println("Debt fully cleared in Month " + month + "!");
                break;
            }
        }

        // Display the results using the array
        System.out.printf("End of Year 1 Balance: RM %.2f%n", yearBalances[0]);
        System.out.printf("End of Year 2 Balance: RM %.2f%n", yearBalances[1]);
        System.out.printf("End of Year 3 Balance: RM %.2f%n", yearBalances[2]);
        System.out.printf("Total Interest Charged: RM %.2f%n", totalInterest);
        System.out.printf("Total Late Fees: RM %.2f%n", totalLateFees);
        System.out.printf("Total Amount Paid: RM %.2f%n", totalAmountPaid);
        System.out.println("Months Raised to Minimum Payment: " + raisedCount);

        if (!cleared) {
            System.out.println("Debt not cleared after 36 months.");
        }
    }
}
