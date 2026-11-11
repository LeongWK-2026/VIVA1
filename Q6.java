package viva1;
import java.util.Scanner;

public class Q6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read the robot command string.
        System.out.print("Enter robot commands: ");
        String commands = sc.nextLine();

        // Validate the input string.
        for (int i = 0; i < commands.length(); i++) {
            char c = commands.charAt(i);
            if (c != 'F' && c != 'B' && c != 'L' && c != 'R' &&
                c != 'f' && c != 'b' && c != 'l' && c != 'r') {
                System.out.println("Invalid Command String");
                sc.close();
                return;
            }
        }

        // Initialize coordinates and headings for both robots.
        // Headings: 0 = North (+Y), 1 = East (+X), 2 = South (-Y), 3 = West (-X)
        char[] dirNames = {'N', 'E', 'S', 'W'};
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        int xP = 0, yP = 0, dirP = 0; // Robot P (uppercase)
        int xQ = 0, yQ = 0, dirQ = 0; // Robot Q (lowercase)

        int meetings = 0;
        int originReturns = 0;
        int maxGap = 0;

        // Process movement simulation loop.
        for (int i = 0; i < commands.length(); i++) {
            char cmd = commands.charAt(i);
            boolean isMove = false;

            // Robot P commands (uppercase)
            if (cmd == 'F') {
                xP += dx[dirP];
                yP += dy[dirP];
                isMove = true;
            } else if (cmd == 'B') {
                xP -= dx[dirP];
                yP -= dy[dirP];
                isMove = true;
            } else if (cmd == 'R') {
                dirP = (dirP + 1) % 4;
            } else if (cmd == 'L') {
                dirP = (dirP + 3) % 4;
            }
            // Robot Q commands (lowercase)
            else if (cmd == 'f') {
                xQ += dx[dirQ];
                yQ += dy[dirQ];
                isMove = true;
            } else if (cmd == 'b') {
                xQ -= dx[dirQ];
                yQ -= dy[dirQ];
                isMove = true;
            } else if (cmd == 'r') {
                dirQ = (dirQ + 1) % 4;
            } else if (cmd == 'l') {
                dirQ = (dirQ + 3) % 4;
            }

            // Perform checks only after a MOVE command
            if (isMove) {
                // Task 1: Meetings (both on the same cell)
                if (xP == xQ && yP == yQ) {
                    meetings++;
                }

                // Task 2: Origin returns (moved robot at (0,0))
                if (Character.isUpperCase(cmd) && xP == 0 && yP == 0) {
                    originReturns++;
                } else if (Character.isLowerCase(cmd) && xQ == 0 && yQ == 0) {
                    originReturns++;
                }

                // Task 3: Maximum Gap (Manhattan Distance)
                int gap = Math.abs(xP - xQ) + Math.abs(yP - yQ);
                if (gap > maxGap) {
                    maxGap = gap;
                }
            }
        }

        // Step 5: Simplify the commands using simple String concatenation and substring.
        String result = "";

        for (int i = 0; i < commands.length(); i++) {
            char current = commands.charAt(i);

            // Find opposite command for the same robot
            char opposite = ' ';
            if (current == 'F') {
                opposite = 'B';
            } else if (current == 'B') {
                opposite = 'F';
            } else if (current == 'L') {
                opposite = 'R';
            } else if (current == 'R') {
                opposite = 'L';
            } else if (current == 'f') {
                opposite = 'b';
            } else if (current == 'b') {
                opposite = 'f';
            } else if (current == 'l') {
                opposite = 'r';
            } else if (current == 'r') {
                opposite = 'l';
            }

            // Check Cancel Rule: compare current with last kept character
            int lastIndex = result.length() - 1;
            if (lastIndex >= 0 && result.charAt(lastIndex) == opposite) {
                // Remove the last command
                result = result.substring(0, lastIndex);
            } else {
                // No cancellation, add current command
                result = result + current;

                // Check Spin Rule: 4 identical turns in a row
                if (result.length() >= 4) {
                    int len = result.length();
                    char c1 = result.charAt(len - 1);
                    char c2 = result.charAt(len - 2);
                    char c3 = result.charAt(len - 3);
                    char c4 = result.charAt(len - 4);

                    if (c1 == c2 && c2 == c3 && c3 == c4 &&
                       (c1 == 'R' || c1 == 'L' || c1 == 'r' || c1 == 'l')) {
                        // Remove all four identical turns
                        result = result.substring(0, len - 4);
                    }
                }
            }
        }

        // Display the results.
        System.out.println("Meetings: " + meetings);
        System.out.println("Origin Returns: " + originReturns);
        System.out.println("Maximum Gap: " + maxGap);
        System.out.println("Robot P Final: (" + xP + "," + yP + ") facing " + dirNames[dirP]);
        System.out.println("Robot Q Final: (" + xQ + "," + yQ + ") facing " + dirNames[dirQ]);

        if (result.isEmpty()) {
            System.out.println("Simplified Commands: EMPTY");
        } else {
            System.out.println("Simplified Commands: " + result);
        }
    }
}