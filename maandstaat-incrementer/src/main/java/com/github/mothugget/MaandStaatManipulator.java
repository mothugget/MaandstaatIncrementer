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
    private Float hours;
    private String description;
    private String customer;

    public MaandStaatManipulator(String filePath, float hours, String description, String customer) {
        this.filePath = filePath;
        this.hours = hours;
        this.description = description;
        this.customer = customer;
        updateFile(filePath, hours, description, customer);
    }

    private void updateFile(String filePath, Float hours, String description, String customer) {
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the first sheet (index 0) – or use workbook.getSheet("SheetName")
            Sheet sheet = workbook.getSheet(customer);
            LocalDate today = LocalDate.now();
            int foundRow = -1;

            if (sheet == null) {
                System.out.println("Customer not found! Check sheet name");
                return;
            }

            for (Row row : sheet) {

                Cell cell = row.getCell(1);
                // try {
                //     System.out.println(cell.getNumericCellValue());
                // } catch (Exception e) {
                //     System.out.println("oops" + e);
                // }

                if (cell!=null && cell.getCellType() == CellType.FORMULA && DateUtil.isCellDateFormatted(cell)) {
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    // System.out.println(cellValue);
                    Date cellDate = cell.getDateCellValue();
                    LocalDate localDate = cellDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    System.out.println(localDate);
                    if (localDate.equals(today)) {
                        foundRow = row.getRowNum();
                        break;
                    }
                }

                if (foundRow != -1)
                    break;
            }
            System.out.println("Todays date on row " + foundRow);
            // // A4 = row 3 (0-based index), column 0
            // Row row = sheet.getRow(3);
            // if (row == null) {
            // row = sheet.createRow(3);
            // }
            // Cell cell = row.getCell(0);
            // if (cell == null) {
            // cell = row.createCell(0);
            // }

            // // Set new value
            // cell.setCellValue("Hello from Apache POI");

            // // Save back to the same file
            // try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // workbook.write(fos);
            // }

            // System.out.println("Cell A4 updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}