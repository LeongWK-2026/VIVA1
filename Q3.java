package viva1;

import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int parcelsProcessed = 0;
        double totalShippingFees = 0;
        int heaviestChargeableWeight = Integer.MIN_VALUE;

        while (true) {
            System.out.print("Enter destination postcode (or EXIT to stop): ");
            String inputPostcode = sc.nextLine().trim();

            if (inputPostcode.equalsIgnoreCase("EXIT")) {
                break;
            }

            // 1. Normalization
            String cleaned = inputPostcode.replaceAll("[\\s-]", "").toUpperCase();
            if (cleaned.startsWith("MY")) {
                cleaned = cleaned.substring(2);
            }

            // 2. Destination Classification
            String zoneName = "";
            String displayPostcode = cleaned;

            if (cleaned.length() >= 3 && Character.isLetter(cleaned.charAt(0)) && Character.isLetter(cleaned.charAt(1))) {
                zoneName = "International (" + cleaned.substring(0, 2) + ")";
            } else if (cleaned.length() == 5 && cleaned.matches("\\d+")) { // \d+: Only have digit
                int areaCode = Integer.parseInt(cleaned.substring(0, 2));
                if (areaCode >= 50 && areaCode <= 60) {
                    zoneName = "Klang Valley";
                } else if ((areaCode >= 1 && areaCode <= 49) || (areaCode >= 61 && areaCode <= 86)) {
                    zoneName = "Peninsular";
                } else if (areaCode >= 87 && areaCode <= 98) {
                    zoneName = "East Malaysia";
                } else {
                    System.out.println("Error: Invalid postcode. Parcel skipped.\n");
                    continue;
                }
            } else {
                System.out.println("Error: Invalid postcode. Parcel skipped.\n");
                continue;
            }

            // 3. Weight Input
            int weightGrams = 0;
            while (true) {
                System.out.print("Enter parcel weight (g): ");
                if (sc.hasNextInt()) {
                    weightGrams = sc.nextInt();
                    sc.nextLine(); // clear buffer
                    if (weightGrams <= 0) {
                        System.out.println("Error: Weight must be greater than 0.");
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Error: Weight must be an integer.");
                    sc.nextLine(); // clear invalid input
                }
            }

            // 4. Dimensions Input
            int length = 0, width = 0, height = 0;
            while (true) {
                System.out.print("Enter dimensions LxWxH (cm): ");
                String dimStr = sc.nextLine().trim();
                String[] parts = dimStr.toLowerCase().split("x");

                if (parts.length == 3 && parts[0].matches("\\d+") && parts[1].matches("\\d+") && parts[2].matches("\\d+")) {
                    length = Integer.parseInt(parts[0]);
                    width = Integer.parseInt(parts[1]);
                    height = Integer.parseInt(parts[2]);

                    if (length > 0 && width > 0 && height > 0) {
                        break;
                    }
                }
                System.out.println("Error: Use the format LxWxH with positive numbers, e.g. 30x20x10.");
            }

            // 5. Chargeable Weight Calculation
            double actualKg = weightGrams / 1000.0;
            double volumetricKg = (length * width * height) / 5000.0;
            double maxKg = (actualKg > volumetricKg) ? actualKg : volumetricKg;
            int chargeableWeight = (int) Math.ceil(maxKg);

            // 6. Fee Calculation & Tiered Adjustment
            double baseFee = 0;
            if (zoneName.startsWith("International")) {
                baseFee = 35.00 * chargeableWeight;
            } else if (zoneName.equals("Klang Valley")) {
                baseFee = 6.00 + (chargeableWeight - 1) * 1.50;
            } else if (zoneName.equals("Peninsular")) {
                baseFee = 8.00 + (chargeableWeight - 1) * 2.00;
            } else if (zoneName.equals("East Malaysia")) {
                baseFee = 14.00 + (chargeableWeight - 1) * 4.50;
            }

            String adjustmentStr = "None";
            double finalFee = baseFee;

            if (chargeableWeight >= 5 && chargeableWeight <= 10) {
                adjustmentStr = "5% bulk discount";
                finalFee = baseFee * 0.95;
            } else if (chargeableWeight > 10) {
                adjustmentStr = "15% heavy parcel surcharge";
                finalFee = baseFee * 1.15;
            }

            // Display Summary
            System.out.println("\nShipping Summary");
            System.out.println("Postcode: " + displayPostcode);
            System.out.println("Zone: " + zoneName);
            System.out.printf("Actual Weight: %.2f kg | Volumetric Weight: %.2f kg%n", actualKg, volumetricKg);
            System.out.println("Chargeable Weight: " + chargeableWeight + " kg");
            System.out.println("Adjustment: " + adjustmentStr);
            System.out.printf("Shipping Fee: RM %.2f%n\n", finalFee);

            // Accumulate Report Data
            parcelsProcessed++;
            totalShippingFees += finalFee;
            if (chargeableWeight > heaviestChargeableWeight) {
                heaviestChargeableWeight = chargeableWeight;
            }
        }

        // 7. Final Report
        System.out.println("Parcels Processed: " + parcelsProcessed);
        System.out.printf("Total Shipping Fees: RM %.2f%n", totalShippingFees);
        System.out.println("Heaviest Chargeable Weight: " + heaviestChargeableWeight + " kg");
    }
}