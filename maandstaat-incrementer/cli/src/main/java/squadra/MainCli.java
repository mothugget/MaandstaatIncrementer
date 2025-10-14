package squadra;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import io.github.cdimascio.dotenv.Dotenv;

public class MainCli {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String filePath = dotenv.get("FILE_PATH");
        String environmentIncrementer = dotenv.get("DEFAULT_HOUR_INCREMENT");
        int numberOfCustomers = 2;
        int daysInThePast = 0;
        LocalDate today = LocalDate.now();
        try {
            numberOfCustomers = Integer.parseInt(dotenv.get("NUMBER_OF_CUSTOMERS"));
        } catch (NumberFormatException e) {
            System.out
                    .println("Number of customers in .env cannot be cast to a number. Using " + numberOfCustomers
                            + " as default.");
        }
        MaandStaatManipulator manipulator = new MaandStaatManipulator();
        String[] customers = new String[numberOfCustomers];
        try {
            customers = MaandStaatManipulator.getCustomers(filePath, numberOfCustomers);
        } catch (IOException e) {
            System.out.println("Failed to find Maandstaat sheet\n" + e);
        }

        float hours = 1;
        try {
            hours = Float.parseFloat(environmentIncrementer);
        } catch (NumberFormatException e) {
            System.out
                    .println("Default incrementer in .env cannot be cast to a number. Using " + hours + " as default.");
        }

        // If no arguments provided
        if (args.length == 0) {
            System.out.println("Need the following args <customer> <description> (<hours> - default is " + hours
                    + ")\nPossible customer values: " + Arrays.toString(customers));
            return;
        }
        if (args.length < 2) {
            System.out.println("Need the following args <customer> <description> (<hours>  - default is " + hours
                    + ")\nPossible customer values: " + Arrays.toString(customers));
            return;
        }
        if (Utilities.isNumeric(args[0]) || Utilities.isNumeric(args[1])) {
            System.out.println("First two arguments must be strings (customer, description).");
            return;
        }
        String customer = args[0];
        String description = args[1];

        int kilometer = 0;
        String location = "";
        for (int i = 0; i < args.length; i++) {
            if (i > 1)
                switch (args[i]) {
                    case "-ditp" -> {
                        try {
                            daysInThePast = Integer.parseInt(args[i + 1]);
                        } catch (Exception e) {
                            System.err.println("Error: \"days in the past\" must be a number");
                            return;
                        }
                    }
                    case "-km" -> {
                        try {
                            kilometer = Integer.parseInt(args[i + 1]);
                        } catch (Exception e) {
                            System.err.println("Error: \"km\" must be a number");
                        }
                    }
                    case "-loc" -> {
                        try {
                            location = args[i + 1];
                        } catch (Exception e) {
                            System.err.println("Error: \"location\" must be a string");
                        }
                    }
                    default->{
                        if(i==2){
                            try {
                                hours=Float.parseFloat(args[i]);
                            }catch (Exception e){
                                System.err.println(e);
                            }
                        }
                    }
                }

        }
        String resultsPrint = "Customer: " + customer + " | Description: " + description + " | Hours: " + hours;
        if (kilometer > 0) {
            resultsPrint = resultsPrint + " | KM: " + kilometer;
        }
        if (!location.equals("")) {
            resultsPrint = resultsPrint + " | Location: " + location;
        }

        System.out.println(resultsPrint);

        // Collect arguments into one string
        String input = String.join(" ", args);

        // Append to log.txt
        LocalDate date = today.plusDays(-daysInThePast);
        try {
            manipulator.updateFile(filePath, hours, description, kilometer, location, customer, date);
        } catch (Exception e) {
            System.err.println(e);
        }
        manipulator.logHoursToTextFile(input);
    }

}
