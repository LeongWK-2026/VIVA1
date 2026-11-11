package viva1;

import java.util.Scanner;
public class Q4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Initialize hub battery capacity and vehicle details
        double hubBattery = 150.0;

        String[] vehicles = {
            "Compact Hatch", "Family SUV", "Delivery Van", "E-Scooter"
        };
        int[] capacities = {40, 75, 90, 4};
        int[] maxPowers = {50, 120, 80, 3};
        boolean[] charged = new boolean[4];

        int vehiclesCharged = 0;
        double totalEnergyDelivered = 0;
        double totalRevenue = 0;

        // Main charging loop
        while (hubBattery > 0) {
            System.out.println("===== Solar EV Charging Hub =====");
            System.out.printf("Hub Battery: %.2f kWh\n", hubBattery);

            for (int i = 0; i < vehicles.length; i++) {
                String status = charged[i] ? " [DONE]" : "";
                System.out.println((i + 1) + ". " + vehicles[i] + " (" + capacities[i] + " kWh, max " + maxPowers[i] + " kW)" + status);
            }
            System.out.println("0. Close hub");

            // Vehicle selection input & validation
            System.out.print("Select a vehicle (0-4): ");
            if (!sc.hasNextInt()) {
                System.out.println("Error: Please enter an integer from 0 to 4.\n");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            if (choice == 0) {
                System.out.println("Hub closed by operator.\n");
                break;
            }

            if (choice < 1 || choice > 4) {
                System.out.println("Error: Invalid vehicle selection.\n");
                continue;
            }

            int index = choice - 1;
            if (charged[index]) {
                System.out.println("Error: This vehicle has already been charged.\n");
                continue;
            }

            // Current battery level validation
            int currentLevel;
            while (true) {
                System.out.print("Enter current battery level (0-99): ");
                if (!sc.hasNextInt()) {
                    System.out.println("Error: Enter a whole-number percentage.");
                    sc.nextLine();
                    continue;
                }
                currentLevel = sc.nextInt();
                sc.nextLine();
                if (currentLevel >= 0 && currentLevel <= 99) {
                    break;
                }
                System.out.println("Error: Current level must be between 0 and 99.");
            }

            // Target battery level validation
            int targetLevel;
            while (true) {
                System.out.print("Enter target battery level (" + (currentLevel + 1) + "-100): ");
                if (!sc.hasNextInt()) {
                    System.out.println("Error: Enter a whole-number percentage.");
                    sc.nextLine();
                    continue;
                }
                targetLevel = sc.nextInt();
                sc.nextLine();
                if (targetLevel > currentLevel && targetLevel <= 100) {
                    break;
                }
                System.out.println("Error: Target must be higher than the current level and at most 100.");
            }

            // Calculations
            int capacity = capacities[index];
            int vehicleMaxPower = maxPowers[index];
            String vehicleName = vehicles[index];

            double delivered = capacity * (targetLevel - currentLevel) / 100.0;
            double drawn = delivered / 0.90;

            int chargingPower = (vehicleMaxPower < 60) ? vehicleMaxPower : 60;
            double chargingTime = (delivered / chargingPower) * 60;

            System.out.println("\n--- Charging Summary ---");
            System.out.println("Vehicle: " + vehicleName);

            if (drawn <= hubBattery) {
                // Full charge scenario
                hubBattery -= drawn;

                // Calculate Cost
                double cost = delivered * 1.20;
                if (delivered > 30) {
                    cost *= 0.95; // 5% discount
                }

                System.out.println("Battery: " + currentLevel + "% -> " + targetLevel + "%");
                System.out.println("Charging Power: " + chargingPower + " kW");
                System.out.printf("Energy Delivered: %.2f kWh (drawn from hub: %.2f kWh)%n", delivered, drawn);
                System.out.printf("Charging Time: %.2f minutes%n", chargingTime);
                System.out.printf("Cost: RM %.2f%n", cost);
                System.out.printf("Remaining Hub Battery: %.2f kWh%n", hubBattery);
                System.out.println("--------------------------\n");

                charged[index] = true;
                vehiclesCharged++;
                totalEnergyDelivered += delivered;
                totalRevenue += cost;
            } else {
                // Partial charge / Hub Depleted scenario
                double actualDrawn = hubBattery;
                double actualDelivered = actualDrawn * 0.90;
                double reachedPercent = currentLevel + (actualDelivered / capacity) * 100.0;
                double actualChargingTime = (actualDelivered / chargingPower) * 60;

                hubBattery = 0.0;

                double cost = actualDelivered * 1.20;
                if (actualDelivered > 30) {
                    cost *= 0.95;
                }

                System.out.println("Battery: " + currentLevel + "% -> " + targetLevel + "%");
                System.out.println("Charging Power: " + chargingPower + " kW");
                System.out.printf("Energy Delivered: %.2f kWh (drawn from hub: %.2f kWh)%n", actualDelivered, actualDrawn);
                System.out.printf("Charging Time: %.2f minutes%n", actualChargingTime);
                System.out.printf("Cost: RM %.2f%n", cost);
                System.out.printf("Remaining Hub Battery: %.2f kWh%n", hubBattery);
                System.out.printf("Hub Battery Depleted! Vehicle only reached %.2f%%.%n", reachedPercent);
                System.out.println("--------------------------\n");

                charged[index] = true;
                vehiclesCharged++;
                totalEnergyDelivered += actualDelivered;
                totalRevenue += cost;
                break;
            }
        }

        // Step 7: Final Report Output
        System.out.println("===== Hub Report =====");
        System.out.println("Vehicles Charged: " + vehiclesCharged);
        System.out.printf("Total Energy Delivered: %.2f kWh%n", totalEnergyDelivered);
        System.out.printf("Total Revenue: RM %.2f%n", totalRevenue);
        System.out.printf("Hub Battery Left: %.2f kWh%n", hubBattery);
    }
}