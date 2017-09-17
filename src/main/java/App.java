
/**
 * File: c:\Users\Rudy\Desktop\html-parser\src\main\java\App.java
 * Created Date: Tuesday, September 12th 2017, 8:03:47 pm
 * Author: RuudyLee
 * -----
 * Last Modified: Sun Sep 17 2017
 * Modified By: RuudyLee
 * -----
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class App {
    public static void main(String[] args) throws IOException {
        File htmlFolder = new File("html");
        File[] listOfFiles = htmlFolder.listFiles();

        // List of every project in the html folder
        List<ProjectPayload> payloads = new ArrayList<>();

        // Iterate through each html file
        for (File f : listOfFiles) {
            ProjectPayload summaryPayload = new ProjectPayload(f);
            payloads.add(summaryPayload);
        }

        payloads.sort(new Comparator<ProjectPayload>() {
            @Override
            public int compare(ProjectPayload o1, ProjectPayload o2) {
                // We want it in reverse order (newest to oldest)
                return o2.getDateForSorting().compareTo(o1.getDateForSorting());
            }
        });

        // testing
        //Element mProductSummary = doc.select("tr[class=printableProductSummary]").first().select("table").first();
        generateExcelFromTable(payloads, "test.xlsx");
    }

    /** 
    * Generates the product summary page in excel based on the html data (payloads)
    * @param payloads html data
    * @param filename output filename
    */
    private static void generateExcelFromTable(List<ProjectPayload> payloads, String filename) throws IOException {
        // Hard-coded values that never change as far as I'm concerned
        final int NUM_COLUMNS = 12;
        String[] firstHeader = { "Project", "Location", "Developer", "Opening Date", "Unit Sizes", "Price Range",
                "Currently Available", "Original $PSF", "Sales for July, 2017", "No. of units Sold", "No. of units",
                "Construction Status" };
        String[] secondHeader = { "Project", "First Occupancy", "Site Status", "Construction Type", "Architect",
                "Interior Designer", "Sales Company", "Mortgage Company", "No. of Units", "No. of Stories",
                "Mntc. Fees", "Notes" };

        // XSSF generates .xlsx files
        SXSSFWorkbook wb = new SXSSFWorkbook(100);

        /////////////////////
        // PROJECT SUMMARY //
        /////////////////////
        SXSSFSheet sh = wb.createSheet();

        // Sheet description
        sh.setDefaultRowHeightInPoints(30f);
        sh.trackAllColumnsForAutoSizing(); // this needs to be called for auto-fit

        // Fonts
        Font headerFont = wb.createFont();
        headerFont.setFontName("Calibri Light");
        headerFont.setFontHeightInPoints((short) 13);
        headerFont.setBold(true);

        Font valueFont = wb.createFont();
        valueFont.setFontName("Calibri Light");
        valueFont.setFontHeightInPoints((short) 13);

        // CellStyle Descriptions
        CellStyle headerDescription = (CellStyle) wb.createCellStyle();
        headerDescription.setFont(headerFont);

        CellStyle valueDescription = (CellStyle) wb.createCellStyle();
        valueDescription.setFont(valueFont);
        valueDescription.setWrapText(true);

        // FIRST PART OF THE PROJECT SUMMARY SHEET //

        // Write the header
        Row firstHeaderRow = sh.createRow(0);
        for (int cellnum = 0; cellnum < NUM_COLUMNS; cellnum++) {
            Cell cell = firstHeaderRow.createCell(cellnum);
            cell.setCellValue(firstHeader[cellnum]);
            cell.setCellStyle(headerDescription);
        }

        // Write values
        for (int i = 0; i < payloads.size(); i++) {
            Row row = sh.createRow(i + 1);
            String[] payload = payloads.get(i).getProjectSummaryFirstHalf();
            for (int cellnum = 0; cellnum < NUM_COLUMNS; cellnum++) {
                Cell cell = row.createCell(cellnum);
                cell.setCellValue(payload[cellnum]);
                // Bold the left-most column
                if (cellnum == 0) {
                    cell.setCellStyle(headerDescription);
                } else {
                    cell.setCellStyle(valueDescription);
                }
            }
        }

        // SECOND PART OF THE PROJECT SUMMARY SHEET //
        // Write the header
        final int startingRow = payloads.size() + 1;
        Row secondHeaderRow = sh.createRow(startingRow);
        for (int cellnum = 0; cellnum < NUM_COLUMNS; cellnum++) {
            Cell cell = secondHeaderRow.createCell(cellnum);
            cell.setCellValue(secondHeader[cellnum]);
            cell.setCellStyle(headerDescription);
        }

        // Write values
        for (int i = 0; i < payloads.size(); i++) {
            Row row = sh.createRow(startingRow + i + 1); // Start after the first half
            String[] payload = payloads.get(i).getProjectSummarySecondHalf();
            for (int cellnum = 0; cellnum < NUM_COLUMNS; cellnum++) {
                Cell cell = row.createCell(cellnum);
                cell.setCellValue(payload[cellnum]);
                // Bold the left-most column
                if (cellnum == 0) {
                    cell.setCellStyle(headerDescription);
                } else {
                    cell.setCellStyle(valueDescription);
                }
            }
        }

        //////////////////////////////////////////

        // auto-fit columns
        for (int i = 0; i < NUM_COLUMNS; i++) {
            sh.autoSizeColumn(i);
        }

        // output the excel file
        File outDir = new File("output");
        outDir.mkdirs();
        FileOutputStream out = new FileOutputStream(outDir.getPath() + "/" + filename);
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
        wb.close();
    }
}