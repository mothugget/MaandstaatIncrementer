package com.github.mothugget;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MaandStaatManipulator {
    private String filePath;
    private float hours;
    private String description;
    private String customer;

    public MaandStaatManipulator(String filePath, float hours, String description, String customer) {
        updateFile(filePath, hours, description, customer);
    }

    private Row getTodaysRow(Sheet sheet) {
        LocalDate today = LocalDate.now();
        Row todayRow = null;
        System.err.println("assigned");
        for (Row row : sheet) {
            Cell cell = row.getCell(1);
            if (cell != null && cell.getCellType() == CellType.FORMULA && DateUtil.isCellDateFormatted(cell)) {
                Date cellDate = cell.getDateCellValue();
                LocalDate localDate = cellDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (localDate.equals(today)) {
                    todayRow = row;
                    break;
                }
            }
        }
        return todayRow;
    }

    private void updateFile(String filePath, Float hours, String description, String customer) {
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the first sheet (index 0) – or use workbook.getSheet("SheetName")
            Sheet sheet = workbook.getSheet(customer);

            if (sheet == null) {
                System.out.println("Customer not found! Check sheet name");
                return;
            }
            Row row = getTodaysRow(sheet);

            if (row == null) {
                System.out.println("Cant find todays date");
            }
            Cell descriptionCell = row.getCell(3);
            if (descriptionCell == null) {
                descriptionCell = row.createCell(3);
            }
            String oldDescriptionValue=descriptionCell.getStringCellValue();
            System.out.println(oldDescriptionValue);
            // Set new value
            //descriptionCell.setCellValue("Hello from Apache POI");

            // Save back to the same file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("Cell A4 updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}