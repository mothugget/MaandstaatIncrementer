package squadra;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MaandStaatManipulator {

    public void logHoursToTextFile(String input) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(LocalDateTime.now() + " - " + input + System.lineSeparator());
            System.out.println("Commit added to log file");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    private Row getDateRow(Sheet sheet,LocalDate date) {
        Row todayRow = null;
        for (Row row : sheet) {
            Cell cell = row.getCell(1);
            if (cell != null && cell.getCellType() == CellType.FORMULA && DateUtil.isCellDateFormatted(cell)) {
                Date cellDate = cell.getDateCellValue();
                LocalDate localDate = cellDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (localDate.equals(date)) {
                    todayRow = row;
                    break;
                }
            }
        }
        return todayRow;
    }

    public static String[] getCustomers(String filePath, int sheetCount) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {

            String[] sheetNames = new String[sheetCount];
            for (int i = 0; i < sheetCount; i++) {
                sheetNames[i] = workbook.getSheetName(i);
            }

            return sheetNames;
        }
    }

    public void updateFile(String filePath, float hours, String description, String customer, LocalDate date) {
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the first sheet (index 0) – or use workbook.getSheet("SheetName")
            Sheet sheet = workbook.getSheet(customer);

            if (sheet == null) {
                System.out.println("Customer not found! Check sheet name");
                return;
            }
            Row row = getDateRow(sheet,date);

            if (row == null) {
                throw new IOException("Date not found");
            }
            Cell descriptionCell = row.getCell(3);
            if (descriptionCell == null) {
                descriptionCell = row.createCell(3);
            }
            Cell hoursCell = row.getCell(4);
            if (hoursCell == null) {
                hoursCell = row.createCell(4);
            }
            String oldDescriptionValue = descriptionCell.getStringCellValue();
            float oldHoursValue = (float) hoursCell.getNumericCellValue();
            String delimiter = (oldDescriptionValue == "") ? "" : "/";

            String newDescriptionValue = oldDescriptionValue + delimiter + description;
            float newHoursValue = oldHoursValue + hours;
            System.out.println("Date - "+date.toString());
            System.out.println("New description - " + newDescriptionValue);
            System.out.println("New hours - " + newHoursValue);
            descriptionCell.setCellValue(newDescriptionValue);
            hoursCell.setCellValue(newHoursValue);

            // Save back to the same file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("Maanstaat updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }
}