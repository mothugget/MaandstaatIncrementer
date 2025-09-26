package squadra;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        // If no arguments provided
        if (args.length == 0) {
            System.out.println("Need the following args <customer> <description> (<hours> - default is 1)");
            return;
        }
        if (args.length < 2) {
            System.out.println("Need the following args <customer> <description> (<hours>  - default is 1)");
            return;
        }
        if (isNumeric(args[0]) || isNumeric(args[1])) {
            System.out.println("First two arguments must be strings (customer, description).");
            return;
        }
        String customer = args[0];
        String description = args[1];

        float hours = 1;
        if (args.length > 2) {
            try {
                hours = Float.parseFloat(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Error: hours must be a number");
                return;
            }
        }

        System.out.println("Customer: " + customer + " | Description: " + description+" | Hours: "+hours);

        Dotenv dotenv = Dotenv.load();
        String filePath = dotenv.get("FILE_PATH");
        // Collect arguments into one string
        String input = String.join(" ", args);

        // Append to log.txt

        new MaandStaatManipulator(filePath, hours, description, customer);
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(LocalDateTime.now() + " - " + input + System.lineSeparator());
            System.out.println("Commit added to log file");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }
}
