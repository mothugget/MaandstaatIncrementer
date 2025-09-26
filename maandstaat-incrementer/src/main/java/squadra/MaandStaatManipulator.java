package squadra;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MaandStaatManipulator {

    public MaandStaatManipulator(String filePath, float hours, String description, String customer) {
        updateFile(filePath, hours, description, customer);
    }

    private Row getTodaysRow(Sheet sheet) {
        LocalDate today = LocalDate.now();
        Row todayRow = null;
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
                return;
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
            String delimiter=(oldDescriptionValue=="")? "":"/";
            
            String newDescriptionValue = oldDescriptionValue + delimiter + description;
            float newHoursValue = oldHoursValue + hours;
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