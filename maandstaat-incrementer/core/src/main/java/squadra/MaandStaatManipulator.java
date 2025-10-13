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

    private Row getDateRow(Sheet sheet, LocalDate date) {
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
    private String getNewStringCellValue(String oldValue,String newValue, String delimiter){
        String delimiterIfNotFirst = (oldValue.equals("") ? "" : delimiter);
        return oldValue + delimiterIfNotFirst + newValue;       
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

    public void updateFile(String filePath, float hours, String description, int kilometer, String location,
            String customer, LocalDate date) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the first sheet (index 0) – or use workbook.getSheet("SheetName")
            Sheet sheet = workbook.getSheet(customer);

            if (sheet == null) {
                System.out.println("Customer not found! Check sheet name");
                return;
            }
            Row row = getDateRow(sheet, date);

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
            Cell kilometerCell =row.getCell(5);
            if (kilometerCell == null) {
                kilometerCell = row.createCell(5);
            }
            Cell locationCell =row.getCell(6);
            if (locationCell == null) {
                locationCell = row.createCell(6);
            }
            String oldDescriptionValue = descriptionCell.getStringCellValue();
            float oldHoursValue = (float) hoursCell.getNumericCellValue();
            int oldKilometerValue = (int) kilometerCell.getNumericCellValue();
            String oldLocationValue = locationCell.getStringCellValue();
            String newDescriptionValue=getNewStringCellValue(oldDescriptionValue, description, "/");
            float newHoursValue = oldHoursValue + hours;
            int newKilometerValue=oldKilometerValue+kilometer;
            String newLocationValue=getNewStringCellValue(oldLocationValue, location, "/");
            

            System.out.println("Date - " + date.toString());
            System.out.println("New description - " + newDescriptionValue);
            System.out.println("New hours - " + newHoursValue);
            descriptionCell.setCellValue(newDescriptionValue);
            hoursCell.setCellValue(newHoursValue);
            if(kilometer>0){
                kilometerCell.setCellValue(newKilometerValue);
            }
            locationCell.setCellValue(newLocationValue);

            // Save back to the same file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("Maanstaat updated successfully!");

        } catch (Exception e) {
            throw e;

        }
    }
}