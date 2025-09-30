package squadra;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String filePath = dotenv.get("FILE_PATH");
        String environmentIncrementer = dotenv.get("DEFAULT_HOUR_INCREMENT");
        float hours=1;
        try {
            hours = Float.parseFloat(environmentIncrementer);
        } catch (NumberFormatException e) {
            System.out.println("Default incrementer in .env cannot be cast to a number. Using "+hours+" as default.");
        }

        // If no arguments provided
        if (args.length == 0) {
            System.out.println("Need the following args <customer> <description> (<hours> - default is "+hours+")");
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

        if (args.length > 2) {
            try {
                hours = Float.parseFloat(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Error: hours must be a number");
                return;
            }
        }

        System.out.println("Customer: " + customer + " | Description: " + description+" | Hours: "+hours);

        // Collect arguments into one string
        String input = String.join(" ", args);

        // Append to log.txt

        MaandStaatManipulator manipulator = new MaandStaatManipulator();
        manipulator.updateFile(filePath, hours, description, customer);
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
