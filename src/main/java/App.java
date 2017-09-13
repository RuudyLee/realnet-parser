
/* created on Sept 12, 2017 by Rudy Lee */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class App {
    public static void main(String[] args) throws IOException {
        File htmlFolder = new File("html");
        File[] listOfFiles = htmlFolder.listFiles();

        // List of every project in the html folder
        List<SummaryPayload> payloads = new ArrayList<>();

        // Iterate through each html file
        for (File f : listOfFiles) {
            // The values we want to pull from the html
            SummaryPayload summaryPayload = new SummaryPayload();

            // Read the html file
            Document doc = Jsoup.parse(new File(f.getAbsolutePath()), "utf-8"); // the html file
            Elements tables = doc.select("table[class=recordDetailFields"); // the tables

            // Parse the html for the values we need
            for (Element table : tables) {
                Elements rows = table.select("tr");
                for (Element e : rows) {
                    Elements keyVals = e.select("td");
                    String key = keyVals.get(0).text();
                    String val = "";
                    // if keyVals is less than 1, that means there is no value to go with the key
                    if (keyVals.size() > 1) {
                        val = keyVals.get(1).text();
                    }
                    summaryPayload.readValue(key, val);
                }
            }

            payloads.add(summaryPayload);
        }

        // testing
        //Element mProductSummary = doc.select("tr[class=printableProductSummary]").first().select("table").first();
        generateExcelFromTable(payloads, "test.xlsx");
    }

    private static void generateCSVFromTable(Element table, String filename) throws IOException {
        Elements rows = table.select("tr");

        // Headers
        // Elements ths = rows.select("th");
        // String thstr = "";
        // for (Element th : ths) {
        //     thstr += th.text() + "#";
        // }
        // csvWriter.writeNext(thstr.split("#"));

        rows.remove(0);
        // Content
        for (Element row : rows) {
            String line = "";
            Elements tds = row.select("td");
            for (Element td : tds) {
                line += td.text() + "#";
            }
            // write here
        }
    }

    private static void generateExcelFromTable(List<SummaryPayload> payloads, String filename) throws IOException {
        String[] headers = { "Project", "Location", "Developer", "Opening Date", "Unit Sizes", "Price Range",
                "Currently Available", "Original $PSF", "Sales for July, 2017", "No. of units Sold", "No. of units",
                "Construction Status" };
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet();

        // Write the header
        Row headerRow = sh.createRow(0);
        for (int cellnum = 0; cellnum < 12; cellnum++) {
            Cell cell = headerRow.createCell(cellnum);
            String address = headers[cellnum];
            cell.setCellValue(address);
        }

        // Write values
        for (int i = 0; i < payloads.size(); i++) {
            Row row = sh.createRow(i + 1);
            String[] payload = payloads.get(i).getAllValues();
            for (int cellnum = 0; cellnum < 12; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = payload[cellnum];
                cell.setCellValue(address);
            }
        }

        FileOutputStream out = new FileOutputStream(filename);
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
        wb.close();
    }
}