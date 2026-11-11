package viva1;

import java.util.Scanner;
public class Q5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int X = 0, Y = 0, B = 0;

        // Input Validation
        while (true) {
            System.out.print("Enter width X: ");
            X = sc.nextInt();
            System.out.print("Enter height Y: ");
            Y = sc.nextInt();
            System.out.print("Enter block size B: ");
            B = sc.nextInt();

            boolean validX = (X >= 7 && X <= 29 && X % 2 != 0);
            boolean validY = (Y >= 7 && Y <= 29 && Y % 2 != 0);
            boolean validB = (B >= 1 && B <= 3);

            if (validX && validY && validB) {
                break;
            }

            System.out.println("Error: X and Y must be odd numbers between 7 and 29, and B must be between 1 and 3.");
        }

        // Positions Calculation
        int centreRow = Y / 2;
        int centreCol = X / 2;

        // Draw Grid using Strict Priority Rules
        for (int r = 0; r < Y; r++) {
            int diag1 = (int) Math.round((double) r * (X - 1) / (Y - 1));
            int diag2 = (X - 1) - diag1;

            for (int c = 0; c < X; c++) {
                // Priority 1: Frame corner
                if ((r == 0 || r == Y - 1) && (c == 0 || c == X - 1)) {
                    System.out.print("+");
                }
                // Priority 2: First or last row
                else if (r == 0 || r == Y - 1) {
                    System.out.print("=");
                }
                // Priority 3: First or last column
                else if (c == 0 || c == X - 1) {
                    System.out.print("I");
                }
                // Priority 4: Centre cell
                else if (r == centreRow && c == centreCol) {
                    System.out.print("@");
                }
                // Priority 5: Diagonals
                else if (c == diag1 || c == diag2) {
                    System.out.print("X");
                }
                // Priority 6: Centre row
                else if (r == centreRow) {
                    System.out.print("-");
                }
                // Priority 7: Centre column
                else if (c == centreCol) {
                    System.out.print("|");
                }
                // Priority 8: Checkerboard pattern
                else if ((r / B + c / B) % 2 == 0) {
                    System.out.print(".");
                }
                // Priority 9: Otherwise
                else {
                    System.out.print("o");
                }
            }
            System.out.println();
        }
    }
}