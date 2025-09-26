package com.github.mothugget;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        // If no arguments provided
        if (args.length == 0) {
            System.out.println("Usage: java -jar target/maandstaat-incrementer-1.0-SNAPSHOT.jar [arguments]");
            return;
        }
        Dotenv dotenv = Dotenv.load();
        System.out.println("env Value" + dotenv.get("TEST"));
        // Collect arguments into one string
        String input = String.join(" ", args);

        // Print to console
        System.out.println("Received arguments: " + input);

        // Append to log.txt
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(LocalDateTime.now() + " - " + input + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
        MaandStaatManipulator manipulator= new MaandStaatManipulator("/Users/karlfredriksson/Documents/Maandstaat/MaaandstaatDemo.xlsm",1,"","Rijkzwaan");
    }
}
