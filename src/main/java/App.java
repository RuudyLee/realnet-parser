
/**
 * File: c:\Users\Rudy\Desktop\html-parser\src\main\java\App.java
 * Created Date: Tuesday, September 12th 2017, 8:03:47 pm
 * Author: RuudyLee
 * -----
 * Last Modified: Fri Sep 15 2017
 * Modified By: RuudyLee
 * -----
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class App {
    public static void main(String[] args) throws IOException {
        File htmlFolder = new File("html");
        File[] listOfFiles = htmlFolder.listFiles();

        // List of every project in the html folder
        List<SummaryPayload> payloads = new ArrayList<>();

        // Iterate through each html file
        for (File f : listOfFiles) {
            SummaryPayload summaryPayload = new SummaryPayload(f.getAbsolutePath());
            payloads.add(summaryPayload);
        }

        // testing
        //Element mProductSummary = doc.select("tr[class=printableProductSummary]").first().select("table").first();
        generateExcelFromTable(payloads, "test.xlsx");
    }

    /** 
    * Generates the product summary page in excel based on the html data (payloads)
    * @param payloads html data
    * @param filename output filename
    */
    private static void generateExcelFromTable(List<SummaryPayload> payloads, String filename) throws IOException {
        String[] headers = { "Project", "Location", "Developer", "Opening Date", "Unit Sizes", "Price Range",
                "Currently Available", "Original $PSF", "Sales for July, 2017", "No. of units Sold", "No. of units",
                "Construction Status" };
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        SXSSFSheet sh = wb.createSheet();

        // Sheet description
        sh.setDefaultRowHeightInPoints(30f);
        sh.trackAllColumnsForAutoSizing();

        // Properties for header
        Font headerFont = wb.createFont();
        headerFont.setFontName("Calibri Light");
        headerFont.setFontHeightInPoints((short) 13);
        headerFont.setBold(true);
        XSSFCellStyle headerDescription = (XSSFCellStyle) wb.createCellStyle();
        headerDescription.setFont(headerFont);

        // Write the header
        Row headerRow = sh.createRow(0);
        for (int cellnum = 0; cellnum < 12; cellnum++) {
            Cell cell = headerRow.createCell(cellnum);
            String value = headers[cellnum];
            cell.setCellValue(value);
            cell.setCellStyle(headerDescription);
        }

        // Properties for values
        Font valueFont = wb.createFont();
        valueFont.setFontName("Calibri Light");
        valueFont.setFontHeightInPoints((short) 13);
        XSSFCellStyle valueDescription = (XSSFCellStyle) wb.createCellStyle();
        valueDescription.setFont(valueFont);
        valueDescription.setWrapText(true);

        // Write values
        for (int i = 0; i < payloads.size(); i++) {
            Row row = sh.createRow(i + 1);
            String[] payload = payloads.get(i).getAllValues();
            for (int cellnum = 0; cellnum < 12; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String value = payload[cellnum];
                cell.setCellValue(value);

                // Bold the left-most column
                if (cellnum == 0) {
                    cell.setCellStyle(headerDescription);
                } else {
                    cell.setCellStyle(valueDescription);
                }
            }
        }

        // auto columns
        for (int i = 0; i < 12; i++) {
            sh.autoSizeColumn(i);
        }

        FileOutputStream out = new FileOutputStream("output/" + filename);
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
        wb.close();
    }
}